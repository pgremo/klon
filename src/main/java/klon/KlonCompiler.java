package klon;

@Bindings("Object")
public class KlonCompiler extends Identity {

  private static final long serialVersionUID = 359686882876703383L;

  @Override
  public KlonObject duplicate(KlonObject value) {
    return value;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonCompiler());
    return result;
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
