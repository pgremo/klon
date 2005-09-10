package klon;

import java.util.Random;

@Bindings("Object")
public class KlonRandom extends KlonObject {

  private static final long serialVersionUID = -7916992470486962761L;

  public static KlonObject prototype() {
    KlonObject result = new KlonRandom();
    result.setData(new MersenneTwister());
    return result;
  }

  public static Random evalAsRandom(KlonObject receiver, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(receiver, index);
    if ("Random".equals(result.getName())) {
      return (Random) result.getData();
    }
    throw KlonException.newException(receiver, "Illegal Argument",
        "argument must evaluate to a Random", message);
  }

  @Override
  public KlonObject duplicate(KlonObject value) throws KlonObject {
    KlonObject result = new KlonRandom();
    result.bind(value);
    result.setData(((MersenneTwister) result.getData()).clone());
    return result;
  }

  @Override
  public String getName() {
    return "Random";
  }

  @ExposedAs("setSeed")
  public static KlonObject setSeed(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    ((Random) receiver.getData()).setSeed(KlonNumber.evalAsNumber(context,
        message, 0).longValue());
    return receiver;
  }

  @ExposedAs("next")
  public static KlonObject next(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, ((Random) receiver.getData())
        .nextDouble());
  }

}
