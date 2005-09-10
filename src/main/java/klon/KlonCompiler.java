package klon;

@Bindings("Object")
public class KlonCompiler extends KlonObject {

  private static final long serialVersionUID = 359686882876703383L;

  @Override
  public KlonObject duplicate(KlonObject value) {
    return value;
  }

  public static KlonObject prototype() {
    return new KlonCompiler();
  }

  @Override
  public String getName() {
    return "Compiler";
  }

  @ExposedAs("fromString")
  public static KlonObject fromString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    String string = KlonString.evalAsString(receiver, message, 0);
    Message result = new Compiler(receiver).fromString(string);
    return KlonMessage.newMessage(receiver, result);
  }

}
