package klon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

@ExposedAs("String")
@Bindings("Object")
public class KlonString extends KlonObject {

  private static final long serialVersionUID = -7547715800603443713L;
  private static Charset charset = Charset.forName("ISO-8859-15");
  private static CharsetDecoder decoder = charset.newDecoder();

  public static KlonObject newString(KlonObject root, String value)
      throws KlonObject {
    KlonObject result = root.getSlot("String")
      .clone();
    result.setData(value);
    return result;
  }

  public static KlonObject newString(KlonObject root, File file)
      throws KlonObject {
    ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      FileChannel channel = in.getChannel();
      while (channel.read(byteBuffer) > 0) {
      }
    } catch (Exception e) {
      throw KlonException.newException(root, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
    byteBuffer.position(0);
    return newString(root, new Buffer(byteBuffer.array()));
  }

  public static KlonObject newString(KlonObject root, Buffer byteBuffer)
      throws KlonObject {
    CharBuffer buffer;
    try {
      buffer = decoder.decode(ByteBuffer.wrap(byteBuffer.array()));
    } catch (CharacterCodingException e) {
      throw KlonException.newException(root, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    }
    return newString(root, buffer.toString());
  }

  public static String evalAsString(KlonObject receiver, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(receiver, index);
    if ("String".equals(result.getType())) {
      return (String) result.getData();
    }
    throw KlonException.newException(receiver, "Object.invalidArgument",
      "argument must evaluate to a string", message);
  }

  public KlonString(KlonState state) {
    super(state);
    data = "";
  }

  @SuppressWarnings("unused")
  @Override
  public int compareTo(KlonObject other) {
    int result;
    if ("String".equals(other.getType())) {
      result = ((String) getData()).compareTo((String) other.getData());
    } else {
      result = hashCode() - other.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "\"" + data + "\"";
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonString(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  @Override
  public String getType() {
    return "String";
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message printMessage = new Compiler(receiver).fromString("asString");
    return KlonString.newString(receiver, receiver.getData()
        + String.valueOf(message.eval(context, 0)
          .perform(context, printMessage)
          .getData()));
  }

  @ExposedAs("beginsWith")
  public static KlonObject beginsWith(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((String) receiver.getData()).startsWith(KlonString.evalAsString(
      context, message, 0)) ? receiver : receiver.getSlot("Nil");
  }

  @ExposedAs("endsWith")
  public static KlonObject endsWith(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((String) receiver.getData()).endsWith(KlonString.evalAsString(
      context, message, 0)) ? receiver : receiver.getSlot("Nil");
  }

  @ExposedAs("split")
  public static KlonObject split(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    List<KlonObject> result = new ArrayList<KlonObject>();
    String delimiter = "\\s";
    if (message.getArgumentCount() > 0) {
      delimiter = KlonString.evalAsString(receiver, message, 0);
    }
    for (String current : ((String) receiver.getData()).split(delimiter)) {
      result.add(KlonString.newString(receiver, current));
    }
    return KlonList.newList(receiver, result);
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonBuffer.newBuffer(receiver, new Buffer(
      ((String) receiver.getData()).getBytes()));
  }

  @SuppressWarnings("unused")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }
}
