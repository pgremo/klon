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
      throw KlonException.newException(receiver, "Object.doesNotExist",
        message.getSelector()
          .getData() + " does not exist", message);
    }
    return self.perform(context, message);
  }

}
