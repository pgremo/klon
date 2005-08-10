package klon.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import klon.KlonObject;

public class SlotFactory {

  private Map<String, Map<String, KlonObject>> cache = new HashMap<String, Map<String, KlonObject>>();

  public Map<String, KlonObject> getSlots(Class c) {
    String name = c.getName();
    Map<String, KlonObject> result = cache.get(name);
    if (result == null) {
      result = loadMethodsFromAnnotation(c);
      cache.put(name, result);
    }
    return null;
  }

  private Map<String, KlonObject> loadMethodsFromAnnotation(Class c) {
    Method[] methods = c.getDeclaredMethods();
    Map<String, KlonObject> result = new HashMap<String, KlonObject>(
      methods.length);
    for (Method current : methods) {
      int modifiers = current.getModifiers();
      if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)
          && Modifier.isStatic(modifiers)) {
        ExposedAs exposed = current.getAnnotation(ExposedAs.class);
        if (exposed != null) {
          result.put(exposed.value(), new PrimitiveExposedMethod(current));
        }
      }
    }
    return result;
  }

}
