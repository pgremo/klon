package klon;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class KlonRoot {

  private static KlonObject ROOT;

  private KlonRoot() {

  }

  public static KlonObject getROOT() {
    return ROOT;
  }

  public static void setROOT(KlonObject root) {
    ROOT = root;
  }

  public static void setup(String[] args) throws Exception {
    KlonObject object = new KlonObject();
    KlonObject root = object.clone();
    KlonObject prototypes = object.clone();
    root.setSlot("Klon", root);
    root.setSlot("Prototypes", prototypes);
    prototypes.setSlot(object.getName(), object);
    root.bind(object);
    root.bind(prototypes);
    object.bind(root);

    KlonObject string = new KlonString();
    prototypes.setSlot(string.getName(), string);

    KlonObject nativeMethod = new KlonNativeMethod();
    prototypes.setSlot(nativeMethod.getName(), nativeMethod);

    string.configure(root);
    nativeMethod.configure(root);
    object.configure(root);

    Class[] types = new Class[] { KlonBlock.class, KlonBuffer.class,
        KlonCompiler.class, KlonDirectory.class, KlonException.class,
        KlonFile.class, KlonList.class, KlonLocals.class, KlonMap.class,
        KlonMessage.class, KlonNil.class, KlonNoOp.class, KlonNumber.class,
        KlonRandom.class, KlonStore.class };
    for (Class<? extends Object> current : types) {
      Constructor constructor = current.getDeclaredConstructor((Class[]) null);
      KlonObject prototype = (KlonObject) constructor
          .newInstance((Object[]) null);
      prototypes.setSlot(prototype.getName(), prototype);
      prototype.configure(root);
    }

    Properties klonProperties = new Properties();
    klonProperties.load(KlonRoot.class
        .getResourceAsStream("/klon/version.properties"));
    klonProperties.load(KlonRoot.class
        .getResourceAsStream("/klon/klon.properties"));
    for (Map.Entry<Object, Object> current : klonProperties.entrySet()) {
      String name = "klon." + current.getKey().toString();
      if (System.getProperties().get(name) == null) {
        System.getProperties().put(name, current.getValue().toString());
      }
    }
    KlonObject properties = object.clone();
    for (Map.Entry<Object, Object> current : System.getProperties().entrySet()) {
      properties.setSlot(current.getKey().toString(), KlonString.newString(
          root, current.getValue().toString()));
    }
    root.setSlot("Properties", properties);

    KlonObject environment = object.clone();
    for (Map.Entry<String, String> current : System.getenv().entrySet()) {
      environment.setSlot(current.getKey(), KlonString.newString(root, current
          .getValue()));
    }
    root.setSlot("Environment", environment);

    List<KlonObject> arguments = new ArrayList<KlonObject>(args.length);
    for (String current : args) {
      arguments.add(KlonString.newString(root, current));
    }
    root.setSlot("Arguments", KlonList.newList(root, arguments));

    ROOT = root;

  }

}
