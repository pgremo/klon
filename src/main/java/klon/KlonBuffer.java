package klon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Prototype(name = "Buffer", bindings = "Object")
public class KlonBuffer extends Identity {

  private static final long serialVersionUID = -5905250334048375486L;

  public static KlonObject newBuffer(KlonObject root, File file)
      throws KlonObject {
    ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      FileChannel channel = in.getChannel();
      while (channel.read(buffer) > 0) {
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
    buffer.position(0);
    KlonObject result = root.getSlot("Buffer")
      .duplicate();
    result.setData(new Buffer(buffer.array()));
    return result;
  }

  public static KlonObject newBuffer(KlonObject root, Buffer value)
      throws KlonObject {
    KlonObject result = root.getSlot("Buffer")
      .duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setData(new Buffer());
    result.setIdentity(new KlonBuffer());
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, (Buffer) receiver.getData());
  }

}
