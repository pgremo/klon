package klon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Buffer implements Serializable {

  private static final long serialVersionUID = -579645227925145018L;

  private ByteBuffer buffer;

  public Buffer(ByteBuffer buffer) {
    this.buffer = buffer;
  }

  public ByteBuffer getBuffer() {
    return buffer;
  }

  public void setBuffer(ByteBuffer buffer) {
    this.buffer = buffer;
  }

  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.writeObject(buffer.array());
  }

  private void readObject(ObjectInputStream stream) throws IOException,
      ClassNotFoundException {
    buffer = ByteBuffer.wrap((byte[]) stream.readObject());
  }
}
