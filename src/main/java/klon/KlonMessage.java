package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage {

  private static final long serialVersionUID = 8916886436753437306L;

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonMessage.class);
    Configurator.setDuplicator(result, KlonMessage.class);
    Configurator.setFormatter(result, KlonMessage.class);
    return result;
  }

}
