package klon;

@Prototype(name = "Locals", bindings = "Object")
public class KlonLocals extends Identity {

  private static final long serialVersionUID = 1963856865914651978L;

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonLocals());
    return result;
  }

  public static KlonObject newLocals(KlonObject root, KlonObject self)
      throws KlonObject {
    KlonObject result = root.getSlot("Locals").duplicate();
    result.setSlot("self", self);
    result.setData(self.getData());
    return result;
  }

}
