package klon;

@Prototype(name = "String", parent = "Object")
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
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonString.class);
  }

  @Override
  public KlonObject clone() {
    return new KlonString(this, primitive);
  }

  @Override
  public String toString() {
    return primitive;
  }

  @ExposedAs("+")
  public static KlonObject append(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return new KlonString(receiver.getPrimitive().toString()
        + message.eval(context, 0).getPrimitive().toString());
  }

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    System.out.print(receiver.getPrimitive());
    return receiver.getSlot("Nil");
  }

}
