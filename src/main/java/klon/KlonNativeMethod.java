package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExposedAs("NativeMethod")
@Bindings("Object")
public class KlonNativeMethod extends KlonObject {

  private static final long serialVersionUID = -5301150120413808899L;

  public static final Class[] PARAMETER_TYPES = new Class[] { KlonObject.class,
      KlonObject.class, KlonObject.class };
  public static final Class[] EXCEPTIONS_TYPE = new Class[] { KlonObject.class };

  private static final Map<Method, KlonObject> existing = new HashMap<Method, KlonObject>();

  public static KlonObject newNativeMethod(KlonObject root, Method subject)
      throws KlonObject {
    KlonObject result = existing.get(subject);
    if (result == null) {
      result = root.getSlot("NativeMethod").clone();
      result.setData(subject);
      existing.put(subject, result);
    }
    return result;
  }

  public KlonNativeMethod() {

  }

  public KlonNativeMethod(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonNativeMethod(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    Method method = (Method) getData();
    if (method == null) {
      out.writeObject(null);
    } else {
      out.writeObject(method.getDeclaringClass());
      out.writeObject(method.getName());
    }
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    Class type = (Class) in.readObject();
    if (type != null) {
      String name = (String) in.readObject();
      try {
        setData(type.getMethod(name, PARAMETER_TYPES));
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result = this;
    Method value = (Method) getData();
    if (value != null) {
      try {
        try {
          result = (KlonObject) value.invoke(null, receiver, context, message);
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        }
      } catch (KlonObject e) {
        ((List<KlonObject>) e.getSlot("stackTrace").getData()).add(KlonString
            .newString(receiver, message.getData().toString()));
        throw e;
      } catch (Throwable e) {
        throw KlonException.newException(receiver,
            e.getClass().getSimpleName(), e.getMessage(), message);
      }
    }
    return result;
  }

}
