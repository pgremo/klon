package klon;

import java.lang.reflect.Method;
import java.util.Map;

import klon.reflection.ExposedAs;
import klon.reflection.ExposedMethod;

public final class Configurator {

  private Configurator() {

  }

  public static void configure(Class type, Map<String, KlonObject> slots) {
    for (Method current : type.getDeclaredMethods()) {
      ExposedAs exposedAs = current.getAnnotation(ExposedAs.class);
      if (exposedAs != null) {
        slots.put(exposedAs.value(), new ExposedMethod(current));
      }
    }
  }
}
