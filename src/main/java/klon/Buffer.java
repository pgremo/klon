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

  public void put(byte value) {
    int newCount = count + 1;
    if (newCount > data.length) {
      byte newData[] = new byte[Math.max(data.length << 1, newCount)];
      System.arraycopy(data, 0, newData, 0, count);
      data = newData;
    }
    data[count] = value;
    count = newCount;
  }

  public void putNumber(int position, int size, double value) {
    long target = Double.doubleToRawLongBits(value);
    for (int i = 0; i < size; i++) {
      data[position + i] = (byte) (target >> (i * 8));
    }
  }

  public double getNumber(int position, int size) {
    long result = 0;
    for (int i = 0; i < size; i++) {
      result |= ((long) data[position + i] & 0xff) << (i * 8);
    }
    return Double.longBitsToDouble(result);
  }

  public byte[] array() {
    return data;
  }

}
