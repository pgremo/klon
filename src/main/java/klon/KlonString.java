package klon;

@Prototype(name = "String", parent = "Object")
public class KlonString extends KlonObject<String> {

  public KlonString() {
    super();
  }

  public KlonString(KlonObject parent, String attached) {
    super(parent, attached);
    this.prototype = KlonString.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonString.class);
  }

  @Override
  public KlonObject clone(String subject) {
    return new KlonString(this, subject);
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String").clone(
        String.valueOf(receiver.toString())
            + message.eval(context, 0).toString());
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String").clone(
        String.valueOf(receiver.getPrimitive()));
  }
}
