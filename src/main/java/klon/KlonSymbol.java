package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends KlonObject {

  private static final long serialVersionUID = 3052106897048736269L;

  public KlonSymbol() {
    super();
    setData("");
  }

  public KlonObject newSymbol(String value) throws KlonObject {
    KlonObject result = duplicate();
    result.setData(value);
    return result;
  }

  public static String format(KlonObject value) {
    return value.getData()
      .toString();
  }

}
