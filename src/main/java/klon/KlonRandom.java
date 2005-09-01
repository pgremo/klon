package klon;

import java.util.Random;

@Prototype(name = "Random", parent = "Object")
public class KlonRandom {

  private static final long serialVersionUID = 9178710141040319542L;

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    result.setData(new MersenneTwister());
    return result;
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
