package klon;

@ExposedAs("NoOp")
public class KlonNoOp extends KlonObject {

  private static final long serialVersionUID = -2300471734654563252L;

  public static KlonObject newNoOp(KlonObject root, Object value)
      throws KlonObject {
    KlonObject result = root.getSlot("NoOp")
      .clone();
    result.setData(value);
    return result;
  }

  public KlonNoOp(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonNoOp(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = (KlonObject) receiver.getData();
    if (result == null) {
      result = context.getSlot("Nil");
    }
    return result;
  }

}
