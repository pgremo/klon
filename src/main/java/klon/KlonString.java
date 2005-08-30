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
public class KlonString extends KlonObject {

  private static Charset charset = Charset.forName("ISO-8859-15");
  private static CharsetDecoder decoder = charset.newDecoder();

  private static final long serialVersionUID = -1460358337296215238L;

  public KlonString() {
    super(null, "");
  }

  public KlonString(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  public KlonString newString(String value) throws KlonException {
    KlonString result = (KlonString) duplicate();
    result.data = value;
    return result;
  }

  public KlonString newString(File file) throws KlonException {
    ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      FileChannel channel = in.getChannel();
      while (channel.read(byteBuffer) > 0) {
      }
    } catch (Exception e) {
      throw ((KlonException) getSlot("Exception")).newException(e.getClass()
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
    return newString(byteBuffer);
  }

  public KlonString newString(ByteBuffer byteBuffer) throws KlonException {
    CharBuffer buffer;
    try {
      buffer = decoder.decode(byteBuffer);
    } catch (CharacterCodingException e) {
      throw ((KlonException) getSlot("Exception")).newException(e.getClass()
        .getSimpleName(), e.getMessage(), null);
    }
    return newString(buffer.toString());
  }

  @Override
  public String toString() {
    return "\"" + String.valueOf(data) + "\"";
  }

  public static String evalAsString(KlonObject receiver, Message message,
      int index) throws KlonException {
    KlonObject result = message.eval(receiver, index);
    if ("String".equals(result.getType())) {
      return (String) result.getData();
    }
    throw ((KlonException) receiver.getSlot("Exception")).newException(
      "Illegal Argument", "argument must evaluate to a string", message);
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    Message printMessage = new Compiler(receiver).fromString("asString");
    return ((KlonString) receiver.getSlot("String")).newString(receiver.getData()
        + String.valueOf(message.eval(context, 0)
          .perform(context, printMessage)
          .getData()));
  }

  @SuppressWarnings("unused")
  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver;
  }
}
