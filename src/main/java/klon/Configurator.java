package klon;

import java.lang.reflect.Method;

public final class Configurator {

  private Configurator() {

  }

  public static void configure(KlonObject root, KlonObject target)
      throws Exception {

    Class<? extends Object> type = target.getClass();
    setBindings(root, target, type);
    setSlotsFromMethods(root, target, type);
  }

  private static void setBindings(KlonObject root, KlonObject target,
      Class<? extends Object> type) throws KlonObject {
    Bindings prototype = type.getAnnotation(Bindings.class);
    if (prototype != null) {
      for (String current : prototype.value()) {
        KlonObject binding = root.getSlot(current);
        if (binding != null) {
          target.bind(binding);
        }
      }
    }
  }

  private static void setSlotsFromMethods(KlonObject root, KlonObject target,
      Class<? extends Object> type) throws KlonObject {
    for (Method current : type.getDeclaredMethods()) {
      ExposedAs exposedAs = current.getAnnotation(ExposedAs.class);
      if (exposedAs != null) {
        KlonObject nativeMethod = KlonNativeMethod.newNativeMethod(root,
            current);
        for (String name : exposedAs.value()) {
          target.setSlot(name, nativeMethod);
        }
      }
    }
  }

}
