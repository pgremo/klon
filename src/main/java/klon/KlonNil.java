package klon;

public class KlonNil extends KlonObject<Object> {

  public KlonNil() {
    super();
  }

  public KlonNil(Object attached) throws KlonException {
    super(Klon.ROOT.getSlot("Nil"), attached);
  }

  public KlonNil(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonNil.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonNil(this, primitive);
  }

  @Override
  public String toString() {
    return "";
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.equals(message.eval(receiver, 0)) ? receiver
        .getSlot("Klon") : receiver.getSlot("Nil");
  }

}
