package klon;

@Prototype(name = "List", parent = "Object")
public class KlonList extends KlonObject {

  private static final long serialVersionUID = -6509654061397424250L;

  public KlonList() {
    super();
  }

  public KlonList(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
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
      result = receiver.getSlot("String")
        .duplicate(buffer.toString());
    }
    return result;
  }

}
