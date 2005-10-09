package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

@ExposedAs("Random")
@Bindings("Object")
public class KlonRandom extends KlonObject {

  private static final long serialVersionUID = -7916992470486962761L;

  public KlonRandom() {

  }

  public KlonRandom(State state) {
    super(state);
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
    KlonMessage.assertArgumentCount(message, 1);
    ((Random) receiver.getData()).setSeed(KlonNumber.evalAsNumber(context,
        message, 0).longValue());
    return receiver;
  }

  @ExposedAs("next")
  public static KlonObject next(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    double result = ((Random) receiver.getData()).nextDouble();
    int count = KlonMessage.getArgumentCount(message);
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
