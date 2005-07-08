package klon.reflection;

import java.lang.reflect.Method;

import klon.KlonException;
import klon.KlonObject;
import klon.Message;

public class PrimitiveMethod extends KlonObject {

  private Method method;

  public PrimitiveMethod(Method method) {
    this.method = method;
  }

  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    Object result = null;
    try {
      result = method.invoke(receiver.down(), message.getDownArguments());
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return Up.UP.up(result);
  }

  public String toString() {
    return "Primitive Method";
  }

}
