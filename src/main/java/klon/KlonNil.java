package klon;

@ExposedAs("Nil")
@Bindings("Object")
public class KlonNil extends KlonObject {

  private static final long serialVersionUID = -1742322624353726742L;

  public static KlonObject newNil(KlonObject root) throws KlonObject {
    return root.getSlot("Nil");
  }

  public KlonNil(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @Override
  public String getType() {
    return "Nil";
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, "");
  }

  @SuppressWarnings("unused")
  @ExposedAs({"and", "&&", "then"})
  public static KlonObject noop(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }

  @ExposedAs({"elseIf"})
  public static KlonObject elseIf(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonObject.ifBranch(receiver, context, message);
  }

  @ExposedAs({"or", "||"})
  public static KlonObject or(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject other = message.eval(context, 0);
    return KlonNil.newNil(receiver)
      .equals(other) ? receiver : other;
  }

  @ExposedAs({"ifNil", "ifFalse", "else"})
  public static KlonObject eval(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return message.eval(context, 0);
  }

  @ExposedAs({"isNil", "ifTrue"})
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.getSlot("Klon");
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.equals(message.eval(receiver, 0))
        ? receiver.getSlot("Klon")
        : KlonNil.newNil(receiver);
  }

  @ExposedAs("!=")
  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return !receiver.equals(message.eval(context, 0))
        ? receiver.getSlot("Klon")
        : KlonNil.newNil(receiver);
  }

}
