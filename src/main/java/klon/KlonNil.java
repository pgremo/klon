package klon;

@ExposedAs("Nil")
@Bindings("Object")
public class KlonNil extends KlonObject {

  private static final long serialVersionUID = -1742322624353726742L;

  public static KlonObject newNil(KlonObject root) {
    return root.getState().getNilObject();
  }

  public KlonNil() {

  }

  public KlonNil(State state) {
    super(state);
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("asString", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("asString", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("and", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("&&", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("then", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("elseIf", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("ifBranch", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("or", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("or", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("||", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("or", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifNil", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("evaluate", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifFalse", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("evaluate", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("else", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("evaluate", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("isNil", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("isNil", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifTrue", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("isNil", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("==", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("isEquals", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("!=", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("isNotEquals", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("print", KlonNativeMethod.newNativeMethod(root, KlonNil.class
        .getMethod("print", KlonNativeMethod.PARAMETER_TYPES)));
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, "");
  }

  public static KlonObject or(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject other = KlonMessage.evalArgument(message, context, 0);
    return receiver.equals(other) ? receiver : other;
  }

  @SuppressWarnings("unused")
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver.getState().getRoot();
  }

  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return receiver.equals(KlonMessage.evalArgument(message, context, 0)) ? receiver
        .getState()
          .getRoot()
        : receiver;
  }

  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return !receiver.equals(KlonMessage.evalArgument(message, context, 0)) ? receiver
        .getState()
          .getRoot()
        : receiver;
  }

  @SuppressWarnings("unused")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    receiver.getState().write("");
    return receiver;
  }

}
