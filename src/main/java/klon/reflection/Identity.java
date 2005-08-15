package klon.reflection;

import java.util.HashMap;

import klon.KlonException;
import klon.KlonMessage;
import klon.KlonObject;

public class Identity {

  private Frame frame;
  private Object down;
  private KlonObject up;

  public Identity(Frame frame, Object down) {
    this.frame = frame;
    this.down = down;
  }

  public KlonObject delegate(Identity receiver, KlonMessage message)
      throws KlonException {
    return frame.delegate(receiver, message);
  }

  public Identity extend() {
    return new Identity(new Frame(frame, new HashMap<String, Slot>()), down);
  }

  public void setSlot(String name, Slot slot) {
    frame.setSlot(name, slot);
  }

  public Object down() {
    if (down == null) {
      down = up();
    }
    return down;
  }

  public KlonObject up() {
    if (up == null) {
      up = new KlonObject(this);
    }
    return up;
  }

}
