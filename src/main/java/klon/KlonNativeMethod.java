package klon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Prototype(name = "NativeMethod", parent = "Object")
public class KlonNativeMethod extends KlonObject {

  private static final long serialVersionUID = -6241106920818116280L;

  public KlonNativeMethod() {
    super();
  }

  public KlonNativeMethod(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  public KlonObject newNativeMethod(Object subject) throws KlonException {
    KlonObject result = duplicate();
    result.data = subject;
    return result;
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = this;
    if (data != null) {
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
        throw ((KlonException) getSlot("Exception")).newException(e.getClass()
          .getSimpleName(), e.getMessage(), message);
      }
    }
    return result;
  }

}
