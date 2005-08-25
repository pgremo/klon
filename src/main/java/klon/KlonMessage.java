package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage extends KlonObject {

  private static final long serialVersionUID = 8916886436753437306L;

  public KlonMessage() {
    super();
  }

  public KlonMessage(KlonObject parent, Object attached) {
    super(parent, attached);
  }

}
