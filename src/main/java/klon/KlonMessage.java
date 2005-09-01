package klon;

@Prototype(name = "Message", parent = "Object")
public class KlonMessage {

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonMessage.class);
    Configurator.setDuplicator(result, KlonMessage.class);
    Configurator.setFormatter(result, KlonMessage.class);
    return result;
  }

}
