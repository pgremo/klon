package klon;

import java.util.Random;

@Prototype(name = "Random", parent = "Object")
public class KlonRandom extends KlonObject {

  private static final long serialVersionUID = 9178710141040319542L;

  public KlonRandom() {
    super(null, new MersenneTwister());
  }

  public KlonRandom(KlonObject parent, Object data) {
    super(parent, data);
  }

  @Override
  public KlonObject duplicate() throws KlonException {
    return super.duplicate(new MersenneTwister());
  }

  @ExposedAs("setSeed")
  public static KlonObject setSeed(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    ((Random) receiver.getData()).setSeed(KlonNumber.evalAsNumber(context,
        message, 0).longValue());
    return receiver;
  }

  @ExposedAs("next")
  public static KlonObject next(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number").duplicate(
        ((Random) receiver.getData()).nextDouble());
  }

}
