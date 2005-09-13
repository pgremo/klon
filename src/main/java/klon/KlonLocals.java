package klon;

@ExposedAs("Locals")
@Bindings("Object")
public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = 1963856865914651978L;

  public static KlonObject newLocals(KlonObject root, KlonObject self)
      throws KlonObject {
    KlonObject result = root.getSlot("Locals")
      .clone();
    result.setSlot("self", self);
    result.setData(self.getData());
    return result;
  }

  public KlonLocals(KlonState state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonLocals(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  @Override
  public String getType() {
    return "Locals";
  }

}
