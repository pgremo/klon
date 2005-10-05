package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ExposedAs("Map")
@Bindings("Object")
public class KlonMap extends KlonObject {

  private static final long serialVersionUID = 7294688679770243365L;

  public KlonMap() {

  }

  public KlonMap(State state) {
    super(state);
    setType("Map");
    setData(new HashMap<KlonObject, KlonObject>());
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
  @ExposedAs("atPut")
  public static KlonObject atPut(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    KlonObject key = message.evalArgument(context, 0);
    KlonObject value = message.evalArgument(context, 1);
    ((Map) receiver.getData()).put(key, value);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atPutIfAbsent")
  public static KlonObject atPutIfAbsent(KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    KlonObject key = message.evalArgument(context, 0);
    KlonObject value = message.evalArgument(context, 1);
    Map map = (Map) receiver.getData();
    if (!map.containsKey(key)) {
      map.put(key, value);
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("at")
  public static KlonObject at(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject key = message.evalArgument(context, 0);
    KlonObject result = ((Map<KlonObject, KlonObject>) receiver.getData()).get(key);
    if (result == null) {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atRemove")
  public static KlonObject atRemove(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject key = message.evalArgument(context, 0);
    ((Map<KlonObject, KlonObject>) receiver.getData()).remove(key);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("hasKey")
  public static KlonObject hasKey(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject key = message.evalArgument(context, 0);
    KlonObject result;
    if (((Map<KlonObject, KlonObject>) receiver.getData()).containsKey(key)) {
      result = receiver;
    } else {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("hasValue")
  public static KlonObject hasValue(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject value = message.evalArgument(context, 0);
    KlonObject result;
    if (((Map<KlonObject, KlonObject>) receiver.getData()).containsValue(value)) {
      result = receiver;
    } else {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    KlonObject result = KlonNil.newNil(receiver);
    String name = (String) message.getArgument(0)
      .getSelector()
      .getData();
    String value = (String) message.getArgument(1)
      .getSelector()
      .getData();
    KlonMessage code = message.getArgument(2);
    for (Map.Entry<KlonObject, KlonObject> current : ((Map<KlonObject, KlonObject>) receiver.getData()).entrySet()) {
      context.setSlot(name, current.getKey());
      context.setSlot(value, current.getValue());
      result = code.eval(context, context);
    }
    return result;
  }

  @SuppressWarnings("unused")
  @ExposedAs("isEmpty")
  public static KlonObject isEmpty(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return ((Map) receiver.getData()).isEmpty()
        ? receiver
        : KlonNil.newNil(receiver);
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ((Map) receiver.getData()).size());
  }

  @SuppressWarnings({"unchecked", "unused"})
  @ExposedAs("clear")
  public static KlonObject clear(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    ((Map) receiver.getData()).clear();
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("keys")
  public static KlonObject keys(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonList.newList(receiver, new ArrayList(
      ((Map) receiver.getData()).keySet()));
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("values")
  public static KlonObject values(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonList.newList(receiver, new ArrayList(
      ((Map) receiver.getData()).values()));
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result;
    Object primitive = receiver.getData();
    if (primitive == null) {
      result = KlonObject.asString(receiver, context, message);
    } else {
      KlonMessage stringMessage = receiver.getState()
        .getAsString();
      StringBuilder buffer = new StringBuilder();
      for (Map.Entry<KlonObject, KlonObject> current : ((Map<KlonObject, KlonObject>) primitive).entrySet()) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        buffer.append(stringMessage.eval(current.getKey(), context)
          .getData())
          .append("=")
          .append(stringMessage.eval(current.getValue(), context)
            .getData());
      }
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }

}
