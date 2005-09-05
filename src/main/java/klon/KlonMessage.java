package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage extends Identity {

  private static final long serialVersionUID = 7244365877217781727L;

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonMessage());
    return result;
  }

  public static KlonObject newMessage(KlonObject root, Message message)
      throws KlonObject {
    KlonObject result = root.getSlot("Message").duplicate();
    result.setData(message);
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
