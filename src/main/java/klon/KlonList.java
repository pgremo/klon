package klon;


@Prototype(name = "List", parent = "Object")
public class KlonList extends KlonObject {

  public KlonList() {
    super();
  }

  public KlonList(KlonObject parent, Object attached) {
    super(parent, attached);
    this.prototype = KlonList.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonList.class);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonList(this, subject);
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
