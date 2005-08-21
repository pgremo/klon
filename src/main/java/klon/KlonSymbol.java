package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject<String> {

  public KlonSymbol() {
    super("");
  }

  public KlonSymbol(String value) throws KlonException {
    super(Klon.ROOT.getSlot("Symbol"), value);
  }

  public KlonSymbol(KlonObject parent, String attached) {
    super(parent, attached);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonSymbol.class);
  }

  @Override
  public KlonObject clone() {
    return new KlonSymbol(this, primitive);
  }

  @Override
  public String toString() {
    return primitive.toString();
  }

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    System.out.print(receiver.getPrimitive());
    return receiver.getSlot("Nil");
  }

}
