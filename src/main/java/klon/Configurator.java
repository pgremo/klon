package klon;

import java.lang.reflect.Method;
import java.util.Map;

import klon.reflection.ExposedAs;
import klon.reflection.ExposedMethod;

public final class Configurator {

  private Configurator() {

  }

  public static void configure(Class type, KlonObject slots)
      throws KlonException {
    for (Method current : type.getDeclaredMethods()) {
      ExposedAs exposedAs = current.getAnnotation(ExposedAs.class);
      if (exposedAs != null) {
        String identity = "'" + current.getName() + "' in "
            + current.getDeclaringClass() + " exposed as '" + exposedAs.value() + "'";
        if (!KlonObject.class.equals(current.getReturnType())) {
          throw new KlonException(identity + " must have a return type of "
              + KlonObject.class + ".");
        }
        if (current.getParameterTypes().length != 3) {
          throw new KlonException(identity + " must have 3 parameters.");
        }
        if (!KlonObject.class.equals(current.getParameterTypes()[0])) {
          throw new KlonException(identity + " first parameter must be a "
              + KlonObject.class + ".");
        }
        if (!KlonObject.class.equals(current.getParameterTypes()[1])) {
          throw new KlonException(identity + " second parameter must be a "
              + KlonObject.class + ".");
        }
        if (!Message.class.equals(current.getParameterTypes()[2])) {
          throw new KlonException(identity + " second parameter must be a "
              + Message.class + ".");
        }
        if (current.getExceptionTypes().length != 1) {
          throw new KlonException(identity + " must throw only 1 exception.");
        }
        if (!KlonException.class.equals(current.getExceptionTypes()[0])) {
          throw new KlonException(identity + " second parameter must be a "
              + KlonException.class + ".");
        }
        slots.setSlot(exposedAs.value(), new ExposedMethod(current));
      }
    }
  }
}
