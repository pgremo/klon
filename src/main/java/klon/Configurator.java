package klon;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Configurator {

  private Configurator() {

  }

  public static void setSlots(KlonObject root, KlonObject target,
      Class<? extends Object> type) throws KlonObject {

    Prototype prototype = type.getAnnotation(Prototype.class);
    if (prototype == null) {
      throw KlonException.newException(root, "Invalid Argument", type
          + " has not Prototype annotation.", null);
    }

    String parent = prototype.parent();
    if (!"".equals(parent)) {
      target.bind(root.getSlot(parent));
    }

    target.setSlot("type", KlonString.newString(root, prototype.name()));

    for (Field current : type.getDeclaredFields()) {
      ExposedAs exposedAs = current.getAnnotation(ExposedAs.class);
      if (exposedAs != null) {
        Object value = null;
        try {
          value = current.get(null);
        } catch (Exception e) {
          e.printStackTrace();
        }
        if (value instanceof Number) {
          target.setSlot(exposedAs.value(), KlonNumber.newNumber(root,
            ((Number) value).doubleValue()));
        } else if (value instanceof String) {
          target.setSlot(exposedAs.value(), KlonString.newString(root,
            (String) value));
        }
      }
    }

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
        if (current.getParameterTypes().length != 3) {
          throw KlonException.newException(root, "Invalid Argument", identity
              + " must have 3 parameters.", null);
        }
        if (!KlonObject.class.equals(current.getParameterTypes()[0])) {
          throw KlonException.newException(root, "Invalid Argument", identity
              + " first parameter must be a " + KlonObject.class + ".", null);
        }
        if (!KlonObject.class.equals(current.getParameterTypes()[1])) {
          throw KlonException.newException(root, "Invalid Argument", identity
              + " second parameter must be a " + KlonObject.class + ".", null);
        }
        if (!Message.class.equals(current.getParameterTypes()[2])) {
          throw KlonException.newException(root, "Invalid Argument", identity
              + " second parameter must be a " + Message.class + ".", null);
        }
        if (current.getExceptionTypes().length != 1) {
          throw KlonException.newException(root, "Invalid Argument", identity
              + " must throw only 1 exception.", null);
        }
        if (!KlonObject.class.equals(current.getExceptionTypes()[0])) {
          throw KlonException.newException(root, "Invalid Argument", identity
              + " second parameter must be a " + KlonException.class + ".",
            null);
        }
        target.setSlot(exposedAs.value(), KlonNativeMethod.newNativeMethod(
          root, current));
      }
    }
  }

  public static void setDuplicator(KlonObject target,
      Class<? extends Object> type) {
    Method duplicator = null;
    try {
      duplicator = type.getDeclaredMethod("duplicate",
        new Class[]{KlonObject.class});
    } catch (NoSuchMethodException e) {
      try {
        duplicator = KlonObject.class.getDeclaredMethod("duplicate",
          new Class[]{KlonObject.class});
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    target.setDuplicator(duplicator);
  }

  public static void setFormatter(KlonObject target,
      Class<? extends Object> type) {
    Method formatter = null;
    try {
      formatter = type.getDeclaredMethod("format",
        new Class[]{KlonObject.class});
    } catch (NoSuchMethodException e) {
      try {
        formatter = KlonObject.class.getDeclaredMethod("format",
          new Class[]{KlonObject.class});
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    target.setFormatter(formatter);
  }

  public static void setActivator(KlonObject target,
      Class<? extends Object> type) {
    Method activator = null;
    try {
      activator = type.getDeclaredMethod("activate", new Class[]{
          KlonObject.class,
          KlonObject.class,
          KlonObject.class,
          Message.class});
    } catch (NoSuchMethodException e) {
      try {
        activator = KlonObject.class.getDeclaredMethod("activate", new Class[]{
            KlonObject.class,
            KlonObject.class,
            KlonObject.class,
            Message.class});
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    target.setActivator(activator);
  }
}
