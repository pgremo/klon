package klon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Prototype(name = "Buffer", parent = "Object")
public class KlonBuffer extends KlonObject {

  private static final long serialVersionUID = -3007245357989336566L;

  public KlonBuffer() {
    super(null, ByteBuffer.allocate(0));
  }

  public KlonBuffer(KlonObject parent, Object data) {
    super(parent, data);
  }

  public KlonBuffer newBuffer(File file) throws KlonObject {
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
    KlonBuffer result = (KlonBuffer) duplicate();
    result.setData(buffer);
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonString) receiver.getSlot("String")).newString((ByteBuffer) receiver.getData());
  }

}
