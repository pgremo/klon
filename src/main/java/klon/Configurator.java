package klon;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Configurator {

  private static final Class[] VALID_PARAMETERS = new Class[]{
      KlonObject.class,
      KlonObject.class,
      Message.class};
  private static final Class[] VALID_EXCEPTIONS = new Class[]{KlonObject.class};

  private Configurator() {

  }

  public static void setSlots(KlonObject root, KlonObject target,
      Class<? extends Object> type) throws Exception {

    Prototype prototype = type.getAnnotation(Prototype.class);
    if (prototype == null) {
      throw KlonException.newException(root, "Invalid Argument", type.getName()
          + " must have a " + Prototype.class.getSimpleName() + " annotation.",
        null);
    }

    for (String current : prototype.bindings()) {
      KlonObject binding = root.getSlot(current);
      if (binding != null) {
        target.bind(binding);
      }
    }

    target.setSlot("type", KlonString.newString(root, prototype.name()));

    setSlotsFromFields(root, target, type);
    setSlotsFromMethods(root, target, type);
  }

  private static void setSlotsFromMethods(KlonObject root, KlonObject target,
      Class<? extends Object> type) throws KlonObject {
    for (Method current : type.getDeclaredMethods()) {
      ExposedAs exposedAs = current.getAnnotation(ExposedAs.class);
      if (exposedAs != null) {
        String identity = "'" + current.getName() + "' in "
            + current.getDeclaringClass() + " exposed as '" + exposedAs.value()
            + "'";
        if (!KlonObject.class.equals(current.getReturnType())) {
          throw KlonException.newException(root, "Invalid Argument", identity
              + " must have a return type of " + KlonObject.class + ".", null);
        }
        validateParameters(root, current, identity);
        validateExceptions(root, current, identity);
        KlonObject nativeMethod = KlonNativeMethod.newNativeMethod(root,
          current);
        for (String name : exposedAs.value()) {
          target.setSlot(name, nativeMethod);
        }
      }
    }
  }

  private static void validateExceptions(KlonObject root, Method current,
      String identity) throws KlonObject {
    if (current.getExceptionTypes().length != VALID_EXCEPTIONS.length) {
      throw KlonException.newException(root, "Invalid Argument", identity
          + " must have " + VALID_EXCEPTIONS.length + " exception(s).", null);
    }
    for (int i = 0; i < VALID_EXCEPTIONS.length; i++) {
      if (!VALID_EXCEPTIONS[i].equals(current.getExceptionTypes()[i])) {
        throw KlonException.newException(root, "Invalid Argument", identity
            + " exception " + i + " must be a " + VALID_EXCEPTIONS[i] + ".",
          null);
      }
    }
  }

  private static void validateParameters(KlonObject root, Method current,
      String identity) throws KlonObject {
    if (current.getParameterTypes().length != VALID_PARAMETERS.length) {
      throw KlonException.newException(root, "Invalid Argument", identity
          + " must have " + VALID_PARAMETERS.length + " parameter(s).", null);
    }
    for (int i = 0; i < VALID_PARAMETERS.length; i++) {
      if (!VALID_PARAMETERS[i].equals(current.getParameterTypes()[i])) {
        throw KlonException.newException(root, "Invalid Argument", identity
            + " parameter " + i + " must be a " + VALID_PARAMETERS[i] + ".",
          null);
      }
    }
  }

  private static void setSlotsFromFields(KlonObject root, KlonObject target,
      Class<? extends Object> type) throws Exception {
    for (Field current : type.getDeclaredFields()) {
      ExposedAs exposedAs = current.getAnnotation(ExposedAs.class);
      if (exposedAs != null) {
        Object value = current.get(null);
        KlonObject object = null;
        if (value instanceof Number) {
          object = KlonNumber.newNumber(root, ((Number) value).doubleValue());
        } else if (value instanceof String) {
          object = KlonString.newString(root, (String) value);
        }
        if (value != null) {
          for (String name : exposedAs.value()) {
            target.setSlot(name, object);
          }
        }
      }
    }
  }

}
