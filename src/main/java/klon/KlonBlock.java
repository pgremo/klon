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
    parent = Klon.ROOT.getSlot("Object");
    Configurator.configure(KlonBlock.class, slots);
  }

  @Override
  public KlonObject clone() {
    return new KlonBlock(this, attached);
  }

  @Override
  public String toString() {
    return attached.toString();
  }

  @Override
  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    return ((Block) attached).activate(receiver, message);
  }

}
