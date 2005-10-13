package klon;

@ExposedAs("Nil")
@Bindings("Object")
public class KlonNil extends KlonObject {

  private static final long serialVersionUID = -1742322624353726742L;

  public static KlonObject newNil(KlonObject root) {
    return root.getState()
      .getNilObject();
  }

  public KlonNil() {

  }

  public KlonNil(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, "");
  }

  @SuppressWarnings("unused")
  @ExposedAs({"and", "&&", "then"})
  public static KlonObject mirror(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonObject.mirror(receiver, context, message);
  }

  @ExposedAs({"elseIf"})
  public static KlonObject elseIf(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonObject.ifBranch(receiver, context, message);
  }

  @ExposedAs({"or", "||"})
  public static KlonObject or(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject other = KlonMessage.evalArgument(message, context, 0);
    return receiver.equals(other) ? receiver : other;
  }

  @ExposedAs({"ifNil", "ifFalse", "else"})
  public static KlonObject eval(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return KlonMessage.evalArgument(message, context, 0);
  }

  @SuppressWarnings("unused")
  @ExposedAs({"isNil", "ifTrue"})
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver.getState()
      .getRoot();
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return receiver.equals(KlonMessage.evalArgument(message, context, 0))
        ? receiver.getState()
          .getRoot()
        : receiver;
  }

  @ExposedAs("!=")
  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return !receiver.equals(KlonMessage.evalArgument(message, context, 0))
        ? receiver.getState()
          .getRoot()
        : receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    receiver.getState()
      .write("");
    return receiver;
  }

}
