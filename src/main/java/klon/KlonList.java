package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExposedAs("List")
@Bindings("Object")
public class KlonList extends KlonObject {

  private static final long serialVersionUID = -4331613935922113899L;

  public static KlonObject newList(KlonObject root, List<KlonObject> value)
      throws KlonObject {
    KlonObject result = root.getSlot("List")
      .clone();
    result.setData(value);
    return result;
  }

  public KlonList() {

  }

  public KlonList(State state) {
    super(state);
    setData(new ArrayList<KlonObject>());
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
    KlonObject result = new KlonList(getState());
    result.bind(this);
    result.setData(new ArrayList<KlonObject>((List<KlonObject>) getData()));
    return result;
  }

  @SuppressWarnings({"unused", "unchecked"})
  @Override
  public int compareTo(KlonObject other) {
    int result;
    if (other instanceof KlonList) {
      List<KlonObject> l1 = (List<KlonObject>) getData();
      List<KlonObject> l2 = (List<KlonObject>) other.getData();
      result = l1.size() - l2.size();
      for (int i = 0; result == 0 && i < l1.size(); i++) {
        result = l1.get(i)
          .compareTo(l2.get(i));
      }
    } else {
      result = super.compareTo(other);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private static void validateIndex(KlonObject receiver, int index,
      KlonObject message) throws KlonObject {
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    if (!data.isEmpty() && index >= 0 && index < data.size()) {
      throw KlonException.newException(receiver, "List.arrayIndexOutOfBounds",
        String.valueOf(index), message);
    }
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("add")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    for (int i = 0; i < KlonMessage.getArgumentCount(message); i++) {
      ((List) receiver.getData()).add(KlonMessage.evalArgument(message,
        context, i));
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("remove")
  public static KlonObject remove(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    ((List) receiver.getData()).remove(KlonMessage.evalArgument(message,
      context, 0));
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atPut")
  public static KlonObject atPut(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    int index = KlonNumber.evalAsNumber(context, message, 0)
      .intValue();
    validateIndex(receiver, index, message);
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    data.add(index, KlonMessage.evalArgument(message, context, 1));
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atRemove")
  public static KlonObject atRemove(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    int index = KlonNumber.evalAsNumber(context, message, 0)
      .intValue();
    validateIndex(receiver, index, message);
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    data.remove(index);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("at")
  public static KlonObject at(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject result;
    int index = KlonNumber.evalAsNumber(context, message, 0)
      .intValue();
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    if (data.isEmpty() || index < 0 || index >= data.size()) {
      result = KlonNil.newNil(receiver);
    } else {
      result = data.get(index);
    }
    return result;
  }

  @SuppressWarnings({"unchecked", "unused"})
  @ExposedAs("pop")
  public static KlonObject pop(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result;
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    if (data.isEmpty()) {
      result = KlonNil.newNil(receiver);
    } else {
      result = data.remove(0);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("push")
  public static KlonObject push(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    ((List) receiver.getData()).add(0, KlonMessage.evalArgument(message,
      context, 0));
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("isEmpty")
  public static KlonObject isEmpty(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return ((Collection) receiver.getData()).isEmpty()
        ? receiver
        : KlonNil.newNil(receiver);
  }

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ((Collection) receiver.getData()).size());
  }

  @SuppressWarnings("unused")
  @ExposedAs("clear")
  public static KlonObject clear(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    ((List) receiver.getData()).clear();
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("reverse")
  public static KlonObject reverse(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    Collections.reverse((List) receiver.getData());
    return receiver;
  }

  @SuppressWarnings({"unused", "unchecked"})
  @ExposedAs("sort")
  public static KlonObject sort(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    Collections.sort((List) receiver.getData());
    return receiver;
  }

  @SuppressWarnings({"unused", "unchecked"})
  @ExposedAs("shuffle")
  public static KlonObject shuffle(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    List data = (List) receiver.getData();
    if (!data.isEmpty()) {
      KlonObject random;
      if (KlonMessage.getArgumentCount(message) == 0) {
        random = receiver.getSlot("Random");
      } else {
        random = KlonMessage.evalArgument(message, receiver, 0);
      }
      KlonObject nextMessage = KlonMessage.newMessageFromString(receiver,
        "next(" + data.size() + ")");
      for (int i = 0; i < data.size(); i++) {
        int index = ((Double) KlonMessage.eval(nextMessage, random, context)
          .getData()).intValue();
        Object tmp = data.get(index);
        data.set(index, data.get(i));
        data.set(i, tmp);
      }
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("random")
  public static KlonObject random(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result;
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    if (data.isEmpty()) {
      result = KlonNil.newNil(receiver);
    } else {
      KlonObject random;
      if (KlonMessage.getArgumentCount(message) == 0) {
        random = receiver.getSlot("Random");
      } else {
        random = KlonMessage.evalArgument(message, receiver, 0);
      }
      KlonObject nextMessage = KlonMessage.newMessageFromString(receiver,
        "next(" + data.size() + ")");
      int index = ((Double) KlonMessage.eval(nextMessage, random, context)
        .getData()).intValue();
      result = data.get(index);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject result = KlonNil.newNil(receiver);
    int arg = 0;
    String index = null;
    if (KlonMessage.getArgumentCount(message) == 3) {
      index = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, arg++))
        .getData();
    }
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, arg++))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, arg);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    for (int i = 0; i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      context.setSlot(value, list.get(i));
      result = KlonMessage.eval(code, context, context);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("detect")
  public static KlonObject detect(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject nil = KlonNil.newNil(receiver);
    KlonObject result = nil;
    int arg = 0;
    String index = null;
    if (KlonMessage.getArgumentCount(message) == 3) {
      index = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, arg++))
        .getData();
    }
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, arg++))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, arg);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    for (int i = 0; nil.equals(result) && i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      context.setSlot(value, list.get(i));
      result = KlonMessage.eval(code, context, context);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("select")
  public static KlonObject select(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject nil = KlonNil.newNil(receiver);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    List<KlonObject> result = new ArrayList<KlonObject>(list.size());
    int arg = 0;
    String index = null;
    if (KlonMessage.getArgumentCount(message) == 3) {
      index = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, arg++))
        .getData();
    }
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, arg++))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, arg);
    for (int i = 0; i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      KlonObject current = list.get(i);
      context.setSlot(value, current);
      if (!nil.equals(KlonMessage.eval(code, context, context))) {
        result.add(current);
      }
    }
    return newList(receiver, result);
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("collect")
  public static KlonObject collect(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    List<KlonObject> result = new ArrayList<KlonObject>(list.size());
    int arg = 0;
    String index = null;
    if (KlonMessage.getArgumentCount(message) == 3) {
      index = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, arg++))
        .getData();
    }
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, arg++))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, arg);
    for (int i = 0; i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      KlonObject current = list.get(i);
      context.setSlot(value, current);
      result.add(KlonMessage.eval(code, context, context));
    }
    return newList(receiver, result);
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result;
    Object primitive = receiver.getData();
    if (primitive == null) {
      result = KlonObject.asString(receiver, context, message);
    } else {
      KlonObject stringMessage = receiver.getState()
        .getAsString();
      StringBuilder buffer = new StringBuilder();
      for (KlonObject current : (Iterable<KlonObject>) primitive) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        buffer.append(KlonMessage.eval(stringMessage, current, context)
          .getData());
      }
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }

}
