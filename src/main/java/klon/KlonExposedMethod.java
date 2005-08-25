package klon;

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
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof KlonException) {
          throw (KlonException) cause;
        }
        if (cause != null) {
          throw (KlonException) getSlot("Exception").duplicate(cause.getClass()
            .getSimpleName(), cause.getMessage());
        }
        throw (KlonException) getSlot("Exception").duplicate(e.getClass()
          .getSimpleName(), e.getMessage());
      }
    }
    return result;
  }

}
