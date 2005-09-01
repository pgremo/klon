package klon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Prototype(name = "NativeMethod", parent = "Object")
public class KlonNativeMethod {

  private static final long serialVersionUID = -6241106920818116280L;

  public static KlonObject newNativeMethod(KlonObject root, Method subject)
      throws KlonObject {
    KlonObject result = root.getSlot("NativeMethod")
      .duplicate();
    result.setData(subject);
    return result;
  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonNativeMethod.class);
    Configurator.setDuplicator(result, KlonNativeMethod.class);
    Configurator.setFormatter(result, KlonNativeMethod.class);
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
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
      } catch (KlonObject e) {
        ((List<KlonObject>) e.getSlot("stackTrace")
          .getData()).add(KlonString.newString(receiver, message.toString()));
        throw e;
      } catch (Throwable e) {
        throw KlonException.newException(receiver, e.getClass()
          .getSimpleName(), e.getMessage(), message);
      }
    }
    return result;
  }

}
