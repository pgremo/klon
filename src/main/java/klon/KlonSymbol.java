package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject {

  public KlonSymbol() {
    super();
  }

  public KlonSymbol(KlonObject parent, Object attached) {
    super(parent, attached);
    this.prototype = KlonSymbol.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonSymbol.class);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonSymbol(this, subject);
  }

}
