package klon;

@Prototype(name = "Symbol", parent = "Object")
public final class KlonSymbol {

  private KlonSymbol() {

  }

  public static KlonObject newSymbol(KlonObject root, String value)
      throws KlonObject {
    KlonObject result = root.getSlot("Symbol")
      .duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setData("");
    Configurator.setActivator(result, KlonSymbol.class);
    Configurator.setDuplicator(result, KlonSymbol.class);
    Configurator.setFormatter(result, KlonSymbol.class);
    Configurator.setComparator(result, KlonSymbol.class);
    return result;
  }

  public static String format(KlonObject value) {
    return value.getData()
      .toString();
  }

}
