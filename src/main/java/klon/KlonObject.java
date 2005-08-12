package klon;

import java.util.Map;

import klon.reflection.ExposedAs;
import klon.reflection.SlotFactory;

public class KlonObject {

  private static final SlotFactory factory = new SlotFactory();
  private Object down;
  private Map<String, KlonObject> slots;

  public void setSlots(Map<String, KlonObject> slots) {
    this.slots = slots;
  }

  public void down(Object down) {
    this.down = down;
  }

  public Object down() {
    return down;
  }

  public KlonObject send(KlonObject receiver, KlonObject locals, KlonMessage message)
      throws KlonException {
    return receiver;
  }

  public KlonObject getSlot(String key) {
    KlonObject result = null;
    result = slots.get(key);
    if (result == null) {
      KlonObject superSlot = slots.get("super");
      if (superSlot != null) {
        result = superSlot.getSlot(key);
      }
    }
    return result;
  }

  @ExposedAs("clone")
  public static KlonObject clone(KlonObject reciever, KlonObject locals,
      KlonMessage message) {
    return null;
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject reciever, KlonObject locals,
      KlonMessage message) {
    return null;
  }

  @ExposedAs("setSlot")
  public static KlonObject setSlot(KlonObject reciever, KlonObject locals,
      KlonMessage message) {
    return null;
  }

  @ExposedAs("removeSlot")
  public static KlonObject removeSlot(KlonObject reciever, KlonObject locals,
      KlonMessage message) {
    return null;
  }

  @ExposedAs("==")
  public KlonObject equals(KlonObject reciever, KlonObject locals,
      KlonMessage message) {
    return equals(message.getArguments()
      .get(0)) ? Lobby.Lobby : Lobby.Nil;
  }
}
