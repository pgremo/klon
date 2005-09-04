package klon;

@Prototype(name = "Compiler", parent = "Object")
public final class KlonCompiler {

  private KlonCompiler() {

  }

  public static KlonObject duplicate(KlonObject value) {
    return value;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonCompiler.class);
    Configurator.setDuplicator(result, KlonCompiler.class);
    Configurator.setFormatter(result, KlonCompiler.class);
    Configurator.setComparator(result, KlonCompiler.class);
    return result;
  }

  @ExposedAs("fromString")
  public static KlonObject fromString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    String string = KlonString.evalAsString(receiver, message, 0);
    Message result = new Compiler(receiver).fromString(string);
    return KlonMessage.newMessage(receiver, result);
  }
}
