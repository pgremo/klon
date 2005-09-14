package klon;

@ExposedAs("NoOp")
public class KlonNoOp extends KlonObject {

  private static final long serialVersionUID = -2300471734654563252L;

  public static KlonObject newNoOp(KlonObject root, Object value)
      throws KlonObject {
    return root.getSlot("NoOp");
  }

  public KlonNoOp(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonObject.noop(receiver, context, message);
  }

}
