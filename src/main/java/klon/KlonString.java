package klon;

@Prototype(name = "String", parent = "Object")
public class KlonString extends KlonObject {

  public KlonString() {
    super(null, "");
  }

  public KlonString(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    Message printMessage = new Compiler(receiver).fromString("asString");
    return receiver.getSlot("String")
      .duplicate(
        receiver.getPrimitive() + String.valueOf(message.eval(context, 0)
          .perform(context, printMessage)
          .getPrimitive()));
  }

  @SuppressWarnings("unused")
  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver;
  }
}
