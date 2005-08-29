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

@Prototype(name = "Buffer", parent = "Object")
public class KlonBuffer extends KlonObject {

  private static Charset charset = Charset.forName("ISO-8859-15");
  private static CharsetDecoder decoder = charset.newDecoder();
  private static final long serialVersionUID = -3007245357989336566L;

  public KlonBuffer() {
    super(null, ByteBuffer.allocate(0));
  }

  public KlonBuffer(KlonObject parent, Object data) {
    super(parent, data);
  }

  public KlonBuffer newBuffer(File file) throws KlonException {
    ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      FileChannel channel = in.getChannel();
      while (channel.read(buffer) > 0) {
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
    buffer.position(0);
    return (KlonBuffer) duplicate(buffer);
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    CharBuffer buffer;
    try {
      buffer = decoder.decode(((ByteBuffer) receiver.getData()));
    } catch (CharacterCodingException e) {
      throw ((KlonException) receiver.getSlot("Exception")).newException(
        e.getClass()
          .getSimpleName(), e.getMessage(), null);
    }
    return receiver.getSlot("String")
      .duplicate(buffer.toString());
  }

}
