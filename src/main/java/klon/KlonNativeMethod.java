package klon;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@ExposedAs("NativeMethod")
@Bindings("Object")
public class KlonNativeMethod extends KlonObject {

  private static final long serialVersionUID = -5301150120413808899L;

  public static KlonObject newNativeMethod(KlonObject root, Method subject)
      throws KlonObject {
    KlonObject result = root.getSlot("NativeMethod")
      .clone();
    result.setData(new NativeMethod(subject));
    return result;
  }

  public KlonNativeMethod(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonNativeMethod(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  @Override
  public String getType() {
    return "NativeMethod";
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    KlonObject result = slot;
    Object value = slot.getData();
    if (value != null) {
      try {
        try {
          result = (KlonObject) ((NativeMethod) value).invoke(receiver,
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
