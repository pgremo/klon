package klon;

import java.io.Serializable;

public class Buffer implements Serializable {

  private static final long serialVersionUID = -579645227925145018L;

  private byte[] data;
  private int count;

  public Buffer() {
    this(0);
  }

  public Buffer(int count) {
    this.count = count;
    this.data = new byte[count];
  }

  public Buffer(byte[] data) {
    this.data = data;
    this.count = data.length;
  }

  public void add(byte value) {
    int newCount = count + 1;
    if (newCount > data.length) {
      byte newData[] = new byte[Math.max(data.length << 1, newCount)];
      System.arraycopy(data, 0, newData, 0, count);
      data = newData;
    }
    data[count] = value;
    count = newCount;
  }

  public byte[] array() {
    return data;
  }

}
