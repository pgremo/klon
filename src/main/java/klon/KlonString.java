package klon;

@Prototype(name = "String", parent = "Object")
public class KlonString extends KlonObject<String> {

  public KlonString() throws KlonException {
    super("");
  }

  public KlonString(String value) throws KlonException {
    super(Klon.ROOT.getSlot("String"), value);
  }

  public KlonString(KlonObject parent, String attached) {
    super(parent, attached);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonString.class);
  }

  @Override
  public KlonObject clone() {
    return new KlonString(this, primitive);
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return new KlonString(String.valueOf(receiver.toString())
        + message.eval(context, 0).toString());
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return new KlonString(String.valueOf(receiver.getPrimitive()));
  }
}
