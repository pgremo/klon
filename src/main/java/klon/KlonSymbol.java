package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject {

  public KlonSymbol() {
    super();
  }

  public KlonSymbol(KlonObject parent, Object attached) {
    super(parent, attached);
  }

}
