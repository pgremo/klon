package klon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Prototype(name = "NativeMethod", parent = "Object")
public class KlonNativeMethod extends KlonObject {

  private static final long serialVersionUID = -6241106920818116280L;

  public KlonObject newNativeMethod(Object subject) throws KlonObject {
    KlonObject result = duplicate();
    result.setData(subject);
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject activate(KlonObject slot, KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = slot;
    Object value = slot.getData();
    if (value != null) {
      try {
        try {
          result = (KlonObject) ((Method) value).invoke(null, receiver,
            context, message);
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        }
      } catch (KlonException e) {
        ((List<KlonObject>) e.getSlot("stackTrace")
          .getData()).add(((KlonString) slot.getSlot("String")).newString(message.toString()));
        throw e;
      } catch (Throwable e) {
        throw ((KlonException) slot.getSlot("Exception")).newException(e.getClass()
          .getSimpleName(), e.getMessage(), message);
      }
    }
    return result;
  }

}
