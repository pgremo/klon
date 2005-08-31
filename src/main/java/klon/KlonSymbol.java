package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject {

  private static final long serialVersionUID = 3052106897048736269L;

  public KlonSymbol() {
    super(null, "");
  }

  public KlonSymbol(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  public KlonSymbol newSymbol(String value) throws KlonObject {
    KlonSymbol result = (KlonSymbol) duplicate();
    result.data = value;
    return result;
  }

  @Override
  public String toString() {
    return String.valueOf(data);
  }

}
