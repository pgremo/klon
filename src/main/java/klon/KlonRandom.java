package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

@ExposedAs("Random")
@Bindings("Object")
public class KlonRandom extends KlonObject {

  private static final long serialVersionUID = -7916992470486962761L;

  public static Random evalAsRandom(KlonObject context, KlonMessage message,
      int index) throws KlonObject {
    KlonObject result = message.evalArgument(context, index);
    if ("Random".equals(result.getType())) {
      return (Random) result.getData();
    }
    throw KlonException.newException(context, "Object.invalidArgument",
      "argument must evaluate to a Random", message);
  }

  public KlonRandom() {

  }

  public KlonRandom(State state) {
    super(state);
    setType("Random");
    setData(new Random());
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonRandom(getState());
    result.bind(this);
    result.setData(new Random());
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

  @ExposedAs("setSeed")
  public static KlonObject setSeed(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    ((Random) receiver.getData()).setSeed(KlonNumber.evalAsNumber(context,
      message, 0)
      .longValue());
    return receiver;
  }

  @ExposedAs("next")
  public static KlonObject next(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    double result = ((Random) receiver.getData()).nextDouble();
    int count = message.getArgumentCount();
    if (count > 0) {
      double max = KlonNumber.evalAsNumber(context, message, count - 1);
      double min = 0;
      if (count > 1) {
        min = KlonNumber.evalAsNumber(context, message, 0);
      }
      result = min + ((max - min) * result);
    }
    return KlonNumber.newNumber(receiver, result);
  }

}
