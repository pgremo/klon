package klon;

@Prototype(name = "String", parent = "Object")
public class KlonString extends KlonObject {

  private static final long serialVersionUID = -1460358337296215238L;

  public KlonString() {
    super(null, "");
  }

  public KlonString(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  public KlonString newString(String value) throws KlonException {
    KlonString result = (KlonString) duplicate();
    result.data = value;
    return result;
  }

  @Override
  public String toString() {
    return "\"" + String.valueOf(data) + "\"";
  }

  public static String evalAsString(KlonObject receiver, Message message,
      int index) throws KlonException {
    KlonObject result = message.eval(receiver, index);
    if ("String".equals(result.getType())) {
      return (String) result.getData();
    }
    throw ((KlonException) receiver.getSlot("Exception")).newException(
      "Illegal Argument", "argument must evaluate to a string", message);
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    Message printMessage = new Compiler(receiver).fromString("asString");
    return ((KlonString) receiver.getSlot("String")).newString(receiver.getData()
        + String.valueOf(message.eval(context, 0)
          .perform(context, printMessage)
          .getData()));
  }

  @SuppressWarnings("unused")
  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver;
  }
}
