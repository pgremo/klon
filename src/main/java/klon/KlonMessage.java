package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage extends KlonObject<Message> {

  public KlonMessage() throws KlonException {
    this(null);
  }

  public KlonMessage(Message attached) throws KlonException {
    this(Klon.ROOT.getSlot("Message"), attached);
  }

  public KlonMessage(KlonObject parent, Message attached) {
    super(parent, attached);
    this.prototype = KlonMessage.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonMessage.class);
  }

  @Override
  public KlonObject clone() {
    return new KlonMessage(this, primitive);
  }

}
