package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    data = new ArrayList<KlonObject>();
  }

  @Override
  public String getType() {
    return "List";
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    data = in.readObject();
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(data);
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject clone() {
    KlonObject result = new KlonList(state);
    result.bind(this);
    result.setData(new ArrayList<KlonObject>((List<KlonObject>) data));
    return result;
  }

  @SuppressWarnings({"unused", "unchecked"})
  @Override
  public int compareTo(KlonObject other) {
    int result;
    if ("List".equals(other.getType())) {
      List<KlonObject> l1 = (List<KlonObject>) getData();
      List<KlonObject> l2 = (List<KlonObject>) other.getData();
      result = l1.size() - l2.size();
      for (int i = 0; result == 0 && i < l1.size(); i++) {
        result = l1.get(i)
          .compareTo(l2.get(i));
      }
    } else {
      result = hashCode() - other.hashCode();
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("add")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    for (int i = 0; i < message.getArgumentCount(); i++) {
      ((List) receiver.getData()).add(message.evalArgument(context, i));
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("remove")
  public static KlonObject remove(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    ((List) receiver.getData()).remove(message.evalArgument(context, 0));
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atPut")
  public static KlonObject atPut(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    int index = KlonNumber.evalAsNumber(context, message, 0)
      .intValue();
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    if (!data.isEmpty() && index >= 0 && index < data.size()) {
      data.add(index, message.evalArgument(context, 1));
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("atRemove")
  public static KlonObject atRemove(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    int index = KlonNumber.evalAsNumber(context, message, 0)
      .intValue();
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    if (!data.isEmpty() && index >= 0 && index < data.size()) {
      data.remove(index);
    }
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("at")
  public static KlonObject at(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result;
    message.assertArgumentCount(1);
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
      KlonMessage message) throws KlonObject {
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
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    ((List) receiver.getData()).add(0, message.evalArgument(context, 0));
    return receiver;
  }

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ((List) receiver.getData()).size());
  }

  @SuppressWarnings("unused")
  @ExposedAs("clear")
  public static KlonObject clear(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    ((List) receiver.getData()).clear();
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("reverse")
  public static KlonObject reverse(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    Collections.reverse((List) receiver.getData());
    return receiver;
  }

  @SuppressWarnings({"unused", "unchecked"})
  @ExposedAs("sort")
  public static KlonObject sort(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    Collections.sort((List) receiver.getData());
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("shuffle")
  public static KlonObject shuffle(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    Random random;
    if (message.getArgumentCount() == 0) {
      random = (Random) receiver.getSlot("Random")
        .getData();
    } else {
      random = KlonRandom.evalAsRandom(receiver, message, 0);
    }
    Collections.shuffle((List) receiver.getData(), random);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("random")
  public static KlonObject random(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    Random random;
    if (message.getArgumentCount() == 0) {
      random = (Random) receiver.getSlot("Random")
        .getData();
    } else {
      random = KlonRandom.evalAsRandom(receiver, message, 0);
    }
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    KlonObject result;
    if (data.isEmpty()) {
      result = KlonNil.newNil(receiver);
    } else {
      result = data.get(random.nextInt(data.size()));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject nil = KlonNil.newNil(receiver);
    KlonObject result = nil;
    int arg = 0;
    String index = null;
    if (message.getArgumentCount() == 3) {
      index = (String) message.getArgument(arg++)
        .getSelector()
        .getData();
    }
    String value = (String) message.getArgument(arg++)
      .getSelector()
      .getData();
    KlonMessage code = message.getArgument(arg);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    for (int i = 0; i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      context.setSlot(value, list.get(i));
      result = code.eval(context, context);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("detect")
  public static KlonObject detect(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject nil = KlonNil.newNil(receiver);
    KlonObject result = nil;
    int arg = 0;
    String index = null;
    if (message.getArgumentCount() == 3) {
      index = (String) message.getArgument(arg++)
        .getSelector()
        .getData();
    }
    String value = (String) message.getArgument(arg++)
      .getSelector()
      .getData();
    KlonMessage code = message.getArgument(arg);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    for (int i = 0; nil.equals(result) && i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      context.setSlot(value, list.get(i));
      result = code.eval(context, context);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("collect")
  public static KlonObject collect(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject nil = KlonNil.newNil(receiver);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    List<KlonObject> result = new ArrayList<KlonObject>(list.size());
    int arg = 0;
    String index = null;
    if (message.getArgumentCount() == 3) {
      index = (String) message.getArgument(arg++)
        .getSelector()
        .getData();
    }
    String value = (String) message.getArgument(arg++)
      .getSelector()
      .getData();
    KlonMessage code = message.getArgument(arg);
    for (int i = 0; i < list.size(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      KlonObject current = list.get(i);
      context.setSlot(value, current);
      if (!nil.equals(code.eval(context, context))) {
        result.add(current);
      }
    }
    return newList(receiver, result);
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
      for (KlonObject current : (Iterable<KlonObject>) primitive) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        buffer.append(stringMessage.eval(current, context)
          .getData());
      }
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }

}
