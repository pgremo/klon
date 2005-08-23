package klon;


@Prototype(name = "Set", parent = "Object")
public class KlonSet extends KlonObject {

  public KlonSet() {
    super();
  }

  public KlonSet(KlonObject parent, Object attached) {
    super(parent, attached);
    this.prototype = KlonSet.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonSet.class);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonSet(this, subject);
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    StringBuilder result = new StringBuilder();
    for (Object current : (Iterable) receiver.getPrimitive()) {
      if (result.length() > 0) {
        result.append(", ");
      }
      result.append(current.toString());
    }
    return receiver.getSlot("String").clone(result.toString());
  }

}
