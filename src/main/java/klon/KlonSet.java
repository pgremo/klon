package klon;

@Prototype(name = "Set", parent = "Object")
public class KlonSet extends KlonObject {

  public KlonSet() {
    super();
  }

  public KlonSet(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonSet(this, subject);
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result;
    Object primitive = receiver.getPrimitive();
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
      result = receiver.getSlot("String")
        .clone(buffer.toString());
    }
    return result;
  }

}
