package klon;

@Bindings("Object")
public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = 1963856865914651978L;

  public static KlonObject prototype() {
    return new KlonLocals();
  }

  public static KlonObject newLocals(KlonObject root, KlonObject self)
      throws KlonObject {
    KlonObject result = root.getSlot("Locals").clone();
    result.setSlot("self", self);
    result.setData(self.getData());
    return result;
  }

  @Override
  public String getName() {
    return "Locals";
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonLocals();
    result.bind(this);
    result.setData(data);
    return result;
  }

}
