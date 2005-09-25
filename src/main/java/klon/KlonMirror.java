package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@ExposedAs("Mirror")
public class KlonMirror extends KlonObject {

  private static final long serialVersionUID = -2300471734654563252L;

  public static KlonObject newMirror(KlonObject root, Object value) {
    return root.getState().getMirror();
  }

  public KlonMirror() {

  }

  public KlonMirror(State state) {
    super(state);
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

  @Override
  public KlonObject clone() {
    return this;
  }

  @SuppressWarnings("unused")
  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver;
  }

}
