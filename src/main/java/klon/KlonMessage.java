package klon;

@Bindings("Object")
public class KlonMessage extends KlonObject {

  private static final long serialVersionUID = 7244365877217781727L;

  public static KlonObject newMessage(KlonObject root, Message message)
      throws KlonObject {
    KlonObject result = root.getSlot("Message").clone();
    result.setData(message);
    return result;
  }

  @Override
  public String getName() {
    return "Message";
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonMessage();
    result.bind(this);
    result.setData(data);
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
