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
    type = "Locals";
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

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonLocals(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  @ExposedAs("updateSlot")
  public static KlonObject updateSlot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject value = message.evalArgument(context, 1);
    KlonObject result = receiver.updateSlot(name, value);
    if (result == null) {
      forward(receiver, context, message);
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject self = receiver.getSlot("self");
    if (self == null || self == receiver) {
      KlonObject.forward(receiver, context, message);
    }
    return self.perform(context, message);
  }

}
