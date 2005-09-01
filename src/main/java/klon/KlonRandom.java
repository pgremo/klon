package klon;

import java.util.Random;

@Prototype(name = "Random", parent = "Object")
public final class KlonRandom {

  private KlonRandom() {

  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    result.setData(new MersenneTwister());
    Configurator.setActivator(result, KlonRandom.class);
    Configurator.setDuplicator(result, KlonRandom.class);
    Configurator.setFormatter(result, KlonRandom.class);
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
