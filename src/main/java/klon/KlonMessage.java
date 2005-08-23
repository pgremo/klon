package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage extends KlonObject<Message> {

  public KlonMessage() {
    super();
  }

  public KlonMessage(Message attached) throws KlonException {
    super(attached);
  }

  public KlonMessage(KlonObject parent, Message attached) {
    super(parent, attached);
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
