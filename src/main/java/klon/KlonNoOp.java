package klon;

@Prototype(name = "NoOp")
public class KlonNoOp {

  private static final long serialVersionUID = -3849427822460733188L;

  public static KlonObject newNoOp(KlonObject root, Object value)
      throws KlonObject {
    KlonObject result = root.getSlot("NoOp")
      .duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonNoOp.class);
    Configurator.setDuplicator(result, KlonNoOp.class);
    Configurator.setFormatter(result, KlonNoOp.class);
    return result;
  }

  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = (KlonObject) receiver.getData();
    if (result == null) {
      result = context.getSlot("Nil");
    }
    return result;
  }

}
