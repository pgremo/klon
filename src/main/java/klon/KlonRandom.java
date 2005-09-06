package klon;

import java.util.Random;

@Prototype(name = "Random", bindings = "Object")
public class KlonRandom extends Identity {

  private static final long serialVersionUID = -7916992470486962761L;

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setData(new MersenneTwister());
    result.setIdentity(new KlonRandom());
    return result;
  }

  public static Random evalAsRandom(KlonObject receiver, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(receiver, index);
    if ("Random".equals(result.getSlot("type").getData())) {
      return (Random) result.getData();
    }
    throw KlonException.newException(receiver, "Illegal Argument",
        "argument must evaluate to a Random", message);
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
