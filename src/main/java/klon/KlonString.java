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

@Prototype(name = "String", parent = "Object")
public final class KlonString {

  private static Charset charset = Charset.forName("ISO-8859-15");
  private static CharsetDecoder decoder = charset.newDecoder();

  private KlonString() {

  }

  public static KlonObject newString(KlonObject root, String value)
      throws KlonObject {
    KlonObject result = root.getSlot("String")
      .duplicate();
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
    return newString(root, byteBuffer);
  }

  public static KlonObject newString(KlonObject root, ByteBuffer byteBuffer)
      throws KlonObject {
    CharBuffer buffer;
    try {
      buffer = decoder.decode(byteBuffer);
    } catch (CharacterCodingException e) {
      throw KlonException.newException(root, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    }
    return newString(root, buffer.toString());
  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    result.setData("");
    Configurator.setActivator(result, KlonString.class);
    Configurator.setDuplicator(result, KlonString.class);
    Configurator.setFormatter(result, KlonString.class);
    return result;
  }

  public static String format(KlonObject value) {
    return "\"" + value.getData()
      .toString() + "\"";
  }

  public static String evalAsString(KlonObject receiver, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(receiver, index);
    if ("String".equals(result.getSlot("type")
      .getData())) {
      return (String) result.getData();
    }
    throw KlonException.newException(receiver, "Illegal Argument",
      "argument must evaluate to a string", message);
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

  @SuppressWarnings("unused")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }
}
