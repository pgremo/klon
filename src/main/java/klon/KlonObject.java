package klon;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import klon.reflection.ExposedAs;

public class KlonObject<T> {

  protected Map<String, KlonObject> slots = new HashMap<String, KlonObject>();
  protected T primitive;

  public KlonObject() {
    this(null, null);
  }

  public KlonObject(T attached) {
    this(null, attached);
  }

  public KlonObject(KlonObject parent, T attached) {
    slots.put("parent", parent);
    this.primitive = attached;
  }

  public void configure() throws KlonException {
    Configurator.configure(KlonObject.class, this);
  }

  public KlonObject clone() {
    return new KlonObject<Object>(this, primitive);
  }

  public Object getPrimitive() {
    return primitive;
  }

  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return this;
  }

  public void setSlot(String name, KlonObject value) {
    slots.put(name, value);
  }

  private KlonObject updateSlot(String name, KlonObject value,
      Collection<KlonObject> searchPath) throws KlonException {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.put(name, value);
    } else {
      KlonObject parent = slots.get("parent");
      if (parent == null || searchPath.contains(parent)) {
        throw new KlonException(name + " does not exist");
      }
      searchPath.add(parent);
      result = parent.updateSlot(name, value, searchPath);
    }
    return result;
  }

  public KlonObject updateSlot(String name, KlonObject value)
      throws KlonException {
    return updateSlot(name, value, new HashSet<KlonObject>());
  }

  private KlonObject getSlot(String name, Collection<KlonObject> searchPath)
      throws KlonException {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.get(name);
    } else {
      KlonObject parent = slots.get("parent");
      if (parent == null || searchPath.contains(parent)) {
        throw new KlonException(name + " does not exist");
      }
      searchPath.add(parent);
      result = parent.getSlot(name, searchPath);
    }
    return result;
  }

  public KlonObject getSlot(String name) throws KlonException {
    return getSlot(name, new HashSet<KlonObject>());
  }

  private KlonObject removeSlot(String name, Collection<KlonObject> searchPath)
      throws KlonException {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.remove(name);
    } else {
      KlonObject parent = slots.get("parent");
      if (parent == null || searchPath.contains(parent)) {
        throw new KlonException(name + " does not exist");
      }
      searchPath.add(parent);
      result = parent.removeSlot(name, searchPath);
    }
    return result;
  }

  public KlonObject removeSlot(String name) throws KlonException {
    return removeSlot(name, new HashSet<KlonObject>());
  }

  public KlonObject slotNames() {
    return new Set(slots.keySet());
  }

  public KlonObject perform(KlonObject context, Message message)
      throws KlonException {
    KlonObject result;
    String name = (String) message.getSelector().getPrimitive();
    try {
      KlonObject slot = getSlot(name);
      result = slot.activate(this, context, message);
    } catch (KlonException e) {
      KlonObject slot = context.getSlot(name);
      result = slot.activate(this, context, message);
    }
    return result;
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

  @ExposedAs("clone")
  public static KlonObject clone(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.clone();
  }

  @ExposedAs("send")
  public static KlonObject send(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject subject = message.eval(context, 0);
    if (subject instanceof Message) {
      return receiver.perform(context, message);
    }
    throw new KlonException("invalid argument for send");
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot(message.evalAsString(context, 0));
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
    return receiver.removeSlot(message.evalAsString(context, 0));
  }

  @ExposedAs("slotNames")
  public static KlonObject slotNames(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.slotNames();
  }

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    System.out.println(receiver);
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
    String counter = (String) message
        .getArgument(0)
          .getSelector()
          .getPrimitive();
    int start = message.evalAsNumber(context, 1).intValue();
    int end = message.evalAsNumber(context, 2).intValue();
    int increment;
    Message code;
    if (message.getArgumentCount() == 5) {
      increment = message.evalAsNumber(context, 3).intValue();
      code = message.getArgument(4);
    } else {
      increment = (int) Math.signum(end - start);
      code = message.getArgument(3);
    }
    boolean done = start == end;
    int i = start;
    while (!done) {
      scope.setSlot(counter, new KlonNumber((double) i));
      result = code.eval(scope, scope);
      i += increment;
      done = increment > 0 ? i >= end : i <= end;
    }
    return result;
  }

  @ExposedAs("while")
  public static KlonObject whileLoop(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    KlonObject result = nil;
    KlonObject scope = context.clone();
    Message condition = message.getArgument(0);
    Message code = message.getArgument(1);
    while (!nil.equals(condition.eval(scope, scope))) {
      result = code.eval(scope, scope);
    }
    return result;
  }

  @ExposedAs("if")
  public static KlonObject ifBranch(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    KlonObject result = nil;
    KlonObject scope = context.clone();
    if (!nil.equals(message.eval(scope, 0))) {
      result = message.eval(scope, 1);
    }
    return result;
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
    try {
      if (context.getSlot((String) target.getSelector().getPrimitive()) != null) {
        result = target.eval(context, context);
      }
    } catch (KlonException e) {

    }
    return result;
  }
}
