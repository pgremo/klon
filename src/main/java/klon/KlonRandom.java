package klon;

import java.util.Random;

@ExposedAs("Random")
@Bindings("Object")
public class KlonRandom extends KlonObject {

  private static final long serialVersionUID = -7916992470486962761L;

  public static Random evalAsRandom(KlonObject context, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(context, index);
    if ("Random".equals(result.getType())) {
      return (Random) result.getData();
    }
    throw KlonException.newException(context, "Object.invalidArgument",
      "argument must evaluate to a Random", message);
  }

  public KlonRandom(State state) {
    super(state);
    data = new MersenneTwister();
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonRandom(state);
    result.bind(this);
    result.setData(((MersenneTwister) data).clone());
    return result;
  }

  @Override
  public String getType() {
    return "Random";
  }

  @ExposedAs("setSeed")
  public static KlonObject setSeed(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    ((Random) receiver.getData()).setSeed(KlonNumber.evalAsNumber(context,
      message, 0)
      .longValue());
    return receiver;
  }

  @ExposedAs("next")
  public static KlonObject next(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      ((Random) receiver.getData()).nextDouble());
  }

}
