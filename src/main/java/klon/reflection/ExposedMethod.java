package klon.reflection;

import java.lang.reflect.Method;

import klon.Klon;
import klon.KlonException;
import klon.Message;
import klon.KlonObject;

public class ExposedMethod extends KlonObject<Method> {

  public ExposedMethod() {
    super();
  }

  public ExposedMethod(Method attached) throws KlonException {
    super(Klon.ROOT.getSlot("Object"), attached);
  }

  public ExposedMethod(KlonObject parent, Method attached) {
    super(parent, attached);
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = null;
    try {
      result = (KlonObject) primitive.invoke(null, receiver, context, message);
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return result;
  }

  public String toString() {
    return "Exposed Method: " + primitive;
  }

}
