package klon;

@Bindings("Object")
public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = 1963856865914651978L;

  public static KlonObject prototype() {
    return new KlonLocals();
  }

  public static KlonObject newLocals(KlonObject root, KlonObject self)
      throws KlonObject {
    KlonObject result = root.getSlot("Locals").duplicate();
    result.setSlot("self", self);
    result.setData(self.getData());
    return result;
  }

  @Override
  public String getName() {
    return "Locals";
  }

  @Override
  public KlonObject duplicate(KlonObject value) throws KlonObject {
    KlonObject result = new KlonLocals();
    result.bind(value);
    result.setData(value.getData());
    return result;
  }

}
