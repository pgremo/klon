package klon;

import java.util.HashMap;
import java.util.Map;

import klon.reflection.ExposedAs;

public class KlonObject {

  protected KlonObject parent;
  protected Map<String, KlonObject> slots = new HashMap<String, KlonObject>();

  public KlonObject() {

  }

  public KlonObject(KlonObject parent) {
    this.parent = parent;
  }

  public KlonObject activate(KlonObject receiver, KlonMessage message)
      throws KlonException {
    return this;
  }

  public KlonObject clone() {
    return new KlonObject(this);
  }

  public KlonObject send(KlonMessage message) throws KlonException {
    String name = message.getSelector()
      .getSelector();
    KlonObject result = getSlot(name);
    return result.activate(this, message);
  }

  public void setSlot(String name, KlonObject value) {
    slots.put(name, value);
  }

  public KlonObject updateSlot(String name, KlonObject value)
      throws MessageNotUnderstood {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.put(name, value);
    } else {
      if (parent == null) {
        throw new MessageNotUnderstood(name + " does not exist");
      }
      result = parent.updateSlot(name, value);
    }
    return result;
  }

  public KlonObject getSlot(String name) throws MessageNotUnderstood {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.get(name);
    } else {
      if (parent == null) {
        throw new MessageNotUnderstood(name + " does not exist");
      }
      result = parent.getSlot(name);
    }
    return result;
  }

  public KlonObject removeSlot(String name) throws MessageNotUnderstood {
    KlonObject result;
    if (slots.containsKey(name)) {
      result = slots.remove(name);
    } else {
      if (parent == null) {
        throw new MessageNotUnderstood(name + " does not exist");
      }
      result = parent.removeSlot(name);
    }
    return result;
  }

  public void configure() {
    Configurator.configure(KlonObject.class, slots);
  }

  @ExposedAs("clone")
  public static KlonObject clone(KlonObject receiver, KlonMessage message)
      throws KlonException {
    return receiver.clone();
  }

  @ExposedAs("send")
  public static KlonObject send(KlonObject receiver, KlonMessage message)
      throws KlonException {
    return receiver.send(message);
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject receiver, KlonMessage message)
      throws KlonException {
    String name = message.evalAsString(receiver, 0);
    return receiver.getSlot(name);
  }

  @ExposedAs("setSlot")
  public static KlonObject setSlot(KlonObject receiver, KlonMessage message)
      throws KlonException {
    String name = message.evalAsString(receiver, 0);
    KlonObject value = message.eval(receiver, 1);
    receiver.setSlot(name, value);
    return value;
  }

  @ExposedAs("updateSlot")
  public static KlonObject updateSlot(KlonObject receiver, KlonMessage message)
      throws KlonException {
    String name = message.evalAsString(receiver, 0);
    KlonObject value = message.eval(receiver, 1);
    receiver.updateSlot(name, value);
    return value;
  }

  @ExposedAs("removeSlot")
  public static KlonObject removeSlot(KlonObject receiver, KlonMessage message)
      throws KlonException {
    String name = message.evalAsString(receiver, 0);
    return receiver.removeSlot(name);
  }

  @ExposedAs("==")
  public KlonObject equals(KlonObject receiver, KlonMessage message) {
    return equals(message.getArguments()
      .get(0)) ? Lobby.Lobby : Lobby.Nil;
  }

}
