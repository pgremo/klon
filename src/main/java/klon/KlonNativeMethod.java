package klon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Prototype(name = "NativeMethod", parent = "Object")
public class KlonNativeMethod extends KlonObject {

  private static final long serialVersionUID = -6241106920818116280L;

  public KlonNativeMethod() {
    super();
  }

  public KlonNativeMethod(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  public KlonObject newNativeMethod(Object subject) throws KlonObject {
    KlonObject result = duplicate();
    result.setData(subject);
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = this;
    Object value = getData();
    if (value != null) {
      try {
        try {
          result = (KlonObject) ((Method) value).invoke(null, receiver, context,
            message);
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        }
      } catch (KlonException e) {
        ((List<KlonObject>) e.getSlot("stackTrace")
          .getData()).add(((KlonString) getSlot("String")).newString(message.toString()));
        throw e;
      } catch (Throwable e) {
        throw ((KlonException) getSlot("Exception")).newException(e.getClass()
          .getSimpleName(), e.getMessage(), message);
      }
    }
    return result;
  }

}
