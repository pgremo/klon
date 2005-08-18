package klon;

public class KlonBlock extends KlonObject {

  public KlonBlock() {
    super();
  }

  public KlonBlock(Object value) throws KlonException {
    super(Klon.ROOT.getSlot("Block"), value);
  }

  public KlonBlock(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonBlock.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonBlock(this, primitive);
  }

  @Override
  public String toString() {
    return String.valueOf(primitive);
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context, Message message)
      throws KlonException {
    return ((Block) primitive).activate(receiver, context, message);
  }

}
