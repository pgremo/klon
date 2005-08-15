package klon;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import klon.reflection.ExposedAs;
import klon.reflection.Identity;

public class KlonObject {

  private List<Map<String, KlonObject>> slots;
  private Object down;

  public KlonObject() {
  }

  public KlonObject(Identity frame) {

  }

  public void setDown(Object value) {
    this.down = value;
  }

  public Object down() {
    return down;
  }

  public void addSlots(Map<String, KlonObject> slots) {
    this.slots.add(slots);
  }

  public KlonObject getSlot(String key) {
    KlonObject result = null;
    Iterator<Map<String, KlonObject>> iterator = slots.iterator();
    while (iterator.hasNext() && result == null) {
      Map<String, KlonObject> current = iterator.next();
      result = current.get(key);
    }
    return result;
  }

  @ExposedAs("clone")
  public static KlonObject send(KlonObject receiver, KlonMessage message)
      throws KlonException {
    return receiver;
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
