package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage extends KlonObject {

  public KlonMessage() {
    super();
  }

  public KlonMessage(KlonObject parent, Object attached) {
    super(parent, attached);
    this.prototype = KlonMessage.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonMessage.class);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonMessage(this, subject);
  }

}
