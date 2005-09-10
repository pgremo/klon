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

@Bindings("Object")
public class KlonString extends Identity {

  private static final long serialVersionUID = -7547715800603443713L;
  private static Charset charset = Charset.forName("ISO-8859-15");
  private static CharsetDecoder decoder = charset.newDecoder();

  public static KlonObject newString(KlonObject root, String value)
      throws KlonObject {
    KlonObject result = root.getSlot("String").duplicate();
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
      throw KlonException.newException(root, e.getClass().getSimpleName(), e
          .getMessage(), null);
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
      throw KlonException.newException(root, e.getClass().getSimpleName(), e
          .getMessage(), null);
    }
    return newString(root, buffer.toString());
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonString());
    result.setData("");
    return result;
  }

  @SuppressWarnings("unused")
  @Override
  public int compare(KlonObject receiver, KlonObject other) throws KlonObject {
    int result;
    if ("String".equals(other.getIdentity().getName())) {
      result = ((String) receiver.getData())
          .compareTo((String) other.getData());
    } else {
      result = receiver.hashCode() - other.hashCode();
    }
    return result;
  }

  @Override
  public String format(KlonObject value) {
    return "\"" + value.getData().toString() + "\"";
  }

  public static String evalAsString(KlonObject receiver, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(receiver, index);
    if ("String".equals(result.getIdentity().getName())) {
      return (String) result.getData();
    }
    throw KlonException.newException(receiver, "Illegal Argument",
        "argument must evaluate to a string", message);
  }

  @Override
  public String getName() {
    return "String";
  }

  @Override
  public KlonObject duplicate(KlonObject value) throws KlonObject {
    KlonObject result = new KlonObject();
    result.bind(value);
    result.setIdentity(new KlonString());
    result.setData(value.getData());
    return result;
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message printMessage = new Compiler(receiver).fromString("asString");
    return KlonString.newString(receiver, receiver.getData()
        + String.valueOf(message
            .eval(context, 0)
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
    return KlonBuffer.newBuffer(receiver, new Buffer(((String) receiver
        .getData()).getBytes()));
  }

  @SuppressWarnings("unused")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }
}
