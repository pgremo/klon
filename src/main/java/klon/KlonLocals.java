package klon;

@Prototype(name = "Locals", parent = "Object")
public class KlonLocals {

  private static final long serialVersionUID = -7385932620894631782L;

  public static KlonObject protoType() {
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
    return result;
  }

}
