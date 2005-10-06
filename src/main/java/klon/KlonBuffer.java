package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@ExposedAs("Buffer")
@Bindings("Object")
public class KlonBuffer extends KlonObject {

  private static final long serialVersionUID = -5905250334048375486L;

  public static KlonObject newBuffer(KlonObject root, Buffer value)
      throws KlonObject {
    KlonObject result = root.getSlot("Buffer")
      .clone();
    result.setData(value);
    return result;
  }

  public KlonBuffer() {

  }

  public KlonBuffer(State state) {
    super(state);
    setData(new Buffer());
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonBuffer(getState());
    result.bind(this);
    result.setData(((Buffer) getData()).clone());
    return result;
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    setData(in.readObject());
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(getData());
  }

  @ExposedAs("asNumber")
  public static KlonObject asNumber(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result;
    try {
      Buffer buffer = (Buffer) receiver.getData();
      result = KlonNumber.newNumber(receiver, buffer.getDouble(0));
    } catch (RuntimeException e) {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, new String(
      ((Buffer) receiver.getData()).toString()));
  }

}
