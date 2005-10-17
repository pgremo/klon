package klon;

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
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    setSlot("forward", KlonNativeMethod.newNativeMethod(root, KlonVoid.class
        .getMethod("forward", KlonNativeMethod.PARAMETER_TYPES)));
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @SuppressWarnings("unused")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver;
  }

}
