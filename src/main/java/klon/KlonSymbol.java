package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject {

  private static final long serialVersionUID = 3052106897048736269L;

  public KlonSymbol() {
    super();
    setData("");
  }

  public KlonSymbol newSymbol(String value) throws KlonObject {
    KlonSymbol result = (KlonSymbol) duplicate();
    result.setData(value);
    return result;
  }

  @Override
  public String toString() {
    return String.valueOf(getData());
  }

}
