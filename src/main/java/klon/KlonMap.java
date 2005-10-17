package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KlonMap extends KlonObject {

  private static final long serialVersionUID = 7294688679770243365L;

  public KlonMap() {

  }

  public KlonMap(State state) {
    super(state);
    setData(new HashMap<KlonObject, KlonObject>());
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("asString", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("asString", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("at", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("at", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("atPut", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("atPut", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("atPutIfAbsent", KlonNativeMethod.newNativeMethod(root,
        KlonMap.class.getMethod("atPutIfAbsent",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("atRemove", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("atRemove", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("clear", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("clear", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("forEach", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("forEach", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("hasKey", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("hasKey", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("hasValue", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("hasValue", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("isEmpty", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("isEmpty", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("keys", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("keys", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("size", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("size", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("values", KlonNativeMethod.newNativeMethod(root, KlonMap.class
        .getMethod("values", KlonNativeMethod.PARAMETER_TYPES)));
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    setData(in.readObject());
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(getData());
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject clone() {
    KlonObject result = new KlonMap(getState());
    result.bind(this);
    result.setData(new HashMap<KlonObject, KlonObject>(
        (Map<KlonObject, KlonObject>) getData()));
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject atPut(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject key = KlonMessage.evalArgument(message, context, 0);
    KlonObject value = KlonMessage.evalArgument(message, context, 1);
    ((Map) receiver.getData()).put(key, value);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject atPutIfAbsent(KlonObject receiver,
      KlonObject context, KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject key = KlonMessage.evalArgument(message, context, 0);
    KlonObject value = KlonMessage.evalArgument(message, context, 1);
    Map map = (Map) receiver.getData();
    if (!map.containsKey(key)) {
      map.put(key, value);
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject at(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject key = KlonMessage.evalArgument(message, context, 0);
    KlonObject result = ((Map<KlonObject, KlonObject>) receiver.getData())
        .get(key);
    if (result == null) {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject atRemove(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject key = KlonMessage.evalArgument(message, context, 0);
    ((Map<KlonObject, KlonObject>) receiver.getData()).remove(key);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject hasKey(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject key = KlonMessage.evalArgument(message, context, 0);
    KlonObject result;
    if (((Map<KlonObject, KlonObject>) receiver.getData()).containsKey(key)) {
      result = receiver;
    } else {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject hasValue(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject value = KlonMessage.evalArgument(message, context, 0);
    KlonObject result;
    if (((Map<KlonObject, KlonObject>) receiver.getData()).containsValue(value)) {
      result = receiver;
    } else {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject result = KlonNil.newNil(receiver);
    String name = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, 0)).getData();
    String value = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, 1)).getData();
    KlonObject code = KlonMessage.getArgument(message, 2);
    for (Map.Entry<KlonObject, KlonObject> current : ((Map<KlonObject, KlonObject>) receiver
        .getData()).entrySet()) {
      context.setSlot(name, current.getKey());
      context.setSlot(value, current.getValue());
      result = KlonMessage.eval(code, context, context);
    }
    return result;
  }

  @SuppressWarnings("unused")
  public static KlonObject isEmpty(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return ((Map) receiver.getData()).isEmpty() ? receiver : KlonNil
        .newNil(receiver);
  }

  @SuppressWarnings("unchecked")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (double) ((Map) receiver.getData())
        .size());
  }

  @SuppressWarnings( { "unchecked", "unused" })
  public static KlonObject clear(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    ((Map) receiver.getData()).clear();
    return receiver;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject keys(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonList.newList(receiver, new ArrayList(((Map) receiver.getData())
        .keySet()));
  }

  @SuppressWarnings("unchecked")
  public static KlonObject values(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonList.newList(receiver, new ArrayList(((Map) receiver.getData())
        .values()));
  }

  @SuppressWarnings("unchecked")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result;
    Object primitive = receiver.getData();
    if (primitive == null) {
      result = KlonObject.asString(receiver, context, message);
    } else {
      KlonObject stringMessage = receiver.getState().getAsString();
      StringBuilder buffer = new StringBuilder();
      for (Map.Entry<KlonObject, KlonObject> current : ((Map<KlonObject, KlonObject>) primitive)
          .entrySet()) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        buffer.append(
            KlonMessage
                .eval(stringMessage, current.getKey(), context)
                  .getData()).append("=").append(
            KlonMessage
                .eval(stringMessage, current.getValue(), context)
                  .getData());
      }
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }

}
