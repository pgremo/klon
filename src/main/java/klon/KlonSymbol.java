package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject<String> {

  public KlonSymbol() {
    super();
  }

  public KlonSymbol(KlonObject parent, String attached) {
    super(parent, attached);
    this.prototype = KlonSymbol.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonSymbol.class);
  }

  @Override
  public KlonObject clone(String subject) {
    return new KlonSymbol(this, subject);
  }

}
