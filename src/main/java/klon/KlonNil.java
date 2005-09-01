package klon;

@Prototype(name = "Nil", parent = "Object")
public class KlonNil {

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonNil.class);
    Configurator.setDuplicator(result, KlonNil.class);
    Configurator.setFormatter(result, KlonNil.class);
    return result;
  }

  public static KlonObject duplicate(KlonObject value) {
    return value;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, "");
  }

  @SuppressWarnings("unused")
  @ExposedAs("and")
  public static KlonObject and(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }

  @ExposedAs("or")
  public static KlonObject or(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject other = message.eval(context, 0);
    return receiver.getSlot("Nil")
      .equals(other) ? receiver : other;
  }

  @ExposedAs("ifNil")
  public static KlonObject ifNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return message.eval(context, 0);
  }

  @ExposedAs("isNil")
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.getSlot("Klon");
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.equals(message.eval(receiver, 0))
        ? receiver.getSlot("Klon")
        : receiver.getSlot("Nil");
  }

}
