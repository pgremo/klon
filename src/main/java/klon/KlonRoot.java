package klon;

import java.lang.reflect.Method;
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
    KlonObject object = KlonObject.prototype();
    KlonObject root = object.duplicate();
    KlonObject prototypes = object.duplicate();
    root.setSlot("Klon", root);
    root.setSlot("Prototypes", prototypes);
    prototypes.setSlot(object.getIdentity()
      .getName(), object);
    root.bind(object);
    root.bind(prototypes);

    KlonObject string = KlonString.prototype();
    prototypes.setSlot(string.getIdentity()
      .getName(), string);

    KlonObject nativeMethod = KlonNativeMethod.prototype();
    prototypes.setSlot(nativeMethod.getIdentity()
      .getName(), nativeMethod);

    string.configure(root, KlonString.class);
    nativeMethod.configure(root, KlonNativeMethod.class);
    object.configure(root, Identity.class);

    Class[] types = new Class[]{
        KlonBlock.class,
        KlonBuffer.class,
        KlonCompiler.class,
        KlonDirectory.class,
        KlonException.class,
        KlonFile.class,
        KlonList.class,
        KlonLocals.class,
        KlonMap.class,
        KlonMessage.class,
        KlonNil.class,
        KlonNoOp.class,
        KlonNumber.class,
        KlonRandom.class,
        KlonStore.class};
    for (Class<? extends Object> current : types) {
      Method creator = current.getDeclaredMethod("prototype", (Class[]) null);
      KlonObject prototype = (KlonObject) creator.invoke(null);
      prototypes.setSlot(prototype.getIdentity()
        .getName(), prototype);
      prototype.configure(root, current);
    }

    Properties klonProperties = new Properties();
    klonProperties.load(KlonRoot.class.getResourceAsStream("/klon/version.properties"));
    klonProperties.load(KlonRoot.class.getResourceAsStream("/klon/klon.properties"));
    for (Map.Entry<Object, Object> current : klonProperties.entrySet()) {
      String name = "klon." + current.getKey()
        .toString();
      if (System.getProperties()
        .get(name) == null) {
        System.getProperties()
          .put(name, current.getValue()
            .toString());
      }
    }
    KlonObject properties = object.duplicate();
    for (Map.Entry<Object, Object> current : System.getProperties()
      .entrySet()) {
      properties.setSlot(current.getKey()
        .toString(), KlonString.newString(root, current.getValue()
        .toString()));
    }
    root.setSlot("Properties", properties);

    KlonObject environment = object.duplicate();
    for (Map.Entry<String, String> current : System.getenv()
      .entrySet()) {
      environment.setSlot(current.getKey(), KlonString.newString(root,
        current.getValue()));
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
