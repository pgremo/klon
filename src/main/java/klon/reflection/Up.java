package klon.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import klon.KlonObject;
import klon.Lobby;

public final class Up extends KlonObject {

  public static final Up UP = new Up();
  private static final Map<String, Map<String, KlonObject>> cache = new HashMap<String, Map<String, KlonObject>>();

  private Up() {

  }

  public KlonObject up(Object o) {
    if (o == null) {
      o = Lobby.Nil;
    }
    Class c;
    boolean isInstance = !(o instanceof Class);
    if (isInstance) {
      c = o.getClass();
    } else {
      c = (Class) o;
    }

    return up(o, c, isInstance);
  }

  private KlonObject up(Object o, Class c, boolean isInstance) {
    KlonObject result = new KlonObject();
    result.setDown(o);
    result.setSlots(getSlots(c, isInstance));
    return result;
  }

  private Map<String, KlonObject> getSlots(Class c, boolean isInstance) {
    if (c.equals(Object.class)) {
      c = KlonObject.class;
    }
    String name = c.getName();
    if (!isInstance) {
      name.concat(" CLASS");
    }
    Map<String, KlonObject> table = cache.get(name);
    if (table == null) {
      table = createTable(c, isInstance);
      cache.put(name, table);
    }
    return table;
  }

  private Map<String, KlonObject> createTable(Class c, boolean isInstance) {
    KlonObject parent;
    if (KlonObject.class.equals(c)) {
      parent = null;
    } else {
      parent = up(null, c.getSuperclass(), isInstance);
    }

    Map<String, KlonObject> table = createFromReflection(c, isInstance);
    table.putAll(createFromWrapper(c, isInstance));
    if (parent != null) {
      table.put("parent", parent);
    }
    return table;
  }

  private Map<String, KlonObject> createFromReflection(Class c,
      boolean isInstance) {
    TableBuilder builder = new TableBuilder();
    loadConstructorsFromReflection(c, isInstance, builder);
    loadMethodsFromReflection(c, isInstance, builder);
    loadFieldsFromReflection(c, isInstance, builder);
    loadMethodsFromAnnotation(c, isInstance, builder);
    return builder.buildTable();
  }

  private Map<String, KlonObject> createFromWrapper(Class c, boolean isInstance) {
    TableBuilder builder = new TableBuilder();
    try {
      Class factory = Class.forName("klon.wrapper." + c.getPackage()
        .getName() + ".Klon" + c.getSimpleName());
      loadMethodsFromAnnotation(factory, isInstance, builder);
    } catch (ClassNotFoundException e) {
    }
    return builder.buildTable();
  }

  private void loadConstructorsFromReflection(Class c, boolean isInstance,
      TableBuilder builder) {
    Constructor[] constructors = c.getDeclaredConstructors();
    for (Constructor current : constructors) {
      int modifiers = current.getModifiers();
      if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)
          && !isInstance) {
        String pattern = createPattern("clone", current.getParameterTypes());
        builder.addSlot(pattern, new PrimitiveClone(current), true);
      }
    }
  }

  private void loadMethodsFromAnnotation(Class c, boolean isInstance,
      TableBuilder builder) {
    Method[] methods = c.getDeclaredMethods();
    for (Method current : methods) {
      int modifiers = current.getModifiers();
      if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)
          && Modifier.isStatic(modifiers)) {
        ExposedAs exposed = current.getAnnotation(ExposedAs.class);
        if (exposed != null) {
          builder.addSlot(exposed.value(), new ExposedMethod(current),
            true);
        }
      }
    }
  }

  private void loadMethodsFromReflection(Class c, boolean isInstance,
      TableBuilder builder) {
    Method[] methods = c.getDeclaredMethods();
    for (Method current : methods) {
      int modifiers = current.getModifiers();
      if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)
          && isInstance | Modifier.isStatic(modifiers)) {
        String pattern = createPattern(current.getName(),
          current.getParameterTypes());
        builder.addSlot(pattern, new PrimitiveMethod(current), false);
      }
    }
  }

  private void loadFieldsFromReflection(Class c, boolean isInstance,
      TableBuilder builder) {
    Field[] fields = c.getDeclaredFields();
    for (Field current : fields) {
      int modifiers = current.getModifiers();
      if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)) {
        String pattern = createPattern(current.getName(), new Class[0]);
        builder.addSlot(pattern, new PrimitiveValue(current), false);
      }
    }
  }

  public String createPattern(String name, Class[] parameters) {
    StringBuilder builder = new StringBuilder();
    for (Class current : parameters) {
      if (builder.length() > 0) {
        builder.append("_");
      }
      builder.append(cleanName(current));
    }
    if (builder.length() > 0) {
      builder.insert(0, "_");
    }
    builder.insert(0, name);
    return builder.toString();
  }

  private String cleanName(Class c) {
    StringBuilder builder = new StringBuilder();
    Class current = c;
    while (current.isArray()) {
      builder.append("A");
      current = current.getComponentType();
    }
    builder.insert(0, current.getSimpleName());
    return builder.toString();
  }
}
