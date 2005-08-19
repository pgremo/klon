package klon;


public class KlonString extends KlonObject<String> {

  public KlonString() {
    super("");
  }

  public KlonString(String value) throws KlonException {
    super(Klon.ROOT.getSlot("String"), value);
  }

  public KlonString(KlonObject parent, String attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonString.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonString(this, primitive);
  }

  @Override
  public String toString() {
    return "\"" + primitive + "\"";
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context, Message message)
      throws KlonException {
    if (receiver instanceof KlonString) {
      return new KlonString((String) receiver.getPrimitive()
          + message.evalAsString(context, 0));
    }
    throw new KlonException("Illegal Argument for +");
  }

}
