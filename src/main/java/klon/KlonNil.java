package klon;

@Prototype(name = "Nil", parent = "Object")
public class KlonNil extends KlonObject<Object> {

  public KlonNil() {
    super();
  }

  public KlonNil(KlonObject parent, Object attached) {
    super(parent, attached);
    this.prototype = KlonNil.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonNil.class);
  }

  @Override
  public KlonObject clone(Object subject) {
    return this;
  }

  @Override
  public String toString() {
    return "Nil";
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String").clone("");
  }

  @ExposedAs("and")
  public static KlonObject and(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver;
  }

  @ExposedAs("or")
  public static KlonObject of(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject other = message.eval(context, 0);
    return receiver.equals(other) ? receiver : other;
  }

  @ExposedAs("ifNil")
  public static KlonObject ifNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return message.eval(context, 0);
  }

  @ExposedAs("isNil")
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Klon");
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.equals(message.eval(receiver, 0)) ? receiver
        .getSlot("Klon") : receiver.getSlot("Nil");
  }

}
