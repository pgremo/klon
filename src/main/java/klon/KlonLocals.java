package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@ExposedAs("Locals")
@Bindings("Object")
public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = 1963856865914651978L;

  public KlonLocals() {

  }

  public KlonLocals(State state) {
    super(state);
  }

  @Override
  public String getType() {
    return "Locals";
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
    KlonObject result = new KlonLocals(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

}
