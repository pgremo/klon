package klon.reflection;

import java.lang.reflect.Method;

import klon.KlonException;
import klon.Message;
import klon.KlonObject;

public class ExposedMethod extends KlonObject {

  private Method method;

  public ExposedMethod(Method method) {
    this.method = method;
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context, Message message)
      throws KlonException {
    KlonObject result = null;
    try {
      result = (KlonObject) method.invoke(null, receiver, context, message);
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return result;
  }

  public String toString() {
    return "Exposed Method: " + method;
  }

}
