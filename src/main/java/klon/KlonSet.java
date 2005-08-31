package klon;

@Prototype(name = "Set", parent = "Object")
public class KlonSet extends KlonObject {

  private static final long serialVersionUID = 9142527726733948367L;

  public KlonSet() {
    super();
  }

  public KlonSet(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
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
      result = ((KlonString) receiver.getSlot("String")).newString(buffer.toString());
    }
    return result;
  }

}
