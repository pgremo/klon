package klon;

@Prototype(name = "Locals", parent = "Object")
public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = -7385932620894631782L;

  public KlonObject newLocals(KlonObject self) throws KlonObject {
    KlonObject result = duplicate();
    result.setSlot("self", self);
    return result;
  }

}
