package klon;

import java.util.HashMap;
import java.util.Map;

import klon.reflection.ExposedAs;
import klon.reflection.SlotFactory;

public class KlonObject {

  private static final SlotFactory factory = new SlotFactory();
  private Object down;
  private Map<String, KlonObject> slots;
  
  public KlonObject(){
    slots = factory.getSlots(KlonObject.class);
  }

  public void setSlots(Map<String, KlonObject> slots) {
    this.slots = slots;
  }

  public void down(Object down) {
    this.down = down;
  }

  public Object down() {
    return down;
  }

  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    return receiver;
  }

  private KlonObject getSlot(String key) {
    KlonObject result = null;
    KlonObject parent = this;
    while (result == null && parent != null) {
      result = parent.slots.get(key);
      if (result == null) {
        parent = parent.slots.get("parent");
      }
    }
    if (result == null) {
      result = Lobby.Nil;
    }
    return result;
  }

  @ExposedAs("perform")
  public static KlonObject perform(KlonObject receiver, KlonObject value)
      throws KlonException {
    Message message = (Message) value.down();
    String slotName = message.getSlotName();
    KlonObject slot = receiver.getSlot(slotName);
    if (slot == Lobby.Nil) {
      throw new MessageNotUnderstood(slotName);
    }
    return slot.activate(receiver,
      new Message(slotName, message.getArguments()));
  }

  @ExposedAs("clone")
  public static KlonObject clone(KlonObject receiver) {
    Map<String, KlonObject> current = new HashMap<String, KlonObject>();
    current.put("parent", receiver);
    KlonObject result = new KlonObject();
    result.setSlots(current);
    return result;
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject receiver, KlonObject value) {
    return receiver.getSlot((String) value.down());
  }

  @ExposedAs("setSlot")
  public static void setSlot(KlonObject receiver, KlonObject key,
      KlonObject value) {
    receiver.slots.put((String) key.down(), value);
  }

  @ExposedAs("removeSlot")
  public static void removeSlot(KlonObject receiver, KlonObject key) {
    receiver.slots.remove(key.down());
  }

  @ExposedAs("==")
  public static KlonObject equals(KlonObject receiver, KlonObject object) {
    return receiver == object ? receiver : Lobby.Nil;
  }
}
