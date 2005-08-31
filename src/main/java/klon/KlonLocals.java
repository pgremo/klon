package klon;

@Prototype(name = "Locals", parent = "Object")
public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = -7385932620894631782L;

  public KlonLocals() {
    super();
  }

  public KlonLocals(KlonObject parent, Object data) {
    super(parent, data);
  }

  public KlonLocals newLocals(KlonObject self) throws KlonObject {
    KlonLocals result = (KlonLocals) duplicate();
    result.setSlot("self", self);
    return result;
  }

}
