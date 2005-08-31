package klon;

@Prototype(name = "NoOp")
public class KlonNoOp extends KlonObject {

  private static final long serialVersionUID = -3849427822460733188L;

  public KlonNoOp() {
    super();
  }

  public KlonNoOp(KlonObject parent, Object data) {
    super(parent, data);
  }

  public KlonNoOp newNoOp(Object value) throws KlonObject {
    KlonNoOp result = (KlonNoOp) duplicate();
    result.setData(value);
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
