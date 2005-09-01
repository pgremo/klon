package klon;

@Prototype(name = "Set", parent = "Object")
public final class KlonSet {

  private KlonSet() {

  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonSet.class);
    Configurator.setDuplicator(result, KlonSet.class);
    Configurator.setFormatter(result, KlonSet.class);
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result;
    Object primitive = receiver.getData();
    if (primitive == null) {
      result = KlonObject.asString(receiver, context, message);
    } else {
      StringBuilder buffer = new StringBuilder();
      for (Object current : (Iterable) primitive) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        buffer.append(current.toString());
      }
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }

}
