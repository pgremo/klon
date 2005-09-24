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
    KlonObject result = root.getSlot("String").clone();
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
    CharBuffer buffer;
    try {
      buffer = decoder.decode(ByteBuffer.wrap(new Buffer(byteBuffer.array())
          .array()));
    } catch (CharacterCodingException e) {
      throw KlonException.newException(root, e.getClass().getSimpleName(), e
          .getMessage(), null);
    }
    return newString(root, buffer.toString());
  }

  public static String evalAsString(KlonObject context, KlonMessage message,
      int index) throws KlonObject {
    KlonObject result = message.evalArgument(context, index);
    if ("String".equals(result.getType())) {
      return (String) result.getData();
    }
    throw KlonException.newException(context, "Object.invalidArgument",
        "argument must evaluate to a string", message);
  }

  public KlonString(State state) {
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

  @ExposedAs( { "+", "concatonate" })
  public static KlonObject append(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonMessage printMessage = receiver.getState().getAsString();
    return KlonString.newString(receiver, receiver.getData()
        + String.valueOf(message.evalArgument(context, 0).perform(context,
            printMessage).getData()));
  }

  @ExposedAs("beginsWith")
  public static KlonObject beginsWith(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return ((String) receiver.getData()).startsWith(KlonString.evalAsString(
        context, message, 0)) ? receiver : KlonNil.newNil(receiver);
  }

  @ExposedAs("endsWith")
  public static KlonObject endsWith(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return ((String) receiver.getData()).endsWith(KlonString.evalAsString(
        context, message, 0)) ? receiver : KlonNil.newNil(receiver);
  }

  @ExposedAs("lowerCase")
  public static KlonObject lowerCase(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, ((String) receiver.getData())
        .toLowerCase());
  }

  @ExposedAs("upperCase")
  public static KlonObject upperCase(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, ((String) receiver.getData())
        .toUpperCase());
  }

  @ExposedAs("split")
  public static KlonObject split(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    List<KlonObject> result = new ArrayList<KlonObject>();
    String delimiter = "\\s";
    if (message.getArgumentCount() > 0) {
      delimiter = KlonString.evalAsString(context, message, 0);
    }
    for (String current : ((String) receiver.getData()).split(delimiter)) {
      result.add(KlonString.newString(receiver, current));
    }
    return KlonList.newList(receiver, result);
  }

  @ExposedAs("replace")
  public static KlonObject replace(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    String search = KlonString.evalAsString(context, message, 0);
    String replace = KlonString.evalAsString(context, message, 1);
    String result = ((String) receiver.getData()).replaceAll(search, replace);
    return KlonString.newString(receiver, result);
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonBuffer.newBuffer(receiver, new Buffer(((String) receiver
        .getData()).getBytes()));
  }

  @SuppressWarnings("unused")
  @ExposedAs("asNumber")
  public static KlonObject asNumber(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Double.valueOf((String) receiver
        .getData()));
  }

  @SuppressWarnings("unused")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    receiver.getState().write(receiver.getData().toString() + "\n");
    return KlonNil.newNil(receiver);
  }

}
