package klon;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Prototype(name = "Object")
public class KlonObject<T> {

  protected Map<String, KlonObject> slots = new HashMap<String, KlonObject>();
  protected T primitive;

  public KlonObject() {
    this(null, null);
  }

  public KlonObject(T attached) throws KlonException {
    this(Klon.ROOT.getSlot("Object"), attached);
  }

  public KlonObject(KlonObject parent, T attached) {
    slots.put("parent", parent);
    this.primitive = attached;
  }

  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonObject.class);
  }

  public KlonObject clone() {
    return new KlonObject<Object>(this, primitive);
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return this;
  }

  public T getPrimitive() {
    return primitive;
  }

  public void setSlot(String name, KlonObject value) {
    slots.put(name, value);
  }

  private void updateSlot(String name, KlonObject value,
      Collection<KlonObject> searchPath) throws KlonException {
    if (slots.containsKey(name)) {
      slots.put(name, value);
    } else {
      KlonObject parent = slots.get("parent");
      if (parent == null || searchPath.contains(parent)) {
        throw new KlonException(name + " does not exist");
      }
      searchPath.add(parent);
      parent.updateSlot(name, value, searchPath);
    }
  }

  public void updateSlot(String name, KlonObject value) throws KlonException {
    updateSlot(name, value, new HashSet<KlonObject>());
  }

  private KlonObject getSlot(String name, Collection<KlonObject> searchPath)
      throws KlonException {
    KlonObject result = null;
    if (slots.containsKey(name)) {
      result = slots.get(name);
    } else {
      KlonObject parent = slots.get("parent");
      if (parent != null && !searchPath.contains(parent)) {
        searchPath.add(parent);
        result = parent.getSlot(name, searchPath);
      }
    }
    return result;
  }

  public KlonObject getSlot(String name) throws KlonException {
    return getSlot(name, new HashSet<KlonObject>());
  }

  private KlonObject removeSlot(String name, Collection<KlonObject> searchPath)
      throws KlonException {
    KlonObject result = null;
    if (slots.containsKey(name)) {
      result = slots.remove(name);
    } else {
      KlonObject parent = slots.get("parent");
      if (parent != null && !searchPath.contains(parent)) {
        searchPath.add(parent);
        result = parent.removeSlot(name, searchPath);
      }
    }
    return result;
  }

  public void removeSlot(String name) throws KlonException {
    removeSlot(name, new HashSet<KlonObject>());
  }

  public KlonObject slotNames() throws KlonException {
    return new KlonSet(slots.keySet());
  }

  public KlonObject perform(KlonObject context, Message message)
      throws KlonException {
    String name = message.getSelector().getPrimitive();
    KlonObject slot = getSlot(name);
    if (slot == null) {
      slot = context.getSlot(name);
    }
    if (slot == null) {
      throw new KlonException(name + " does not exist");
    }
    return slot.activate(this, context, message);
  }

  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (this == obj) {
      result = true;
    } else {
      if (obj instanceof KlonObject) {
        if (slots.equals(((KlonObject) obj).slots)) {
          result = true;
        }
        if (result && primitive != null) {
          result = primitive.equals(((KlonObject) obj).primitive);
        }
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return primitive == null ? getClass().getSimpleName() + "@"
        + Integer.toHexString(hashCode()) : String.valueOf(primitive);
  }

  @SuppressWarnings("unused")
  @ExposedAs("clone")
  public static KlonObject clone(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.clone();
  }

  @ExposedAs("send")
  public static KlonObject send(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject subject = message.eval(context, 0);
    if (subject instanceof KlonMessage) {
      return receiver.perform(context, (Message) subject.getPrimitive());
    }
    throw new KlonException("invalid argument for send");
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    String name = message.evalAsString(context, 0);
    KlonObject result = receiver.getSlot(name);
    if (result == null) {
      throw new KlonException(name + " does not exist");
    }
    return result;
  }

  @ExposedAs("setSlot")
  public static KlonObject setSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    String name = message.evalAsString(context, 0);
    KlonObject value = message.eval(context, 1);
    receiver.setSlot(name, value);
    return value;
  }

  @ExposedAs("updateSlot")
  public static KlonObject updateSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    String name = message.evalAsString(context, 0);
    KlonObject value = message.eval(context, 1);
    receiver.updateSlot(name, value);
    return value;
  }

  @ExposedAs("removeSlot")
  public static KlonObject removeSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    receiver.removeSlot(message.evalAsString(context, 0));
    return receiver;
  }

  @ExposedAs("foreach")
  public static KlonObject foreach(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = receiver.getSlot("Nil");
    KlonObject scope = context.clone();
    String name = message.getArgument(0).getSelector().getPrimitive();
    String value = message.getArgument(1).getSelector().getPrimitive();
    Message code = message.getArgument(2);
    for (Object item : receiver.slots.entrySet()) {
      Map.Entry<String, KlonObject> current = (Map.Entry<String, KlonObject>) item;
      scope.setSlot(name, new KlonString(current.getKey()));
      scope.setSlot(value, current.getValue());
      result = code.eval(scope, scope);
    }
    return result;
  }

  @ExposedAs("slotNames")
  public static KlonObject slotNames(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.slotNames();
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return new KlonString(receiver.getClass().getSimpleName() + "@"
        + Integer.toHexString(receiver.hashCode()));
  }

  @ExposedAs("write")
  public static KlonObject write(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    Message printMessage = new Compiler().fromString("asString");
    for (int i = 0; i < message.getArgumentCount(); i++) {
      KlonObject target = message.eval(context, i);
      System.out.print(target.perform(context, printMessage));
    }
    return receiver;
  }

  @ExposedAs("writeLine")
  public static KlonObject writeLine(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    write(receiver, context, message);
    System.out.println();
    return receiver;
  }

  @ExposedAs("exit")
  public static KlonObject exit(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    int result = 0;
    if (message.getArgumentCount() == 1) {
      result = message.evalAsNumber(context, 0).intValue();
    }
    System.exit(result);
    return receiver.getSlot("Nil");
  }

  @ExposedAs("block")
  public static KlonObject block(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    int count = message.getArgumentCount() - 1;
    String[] parameters = new String[count];
    for (int i = 0; i < count; i++) {
      KlonObject current = message.getArgument(i).getSelector();
      if (current == null) {
        throw new KlonException(current + " must be a Symbol");
      }
      parameters[i] = (String) current.getPrimitive();
    }
    return new KlonBlock(new Block(parameters, message.getArgument(count)));
  }

  @ExposedAs("for")
  public static KlonObject forLoop(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = receiver.getSlot("Nil");
    KlonObject scope = context.clone();
    String counter = message.getArgument(0).getSelector().getPrimitive();
    int start = message.evalAsNumber(context, 1).intValue();
    int end = message.evalAsNumber(context, 2).intValue();
    int increment;
    Message code;
    if (message.getArgumentCount() == 5) {
      increment = message.evalAsNumber(context, 3).intValue();
      code = message.getArgument(4);
    } else {
      increment = end - start < 0 ? -1 : 1;
      code = message.getArgument(3);
    }
    int i = start;
    while (!(increment > 0 ? i > end : i < end)) {
      scope.setSlot(counter, new KlonNumber((double) i));
      result = code.eval(scope, scope);
      i += increment;
    }
    return result;
  }

  @ExposedAs("while")
  public static KlonObject whileLoop(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    KlonObject result = nil;
    Message condition = message.getArgument(0);
    Message code = message.getArgument(1);
    while (!nil.equals(condition.eval(context, context))) {
      result = code.eval(context, context);
    }
    return result;
  }

  @ExposedAs("if")
  public static KlonObject ifBranch(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = message.eval(context, 0);
    if (message.getArgumentCount() > 1) {
      if (!receiver.getSlot("Nil").equals(result)) {
        result = message.eval(context, 1);
      } else if (message.getArgumentCount() == 3) {
        result = message.eval(context, 2);
      }
    }
    return result;
  }

  @ExposedAs("and")
  public static KlonObject and(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    return nil.equals(message.eval(context, 0)) ? nil : receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("or")
  public static KlonObject or(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Nil");
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return message.eval(context, 0);
  }

  @SuppressWarnings("unused")
  @ExposedAs("ifNil")
  public static KlonObject ifNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver;
  }

  @ExposedAs("isNil")
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Nil");
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.equals(message.eval(context, 0)) ? receiver : receiver
        .getSlot("Nil");
  }

  @ExposedAs("?")
  public static KlonObject condition(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = receiver.getSlot("Nil");
    Message target = message.getArgument(0);
    if (receiver.getSlot(target.getSelector().getPrimitive()) != null) {
      result = receiver.perform(context, target);
    }
    return result;
  }
}
