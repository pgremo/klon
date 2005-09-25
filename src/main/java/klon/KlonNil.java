package klon;

@ExposedAs("Nil")
@Bindings("Object")
public class KlonNil extends KlonObject {

  private static final long serialVersionUID = -1742322624353726742L;

  public static KlonObject newNil(KlonObject root) {
    return root.getState().getNil();
  }

  public KlonNil() {

  }

  public KlonNil(State state) {
    super(state);
  }

  @Override
  public String getType() {
    return "Nil";
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, "");
  }

  @SuppressWarnings("unused")
  @ExposedAs( { "and", "&&", "then" })
  public static KlonObject noop(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver;
  }

  @ExposedAs( { "elseIf" })
  public static KlonObject elseIf(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonObject.ifBranch(receiver, context, message);
  }

  @ExposedAs( { "or", "||" })
  public static KlonObject or(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject other = message.evalArgument(context, 0);
    return KlonNil.newNil(receiver).equals(other) ? receiver : other;
  }

  @ExposedAs( { "ifNil", "ifFalse", "else" })
  public static KlonObject eval(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return message.evalArgument(context, 0);
  }

  @ExposedAs( { "isNil", "ifTrue" })
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver.getSlot("Klon");
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver.equals(message.evalArgument(context, 0)) ? receiver
        .getSlot("Klon") : KlonNil.newNil(receiver);
  }

  @ExposedAs("!=")
  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return !receiver.equals(message.evalArgument(context, 0)) ? receiver
        .getSlot("Klon") : KlonNil.newNil(receiver);
  }

  @SuppressWarnings("unused")
  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    receiver.getState().write("");
    return KlonNil.newNil(receiver);
  }

}
