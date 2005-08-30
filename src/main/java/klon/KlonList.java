package klon;

import java.util.ArrayList;
import java.util.List;

@Prototype(name = "List", parent = "Object")
public class KlonList extends KlonObject {

  private static final long serialVersionUID = -6509654061397424250L;

  public KlonList() {
    super(null, new ArrayList());
  }

  public KlonList(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject duplicate() throws KlonException {
    KlonObject result = super.duplicate();
    result.data = new ArrayList((List) getData());
    return result;
  }

  public KlonList newList(List value) throws KlonException {
    KlonList result = (KlonList) duplicate();
    result.data = value;
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("add")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    ((List) receiver.getData()).add(message.eval(context, 0));
    return receiver;
  }

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber((double) ((List) receiver.getData()).size());
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
      result = ((KlonString) receiver.getSlot("String")).newString(buffer.toString());
    }
    return result;
  }

}
