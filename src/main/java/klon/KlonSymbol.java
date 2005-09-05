package klon;

@Prototype(name = "Symbol", parent = "Object")
public class KlonSymbol extends Identity {

  private static final long serialVersionUID = -3249031497578338078L;

  public static KlonObject newSymbol(KlonObject root, String value)
      throws KlonObject {
    KlonObject result = root.getSlot("Symbol").duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setData("");
    result.setIdentity(new KlonSymbol());
    return result;
  }

  @Override
  public String format(KlonObject value) {
    return value.getData().toString();
  }

}
