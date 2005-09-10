package klon;


@Bindings("Object")
public class KlonLocals extends Identity {

  private static final long serialVersionUID = 1963856865914651978L;

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonLocals());
    return result;
  }

  public static KlonObject newLocals(KlonObject root, KlonObject self)
      throws KlonObject {
    KlonObject result = root.getSlot("Locals")
      .duplicate();
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
    KlonObject result = new KlonObject();
    result.bind(value);
    result.setIdentity(new KlonLocals());
    result.setData(value.getData());
    return result;
  }

}
