package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject {

  private static final long serialVersionUID = 3052106897048736269L;

  public KlonSymbol() {
    super();
  }

  public KlonSymbol(KlonObject parent, Object attached) {
    super(parent, attached);
  }

}
