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
        try {
          result = (KlonObject) ((Method) data).invoke(null, receiver, context,
              message);
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        }
      } catch (KlonException e) {
        throw e;
      } catch (Throwable e) {
        throw ((KlonException) getSlot("Exception")).newException(e
            .getClass()
              .getSimpleName(), e.getMessage(), message);
      }
    }
    return result;
  }

}
