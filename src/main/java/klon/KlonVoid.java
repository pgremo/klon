package klon;

@ExposedAs("Void")
public class KlonVoid extends KlonObject {

  private static final long serialVersionUID = -2300471734654563252L;

  public static KlonObject newVoid(KlonObject root, Object value) {
    return root.getState().getVoidObject();
  }

  public KlonVoid() {

  }

  public KlonVoid(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @SuppressWarnings("unused")
  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver;
  }

}
