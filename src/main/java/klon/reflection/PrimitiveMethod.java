package klon.reflection;

import java.lang.reflect.Method;

import klon.KlonException;
import klon.KlonObject;
import klon.KlonMessage;

public class PrimitiveMethod implements Slot {

  private Method method;

  public PrimitiveMethod(Method method) {
    this.method = method;
  }

  public KlonObject activate(Identity receiver, KlonMessage message)
      throws KlonException {
    Object result = null;
    try {
      KlonObject uppedReceiver = receiver.up();
      Object[] downed = new Object[message.getArgumentCount()];
      for (int i = 0; i < message.getArgumentCount(); i++) {
        downed[i] = message.eval(uppedReceiver, i)
          .down();
      }
      result = method.invoke(receiver.down(), downed);
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return Up.UP.up(result);
  }

  public String toString() {
    return "Primitive Method";
  }

}
