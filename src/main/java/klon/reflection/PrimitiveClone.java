package klon.reflection;

import java.lang.reflect.Constructor;

import klon.KlonException;
import klon.KlonObject;
import klon.KlonMessage;

public class PrimitiveClone implements Slot {

  private Constructor constructor;

  public PrimitiveClone(Constructor constructor) {
    this.constructor = constructor;
  }

  public KlonObject activate(Identity receiver, KlonMessage message)
      throws KlonException {
    Object result = null;
    try {
      KlonObject uppedReceiver = receiver.up();
      Object[] arguments = new Object[message.getArgumentCount()];
      for (int i = 0; i < message.getArgumentCount(); i++) {
        arguments[i] = message.eval(uppedReceiver, i)
          .down();
      }
      result = constructor.newInstance(arguments);
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return Up.UP.up(result);
  }

  public String toString() {
    return "Primitive Clone: " + constructor;
  }
}
