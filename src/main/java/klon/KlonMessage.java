package klon;

@Prototype(name = "Message", parent = "Object")
public final class KlonMessage {

  private KlonMessage() {

  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonMessage.class);
    Configurator.setDuplicator(result, KlonMessage.class);
    Configurator.setFormatter(result, KlonMessage.class);
    return result;
  }

  public static KlonObject newMessage(KlonObject root, Message message)
      throws KlonObject {
    KlonObject result = root.getSlot("Message")
      .duplicate();
    result.setData(message);
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
