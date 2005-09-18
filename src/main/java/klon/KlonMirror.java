package klon;

@ExposedAs("Mirror")
public class KlonMirror extends KlonObject {

  private static final long serialVersionUID = -2300471734654563252L;

  public static KlonObject newNoOp(KlonObject root, Object value) {
    return root.getState().getMirror();
  }

  public KlonMirror(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @SuppressWarnings("unused")
  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }

}
