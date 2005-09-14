package klon;

@ExposedAs("Locals")
@Bindings("Object")
public class KlonLocals extends KlonObject {

  private static final long serialVersionUID = 1963856865914651978L;

  public KlonLocals(State state) {
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
