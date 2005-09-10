package klon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Bindings("Object")
public class KlonMap extends KlonObject {

  private static final long serialVersionUID = 7294688679770243365L;

  public static KlonObject prototype() {
    KlonObject result = new KlonMap();
    result.setData(new HashMap<KlonObject, KlonObject>());
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject duplicate(KlonObject value) throws KlonObject {
    KlonObject result = new KlonMap();
    result.bind(value);
    result.setData(new HashMap<KlonObject, KlonObject>(
        (Map<KlonObject, KlonObject>) value.getData()));
    return result;
  }

  @Override
  public String getName() {
    return "Map";
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atPut")
  public static KlonObject atPut(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject key = message.eval(context, 0);
    KlonObject value = message.eval(context, 1);
    ((Map) receiver.getData()).put(key, value);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atPutIfAbsent")
  public static KlonObject atPutIfAbsent(KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    KlonObject key = message.eval(context, 0);
    KlonObject value = message.eval(context, 1);
    Map map = (Map) receiver.getData();
    if (!map.containsKey(key)) {
      map.put(key, value);
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("at")
  public static KlonObject at(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject key = message.eval(context, 0);
    KlonObject result = ((Map<KlonObject, KlonObject>) receiver.getData())
        .get(key);
    if (result == null) {
      result = receiver.getSlot("Nil");
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atRemove")
  public static KlonObject atRemove(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject key = message.eval(context, 0);
    ((Map<KlonObject, KlonObject>) receiver.getData()).remove(key);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("hasKey")
  public static KlonObject hasKey(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject key = message.eval(context, 0);
    KlonObject result;
    if (((Map<KlonObject, KlonObject>) receiver.getData()).containsKey(key)) {
      result = receiver;
    } else {
      result = receiver.getSlot("Nil");
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("hasValue")
  public static KlonObject hasValue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject value = message.eval(context, 0);
    KlonObject result;
    if (((Map<KlonObject, KlonObject>) receiver.getData()).containsValue(value)) {
      result = receiver;
    } else {
      result = receiver.getSlot("Nil");
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    String name = (String) message.getArgument(0).getSelector().getData();
    String value = (String) message.getArgument(1).getSelector().getData();
    Message code = message.getArgument(2);
    for (Map.Entry<KlonObject, KlonObject> current : ((Map<KlonObject, KlonObject>) receiver
        .getData()).entrySet()) {
      context.setSlot(name, current.getKey());
      context.setSlot(value, current.getValue());
      result = code.eval(context, context);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (double) ((Map) receiver.getData())
        .size());
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("keys")
  public static KlonObject keys(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonList.newList(receiver, new ArrayList(((Map) receiver.getData())
        .keySet()));
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("values")
  public static KlonObject values(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonList.newList(receiver, new ArrayList(((Map) receiver.getData())
        .values()));
  }

}
