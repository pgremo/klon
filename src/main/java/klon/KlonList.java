package klon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Prototype(name = "List", parent = "Object")
public final class KlonList {

  private KlonList() {

  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setData(new ArrayList<KlonObject>());
    Configurator.setActivator(result, KlonList.class);
    Configurator.setDuplicator(result, KlonList.class);
    Configurator.setFormatter(result, KlonList.class);
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject duplicate(KlonObject value) throws KlonObject {
    KlonObject result = KlonObject.duplicate(value);
    result
        .setData(new ArrayList<KlonObject>((List<KlonObject>) value.getData()));
    return result;
  }

  public static KlonObject newList(KlonObject root, List<KlonObject> value)
      throws KlonObject {
    KlonObject result = root.getSlot("List").duplicate();
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
    ((List) receiver.getData()).add(KlonNumber
        .evalAsNumber(context, message, 0)
          .intValue(), message.eval(context, 1));
    return receiver;
  }

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (double) ((List) receiver.getData())
        .size());
  }

  @SuppressWarnings("unused")
  @ExposedAs("shuffle")
  public static KlonObject shuffle(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Random random;
    if (message.getArgumentCount() == 0) {
      random = (Random) receiver.getSlot("Random").getData();
    } else {
      random = KlonRandom.evalAsRandom(receiver, message, 0);
    }
    Collections.shuffle((List) receiver.getData(), random);
    return receiver;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("random")
  public static KlonObject random(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Random random;
    if (message.getArgumentCount() == 0) {
      random = (Random) receiver.getSlot("Random").getData();
    } else {
      random = KlonRandom.evalAsRandom(receiver, message, 0);
    }
    List<KlonObject> data = (List<KlonObject>) receiver.getData();
    KlonObject result;
    if (data.isEmpty()) {
      result = receiver.getSlot("Nil");
    } else {
      result = data.get(random.nextInt(data.size()));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    String index = (String) message.getArgument(0).getSelector().getData();
    String value = (String) message.getArgument(1).getSelector().getData();
    Message code = message.getArgument(2);
    List<KlonObject> list = (List<KlonObject>) receiver.getData();
    for (int i = 0; i < list.size(); i++) {
      context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      context.setSlot(value, list.get(i));
      result = code.eval(context, context);
    }
    return result;
  }

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
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }

}
