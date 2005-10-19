package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("add", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("add", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asString", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("asString", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("at", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("at", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("atPut", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("atPut", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("atRemove", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("atRemove", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("clear", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("clear", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("collect", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("collect", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("detect", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("detect", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("forEach", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("forEach", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("isEmpty", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("isEmpty", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("pop", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("pop", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("push", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("push", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("random", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("random", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("remove", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("remove", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("reverse", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("reverse", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("select", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("select", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("shuffle", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("shuffle", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("size", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("size", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("sort", KlonNativeMethod.newNativeMethod(root,
      KlonList.class.getMethod("sort", KlonNativeMethod.PARAMETER_TYPES)));
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
  public static KlonObject remove(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    ((List) receiver.getData()).remove(KlonMessage.evalArgument(message,
      context, 0));
    return receiver;
  }

  @SuppressWarnings("unchecked")
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
  public static KlonObject push(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    ((List) receiver.getData()).add(0, KlonMessage.evalArgument(message,
      context, 0));
    return receiver;
  }

  @SuppressWarnings("unused")
  public static KlonObject isEmpty(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return ((Collection) receiver.getData()).isEmpty()
        ? receiver
        : KlonNil.newNil(receiver);
  }

  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ((Collection) receiver.getData()).size());
  }

  @SuppressWarnings("unused")
  public static KlonObject clear(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    ((List) receiver.getData()).clear();
    return receiver;
  }

  @SuppressWarnings("unused")
  public static KlonObject reverse(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    Collections.reverse((List) receiver.getData());
    return receiver;
  }

  @SuppressWarnings({"unused", "unchecked"})
  public static KlonObject sort(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    Collections.sort((List) receiver.getData());
    return receiver;
  }

  @SuppressWarnings({"unused", "unchecked"})
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

  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return forEach(receiver, context, message, new ForEach(
      KlonNil.newNil(receiver)));
  }

  public static KlonObject detect(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return forEach(receiver, context, message, new Detect(
      KlonNil.newNil(receiver)));
  }

  public static KlonObject select(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return forEach(receiver, context, message, new Select(newList(receiver,
      new ArrayList<KlonObject>())));
  }

  public static KlonObject collect(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return forEach(receiver, context, message, new Collect(newList(receiver,
      new ArrayList<KlonObject>())));
  }

  @SuppressWarnings("unchecked")
  private static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message, Closure closure) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    int arg = KlonMessage.getArgumentCount(message);
    closure.setCode(KlonMessage.getArgument(message, --arg));
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, --arg))
      .getData();
    String index = null;
    if (arg > 0) {
      index = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, --arg))
        .getData();
    }
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    boolean done = false;
    for (int i = 0; !done && i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      KlonObject current = list.get(i);
      context.setSlot(value, current);
      done = closure.invoke(current, context);
    }
    return closure.result();
  }

  @SuppressWarnings("unchecked")
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
