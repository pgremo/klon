package klon;

import java.util.HashMap;
import java.util.Map;

import klon.reflection.ExposedAs;

public class KlonObject {

  protected KlonObject parent;
  protected Map<String, KlonObject> slots = new HashMap<String, KlonObject>();
  protected Object primitive;

  public KlonObject() {
    this(null, null);
  }

  public KlonObject(Object attached) {
    this(null, attached);
  }

  public KlonObject(KlonObject parent, Object attached) {
    this.parent = parent;
    this.primitive = attached;
  }

  public void configure() throws KlonException {
    Configurator.configure(KlonObject.class, slots);
  }

  public KlonObject clone() {
    return new KlonObject(this, primitive);
  }

  public Object getPrimitive() {
    return primitive;
  }

  public void setPrimitive(Object primitive) {
    this.primitive = primitive;
  }

  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return this;
  }

  public void setSlot(String name, KlonObject value) {
    slots.put(name, value);
  }

  public KlonObject updateSlot(String name, KlonObject value)
      throws KlonException {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.put(name, value);
    } else {
      if (parent == null) {
        throw new KlonException(name + " does not exist");
      }
      result = parent.updateSlot(name, value);
    }
    return result;
  }

  public KlonObject getSlot(String name) throws KlonException {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.get(name);
    } else {
      if (parent == null) {
        throw new KlonException(name + " does not exist");
      }
      result = parent.getSlot(name);
    }
    return result;
  }

  public KlonObject removeSlot(String name) throws KlonException {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.remove(name);
    } else {
      if (parent == null) {
        throw new KlonException(name + " does not exist");
      }
      result = parent.removeSlot(name);
    }
    return result;
  }

  public KlonObject slotNames() {
    return new Set(slots.keySet());
  }

  public KlonObject perform(KlonObject context, Message message)
      throws KlonException {
    String name = (String) message.getSelector().getPrimitive();
    try {
      KlonObject result = getSlot(name);
      return result.activate(this, context, message);
    } catch (KlonException e) {
      KlonObject result = context.getSlot(name);
      return result.activate(this, context, message);
    }
  }

  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (this == obj) {
      result = true;
    } else {
      if (obj instanceof KlonObject) {
        if (parent != null && parent.equals(((KlonObject) obj).parent)) {
          result = true;
        }
        if (result && slots.equals(((KlonObject) obj).slots)) {
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
    int count = message.getArgumentCount();
    String[] parameters = new String[count - 1];
    for (int i = 0; i < count - 1; i++) {
      KlonObject current = message.getArgument(i).getSelector();
      if (current == null) {
        throw new KlonException(current + " must be a Symbol");
      }
      parameters[i] = (String) current.getPrimitive();
    }
    return new KlonBlock(new Block(parameters, message.getArgument(count - 1)));
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.equals(message.eval(context, 0)) ? receiver : receiver
        .getSlot("Nil");
  }

}
