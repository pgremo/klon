package klon.reflection;

import java.lang.reflect.Method;

import klon.KlonException;
import klon.KlonObject;
import klon.Message;

public class PrimitiveExposedMethod extends KlonObject {

  private Method method;

  public PrimitiveExposedMethod(Method method) {
    super();
    this.method = method;
  }

  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject result = null;
    try {
      Object[] arguments = message.getArguments();
      Object[] fakes = new Object[arguments.length + 1];
      fakes[0] = receiver;
      System.arraycopy(arguments, 0, fakes, 1, arguments.length);
      result = (KlonObject) method.invoke(null, fakes);
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return result;
  }

  public String toString() {
    return "Primitive Function";
  }

}
