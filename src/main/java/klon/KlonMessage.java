package klon;

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
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonMessage.class, this);
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
