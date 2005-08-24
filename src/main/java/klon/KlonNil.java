package klon;

@Prototype(name = "Nil", parent = "Object")
public class KlonNil extends KlonObject {

  public KlonNil() {
    super();
  }

  @Override
  public KlonObject duplicate(Object subject) {
    return this;
  }

  @Override
  public String toString() {
    return "Nil";
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String")
      .duplicate("");
  }

  @SuppressWarnings("unused")
  @ExposedAs("and")
  public static KlonObject and(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver;
  }

  @ExposedAs("or")
  public static KlonObject or(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject other = message.eval(context, 0);
    return receiver.getSlot("Nil")
      .equals(other) ? receiver : other;
  }

  @ExposedAs("ifNil")
  public static KlonObject ifNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return message.eval(context, 0);
  }

  @ExposedAs("isNil")
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Klon");
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.equals(message.eval(receiver, 0))
        ? receiver.getSlot("Klon")
        : receiver.getSlot("Nil");
  }

}
