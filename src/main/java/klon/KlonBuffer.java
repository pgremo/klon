package klon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Bindings("Object")
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

  @Override
  public KlonObject duplicate(KlonObject value) throws KlonObject {
    KlonObject result = super.duplicate(value);
    result.setData(((Buffer)value.getData()).clone());
    return result;
  }

  @ExposedAs("type")
  public static String type = "Buffer";

  @ExposedAs("asNumber")
  public static KlonObject asNumber(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    int size = 8;
    if (message.getArgumentCount() > 0) {
      size = KlonNumber.evalAsNumber(receiver, message, 0)
        .intValue();
    }
    size = Math.min(size, 8);
    return KlonNumber.newNumber(receiver, size, (Buffer) receiver.getData());
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, (Buffer) receiver.getData());
  }

}
