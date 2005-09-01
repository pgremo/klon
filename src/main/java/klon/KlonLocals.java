package klon;

@Prototype(name = "Locals", parent = "Object")
public final class KlonLocals {

  private KlonLocals() {

  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonLocals.class);
    Configurator.setDuplicator(result, KlonLocals.class);
    Configurator.setFormatter(result, KlonLocals.class);
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

}
