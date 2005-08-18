package klon;

public class KlonSymbol extends KlonObject {

  public KlonSymbol() {
    super("");
  }

  public KlonSymbol(Object value) throws KlonException {
    super(Klon.ROOT.getSlot("Symbol"), value);
  }

  public KlonSymbol(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonSymbol.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonSymbol(this, primitive);
  }

  @Override
  public String toString() {
    return primitive.toString();
  }

}
