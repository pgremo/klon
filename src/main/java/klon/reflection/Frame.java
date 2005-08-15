package klon.reflection;

import java.util.HashMap;
import java.util.Map;

import klon.KlonException;
import klon.KlonMessage;
import klon.KlonObject;
import klon.KlonString;
import klon.Lobby;

public class Frame {

  private Frame parent;
  private Map<String, Slot> slots = new HashMap<String, Slot>();

  public Frame(Frame parent, Map<String, Slot> slots) {
    this.parent = parent;
    this.slots = slots;
  }

  public KlonObject delegate(Identity receiver, KlonMessage message)
      throws KlonException {
    KlonObject result = Lobby.Nil;
    Slot slot = slots.get(((KlonString) message.getSelector()).getValue());
    if (slot == null) {
      result = parent.delegate(receiver, message);
    } else {
      result = slot.activate(receiver, message);
    }
    return result;
  }

  public void setSlot(String name, Slot slot) {
    slots.put(name, slot);
  }

}
