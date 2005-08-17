package klon;

import java.util.HashMap;
import java.util.Map;

import klon.reflection.ExposedAs;

public class KlonObject {

  protected KlonObject parent;
  protected Map<String, KlonObject> slots = new HashMap<String, KlonObject>();
  protected Object attached;

  public KlonObject() {
    this(null, null);
  }

  public KlonObject(Object attached) {
    this(null, attached);
  }

  public KlonObject(KlonObject parent, Object attached) {
    this.parent = parent;
    this.attached = attached;
  }

  public void configure() throws KlonException {
    Configurator.configure(KlonObject.class, slots);
  }

  public KlonObject clone() {
    return new KlonObject(this);
  }

  public Object getAttached() {
    return attached;
  }

  public void setAttached(Object attached) {
    this.attached = attached;
  }

  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    return this;
  }

  public KlonObject send(Message message) throws KlonException {
    String name = message.getSelector()
      .getSelector();
    KlonObject result = getSlot(name);
    return result.activate(this, message);
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
        if (result && attached != null) {
          result = attached.equals(((KlonObject) obj).attached);
        }
      }
    }
    return result;
  }

  @ExposedAs("clone")
  public static KlonObject clone(KlonObject receiver, Message message)
      throws KlonException {
    return receiver.clone();
  }

  @ExposedAs("send")
  public static KlonObject send(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject subject = message.eval(receiver, 0);
    if (subject instanceof Message) {
      return receiver.send((Message) subject);
    }
    throw new KlonException("invalid argument for send");
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject receiver, Message message)
      throws KlonException {
    return receiver.getSlot(message.evalAsString(receiver, 0));
  }

  @ExposedAs("setSlot")
  public static KlonObject setSlot(KlonObject receiver, Message message)
      throws KlonException {
    String name = message.evalAsString(receiver, 0);
    KlonObject value = message.eval(receiver, 1);
    receiver.setSlot(name, value);
    return value;
  }

  @ExposedAs("updateSlot")
  public static KlonObject updateSlot(KlonObject receiver, Message message)
      throws KlonException {
    String name = message.evalAsString(receiver, 0);
    KlonObject value = message.eval(receiver, 1);
    receiver.updateSlot(name, value);
    return value;
  }

  @ExposedAs("removeSlot")
  public static KlonObject removeSlot(KlonObject receiver, Message message)
      throws KlonException {
    return receiver.removeSlot(message.evalAsString(receiver, 0));
  }

  @ExposedAs("slotNames")
  public static KlonObject slotNames(KlonObject receiver, Message message)
      throws KlonException {
    return receiver.slotNames();
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, Message message)
      throws KlonException {
    return receiver.equals(message.eval(receiver, 0))
        ? receiver
        : Klon.ROOT.getSlot("Nil");
  }

}
