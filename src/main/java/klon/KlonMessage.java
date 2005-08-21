package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage extends KlonObject<Message> {

  public KlonMessage() {
    super();
  }

  public KlonMessage(Message attached) {
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

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    System.out.print(receiver.getPrimitive());
    return receiver.getSlot("Nil");
  }

}
