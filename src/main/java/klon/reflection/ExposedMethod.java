package klon.reflection;

import java.lang.reflect.Method;

import klon.KlonException;
import klon.KlonMessage;
import klon.KlonObject;

public class ExposedMethod implements Slot {

  private Method method;

  public ExposedMethod(Method method) {
    super();
    this.method = method;
  }

  public KlonObject activate(Identity receiver, KlonMessage message)
      throws KlonException {
    KlonObject result = null;
    try {
      result = (KlonObject) method.invoke(null, receiver, message);
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return result;
  }

  public String toString() {
    return "Primitive Function: " + method;
  }

}
