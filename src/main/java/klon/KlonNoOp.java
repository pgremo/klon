package klon;

public class KlonNoOp extends KlonObject {

  private static final long serialVersionUID = -3849427822460733188L;

  public KlonNoOp() {
    super();
  }

  public KlonNoOp(KlonObject parent, Object data) {
    super(parent, data);
  }

  @SuppressWarnings("unused")
  @Override
  public KlonObject perform(KlonObject context, Message message)
      throws KlonException {
    return (KlonObject) getData();
  }

}
