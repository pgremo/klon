package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject<String> {

  public KlonSymbol() throws KlonException {
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

}
