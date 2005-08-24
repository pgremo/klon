package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage extends KlonObject {

  public KlonMessage() {
    super();
  }

  public KlonMessage(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonMessage(this, subject);
  }

}
