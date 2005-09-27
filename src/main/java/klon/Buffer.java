package klon;

import java.io.Serializable;

public class Buffer implements Serializable, Cloneable {

  private static final long serialVersionUID = -579645227925145018L;

  private byte[] data;
  private int count;

  public Buffer() {
    this(0);
  }

  public Buffer(int count) {
    this(new byte[count]);
  }

  public Buffer(byte[] data) {
    this.data = data;
    this.count = data.length;
  }

  public void put(int position, byte value) {
    int newCount = count + 1;
    if (newCount > data.length) {
      byte newData[] = new byte[Math.max(data.length << 1, newCount)];
      System.arraycopy(data, 0, newData, 0, count);
      data = newData;
    }
    data[count] = value;
    count = newCount;
  }

  public byte get(int position) {
    if (position >= count) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return data[position];
  }

  public void putDouble(int position, double value) {
    long target = Double.doubleToRawLongBits(value);
    for (int i = 0; i < 8; i++) {
      put(position + i, (byte) (target >> (i * 8)));
    }
  }

  public double getDouble(int position) {
    long result = 0;
    for (int i = 0; i < 8; i++) {
      result |= ((long) get(position) & 0xff) << (i * 8);
    }
    return Double.longBitsToDouble(result);
  }

  @Override
  public Object clone() {
    byte[] newData = new byte[count];
    System.arraycopy(data, 0, newData, 0, count);
    return new Buffer(newData);
  }

  @Override
  public String toString() {
    return new String(data, 0, count);
  }

}
