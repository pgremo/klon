package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage {

  private static final long serialVersionUID = 8916886436753437306L;

  public static KlonObject protoType() {
    return new KlonObject();
  }

}
