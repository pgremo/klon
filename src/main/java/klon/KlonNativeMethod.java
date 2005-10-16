package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExposedAs("NativeMethod")
@Bindings("Object")
public class KlonNativeMethod extends KlonObject {

  private static final long serialVersionUID = -5301150120413808899L;

  private static final Class[] PARAMETER_TYPES = new Class[] {
      KlonObject.class, KlonObject.class, KlonObject.class };
  private static final Class[] EXCEPTIONS_TYPE = new Class[] { KlonObject.class };

  private static final Map<Method, KlonObject> existing = new HashMap<Method, KlonObject>();

  public static KlonObject newNativeMethod(KlonObject root, Method subject)
      throws KlonObject {
    KlonObject result = existing.get(subject);
    if (result == null) {
      validateMethod(root, subject);
      result = root.getSlot("NativeMethod").clone();
      result.setData(subject);
      existing.put(subject, result);
    }
    return result;
  }

  private static void validateMethod(KlonObject root, Method current)
      throws KlonObject {
    String identity = "'" + current.getName() + "' in "
        + current.getDeclaringClass();
    if (!Modifier.isStatic(current.getModifiers())) {
      throw new IllegalArgumentException(identity + " must be static "
          + KlonObject.class + ".");
    }
    if (!KlonObject.class.equals(current.getReturnType())) {
      throw new IllegalArgumentException(identity
          + " must have a return type of " + KlonObject.class + ".");
    }
    validateParameters(root, current, identity);
    validateExceptions(root, current, identity);
  }

  @SuppressWarnings("unused")
  private static void validateExceptions(KlonObject root, Method current,
      String identity) throws KlonObject {
    if (current.getExceptionTypes().length != KlonNativeMethod.EXCEPTIONS_TYPE.length) {
      throw new IllegalArgumentException(identity + " must have "
          + KlonNativeMethod.EXCEPTIONS_TYPE.length + " exception(s).");
    }
    for (int i = 0; i < KlonNativeMethod.EXCEPTIONS_TYPE.length; i++) {
      if (!KlonNativeMethod.EXCEPTIONS_TYPE[i].equals(current
          .getExceptionTypes()[i])) {
        throw new IllegalArgumentException(identity + " exception " + i
            + " must be a " + KlonNativeMethod.EXCEPTIONS_TYPE[i] + ".");
      }
    }
  }

  @SuppressWarnings("unused")
  private static void validateParameters(KlonObject root, Method current,
      String identity) throws KlonObject {
    if (current.getParameterTypes().length != KlonNativeMethod.PARAMETER_TYPES.length) {
      throw new IllegalArgumentException(identity + " must have "
          + KlonNativeMethod.PARAMETER_TYPES.length + " parameter(s).");
    }
    for (int i = 0; i < KlonNativeMethod.PARAMETER_TYPES.length; i++) {
      if (!KlonNativeMethod.PARAMETER_TYPES[i].equals(current
          .getParameterTypes()[i])) {
        throw new IllegalArgumentException(identity + " parameter " + i
            + " must be a " + KlonNativeMethod.PARAMETER_TYPES[i] + ".");
      }
    }
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
