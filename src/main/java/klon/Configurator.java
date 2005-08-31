package klon;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Configurator {

  private Configurator() {

  }

  public static void configure(KlonObject root, KlonObject target,
      Class<? extends Object> type) throws KlonObject {
    Prototype prototype = type.getAnnotation(Prototype.class);
    KlonException exceptionProto = (KlonException) root.getSlot("Exception");
    if (prototype == null) {
      throw exceptionProto.newException("Invalid Argument", type
          + " has not Prototype annotation.", null);
    }
    String parent = prototype.parent();
    if (!"".equals(parent)) {
      target.bind(root.getSlot(parent));
    }
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
          target.setSlot(
            exposedAs.value(),
            ((KlonNumber) root.getSlot("Number")).newNumber(((Number) value).doubleValue()));
        } else if (value instanceof String) {
          target.setSlot(exposedAs.value(),
            ((KlonString) root.getSlot("String")).newString((String) value));
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
          throw exceptionProto.newException("Invalid Argument", identity
              + " must have a return type of " + KlonObject.class + ".", null);
        }
        if (current.getParameterTypes().length != 3) {
          throw exceptionProto.newException("Invalid Argument", identity
              + " must have 3 parameters.", null);
        }
        if (!KlonObject.class.equals(current.getParameterTypes()[0])) {
          throw exceptionProto.newException("Invalid Argument", identity
              + " first parameter must be a " + KlonObject.class + ".", null);
        }
        if (!KlonObject.class.equals(current.getParameterTypes()[1])) {
          throw exceptionProto.newException("Invalid Argument", identity
              + " second parameter must be a " + KlonObject.class + ".", null);
        }
        if (!Message.class.equals(current.getParameterTypes()[2])) {
          throw exceptionProto.newException("Invalid Argument", identity
              + " second parameter must be a " + Message.class + ".", null);
        }
        if (current.getExceptionTypes().length != 1) {
          throw exceptionProto.newException("Invalid Argument", identity
              + " must throw only 1 exception.", null);
        }
        if (!KlonObject.class.equals(current.getExceptionTypes()[0])) {
          throw exceptionProto.newException("Invalid Argument", identity
              + " second parameter must be a " + KlonException.class + ".",
            null);
        }
        target.setSlot(
          exposedAs.value(),
          ((KlonNativeMethod) root.getSlot("NativeMethod")).newNativeMethod(current));
      }
    }

    Method activator = null;
    try {
      activator = type.getDeclaredMethod("activate", new Class[]{
          KlonObject.class,
          KlonObject.class,
          KlonObject.class,
          Message.class});
    } catch (NoSuchMethodException e) {
      if (activator == null) {
        activator = root.getSlot("Object")
          .getActivator();
      }
    }
    target.setActivator(activator);

    Method formatter = null;
    try {
      formatter = type.getDeclaredMethod("format",
        new Class[]{KlonObject.class});
    } catch (NoSuchMethodException e) {
      if (formatter == null) {
        formatter = root.getSlot("Object")
          .getFormatter();
      }
    }
    target.setFormatter(formatter);
  }
}
