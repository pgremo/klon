package klon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@ExposedAs("Buffer")
@Bindings("Object")
public class KlonBuffer extends KlonObject {

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
    buffer.position(0);
    KlonObject result = root.getSlot("Buffer").clone();
    result.setData(new Buffer(buffer.array()));
    return result;
  }

  public static KlonObject newBuffer(KlonObject root, Buffer value)
      throws KlonObject {
    KlonObject result = root.getSlot("Buffer").clone();
    result.setData(value);
    return result;
  }

  public KlonBuffer() {

  }

  public KlonBuffer(State state) {
    super(state);
    data = new Buffer();
  }

  @Override
  public String getType() {
    return "Buffer";
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonBuffer(state);
    result.bind(this);
    result.setData(((Buffer) data).clone());
    return result;
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    data = in.readObject();
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(data);
  }

  @ExposedAs("asNumber")
  public static KlonObject asNumber(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    Buffer buffer = (Buffer) receiver.getData();
    return KlonNumber.newNumber(receiver, buffer.getDouble(0));
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, new String(((Buffer) receiver
        .getData()).toString()));
  }

}
