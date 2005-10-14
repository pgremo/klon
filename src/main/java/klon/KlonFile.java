package klon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

@ExposedAs("File")
@Bindings("Object")
public class KlonFile extends KlonObject {

  private static final long serialVersionUID = 3159106516324355579L;
  private static Charset charset = Charset.forName("ISO-8859-15");
  private static CharsetDecoder decoder = charset.newDecoder();

  public static KlonObject newFile(KlonObject root, File file)
      throws KlonObject {
    KlonObject result = root.getSlot("File")
      .clone();
    result.setData(file);
    return result;
  }

  public KlonFile() {

  }

  public KlonFile(State state) {
    super(state);
    setData(new File("").getAbsoluteFile());
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    setData(in.readObject());
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(getData());
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonFile(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  @ExposedAs("path")
  public static KlonObject path(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    try {
      return KlonString.newString(receiver,
        ((File) receiver.getData()).getCanonicalPath());
    } catch (IOException e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), message);
    }
  }

  @ExposedAs("setPath")
  public static KlonObject setPath(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    receiver.setData(new File(KlonString.evalAsString(context, message, 0)).getAbsoluteFile());
    return receiver;
  }

  @ExposedAs("name")
  public static KlonObject name(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, ((File) receiver.getData()).getName());
  }

  @ExposedAs("parent")
  public static KlonObject parent(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonDirectory.newDirectory(receiver,
      ((File) receiver.getData()).getParentFile());
  }

  @SuppressWarnings("unused")
  @ExposedAs("exists")
  public static KlonObject exists(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result;
    if (((File) receiver.getData()).exists()) {
      result = receiver;
    } else {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ((File) receiver.getData()).length());
  }

  @ExposedAs("remove")
  public static KlonObject remove(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    File file = (File) receiver.getData();
    if (file.exists()) {
      file.delete();
    } else {
      throw KlonException.newException(receiver, "File.doesNotExist",
        file.getAbsolutePath() + " does not exist", message);
    }
    return receiver;
  }

  @ExposedAs("moveTo")
  public static KlonObject moveTo(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    File file = (File) receiver.getData();
    if (file.exists()) {
      File target = new File(KlonString.evalAsString(receiver, message, 0));
      if (target.exists()) {
        throw KlonException.newException(receiver, "File.nameConflict",
          target.getAbsolutePath() + " already exist", message);
      }
      file.renameTo(target);
    } else {
      throw KlonException.newException(receiver, "File.doesNotExist",
        file.getAbsolutePath() + " does not exist", message);
    }
    return receiver;
  }

  @ExposedAs("readLines")
  public static KlonObject readLines(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    List<KlonObject> result = new ArrayList<KlonObject>();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader((File) receiver.getData()));
      String line = in.readLine();
      while (line != null) {
        result.add(KlonString.newString(receiver, line));
        line = in.readLine();
      }
    } catch (IOException e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), message);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
    return KlonList.newList(receiver, result);
  }

  @SuppressWarnings({"unchecked", "unused"})
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject result = KlonNil.newNil(receiver);
    int arg = 0;
    String index = null;
    if (KlonMessage.getArgumentCount(message) == 3) {
      index = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, arg++))
        .getData();
    }
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, arg++))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, arg);
    FileInputStream in = null;
    try {
      in = new FileInputStream((File) receiver.getData());
      int current = in.read();
      int count = 1;
      while (current != -1) {
        if (index != null) {
          context.setSlot(index, KlonNumber.newNumber(receiver, (double) count));
        }
        context.setSlot(value, KlonNumber.newNumber(receiver, (double) current));
        result = KlonMessage.eval(code, context, context);
        current = in.read();
        count++;
      }
    } catch (IOException e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), message);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e1) {
        }
      }
    }
    return result;
  }

  private static ByteBuffer readIntoBuffer(KlonObject receiver)
      throws KlonObject {
    File file = (File) receiver.getData();
    ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      FileChannel channel = in.getChannel();
      while (channel.read(buffer) > 0) {
      }
    } catch (Exception e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
    return buffer;
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonBuffer.newBuffer(receiver, new Buffer(
      readIntoBuffer(receiver).array()));
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    ByteBuffer buffer = readIntoBuffer(receiver);
    buffer.position(0);
    CharBuffer result;
    try {
      result = decoder.decode(buffer);
    } catch (CharacterCodingException e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    }
    return KlonString.newString(receiver, result.toString());
  }

}
