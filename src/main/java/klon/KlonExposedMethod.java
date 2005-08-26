package klon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Prototype(name = "ExposedMethod", parent = "Object")
public class KlonExposedMethod extends KlonObject {

  private static final long serialVersionUID = -6241106920818116280L;

  public KlonExposedMethod() {
    super();
  }

  public KlonExposedMethod(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = null;
    if (data == null) {
      result = getSlot("Nil");
    } else {
      try {
        result = (KlonObject) ((Method) data).invoke(null, receiver, context,
          message);
      } catch (Throwable e) {
        Throwable cause = e;
        if (cause instanceof InvocationTargetException) {
          cause = ((InvocationTargetException) e).getTargetException();
        }
        if (cause.getCause() != null) {
          cause = cause.getCause();
        }
        if (cause instanceof KlonException) {
          throw (KlonException) cause;
        }
        throw (KlonException) getSlot("Exception").duplicate(cause.getClass()
          .getSimpleName(), cause.getMessage());
      }
    }
    return result;
  }

}
