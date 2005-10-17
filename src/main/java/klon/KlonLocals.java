package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = 1963856865914651978L;

  public KlonLocals() {

  }

  public KlonLocals(State state) {
    super(state);
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("updateSlot", KlonNativeMethod.newNativeMethod(root,
        KlonLocals.class.getMethod("updateSlot",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("forward", KlonNativeMethod.newNativeMethod(root, KlonLocals.class
        .getMethod("forward", KlonNativeMethod.PARAMETER_TYPES)));
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

  public static KlonObject updateSlot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject value = KlonMessage.evalArgument(message, context, 1);
    KlonObject result = receiver.updateSlot(name, value);
    if (result == null) {
      forward(receiver, context, message);
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject self = receiver.getSlot("self");
    if (self == null || self == receiver) {
      KlonObject.forward(receiver, context, message);
    }
    return self.perform(context, message);
  }

}
