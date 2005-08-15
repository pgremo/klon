package klon.reflection;

import java.lang.reflect.Field;

import klon.KlonException;
import klon.KlonMessage;
import klon.KlonObject;

public class PrimitiveValue implements Slot {

  private Field field;

  public PrimitiveValue(Field field) {
    this.field = field;
  }

  public KlonObject activate(Identity receiver, KlonMessage message)
      throws KlonException {
    KlonObject result;
    try {
      if (message.getArgumentCount() > 0) {
        field.set(receiver.down(), message.eval(receiver.up(), 0)
          .down());
      }
      result = Up.UP.up(field.get(receiver));
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return result;
  }

  public String toString() {
    return "Primitive Value: " + field;
  }

}
