package klon;

@Prototype(name = "String", parent = "Object")
public class KlonString extends KlonObject {

  public KlonString() {
    super(null, "");
  }

  public KlonString(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonString(this, subject);
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String")
      .clone(String.valueOf(receiver.toString()) + message.eval(context, 0)
        .toString());
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String")
      .clone(String.valueOf(receiver.getPrimitive()));
  }
}
