package klon;

import java.lang.reflect.Method;

@Prototype(name = "ExposedMethod", parent = "Object")
public class KlonExposedMethod extends KlonObject {

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
    if (primitive == null) {
      result = getSlot("Nil");
    } else {
      try {
        result = (KlonObject) ((Method) primitive).invoke(null, receiver,
          context, message);
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof KlonException) {
          throw (KlonException) cause;
        }
        if (cause != null) {
          throw new KlonException(cause);
        }
        throw new KlonException(e);
      }
    }
    return result;
  }

}
