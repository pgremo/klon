package klon;

import java.util.ArrayList;
import java.util.List;

@Prototype(name = "List", parent = "Object")
public class KlonList extends KlonObject {

  private static final long serialVersionUID = -6509654061397424250L;

  public KlonList() {
    super(null, new ArrayList<KlonObject>());
  }

  public KlonList(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject duplicate() throws KlonObject {
    KlonObject result = super.duplicate();
    result.setData(new ArrayList<KlonObject>((List<KlonObject>) getData()));
    return result;
  }

  public KlonList newList(List<KlonObject> value) throws KlonObject {
    KlonList result = (KlonList) duplicate();
    result.setData(value);
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("add")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    ((List) receiver.getData()).add(message.eval(context, 0));
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("insertAt")
  public static KlonObject insertAt(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    ((List) receiver.getData()).add(
      KlonNumber.evalAsNumber(context, message, 0)
        .intValue(), message.eval(context, 1));
    return receiver;
  }

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber((double) ((List) receiver.getData()).size());
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    KlonObject scope = ((KlonLocals) receiver.getSlot("Locals")).newLocals(receiver);
    String index = (String) message.getArgument(0)
      .getSelector()
      .getData();
    String value = (String) message.getArgument(1)
      .getSelector()
      .getData();
    Message code = message.getArgument(2);
    KlonNumber numberProto = (KlonNumber) receiver.getSlot("Number");
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    for (int i = 0; i < list.size(); i++) {
      scope.setSlot(index, numberProto.newNumber((double) i));
      scope.setSlot(value, list.get(i));
      result = code.eval(scope, scope);
    }
    return result;
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
