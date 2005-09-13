package klon;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KlonState implements Serializable {

  private static final long serialVersionUID = -6877483386219227949L;
  private KlonObject root;
  private Message asString;

  public KlonState(String[] args) throws Exception {
    KlonObject object = new KlonObject(this);
    root = object.clone();
    KlonObject prototypes = object.clone();
    root.setSlot("Klon", root);
    root.setSlot("Prototypes", prototypes);
    prototypes.setSlot(object.getName(), object);
    root.bind(object);
    root.bind(prototypes);
    object.bind(root);

    KlonObject string = new KlonString(this);
    prototypes.setSlot(string.getName(), string);

    KlonObject nativeMethod = new KlonNativeMethod(this);
    prototypes.setSlot(nativeMethod.getName(), nativeMethod);

    string.configure(root);
    nativeMethod.configure(root);
    object.configure(root);

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
      Constructor constructor = current.getDeclaredConstructor(new Class[]{KlonState.class});
      KlonObject prototype = (KlonObject) constructor.newInstance(new Object[]{this});
      prototypes.setSlot(prototype.getName(), prototype);
      prototype.configure(root);
    }

    Properties klonProperties = new Properties();
    klonProperties.load(getClass().getResourceAsStream(
      "/klon/version.properties"));
    klonProperties.load(getClass().getResourceAsStream("/klon/klon.properties"));
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
    KlonObject properties = object.clone();
    for (Map.Entry<Object, Object> current : System.getProperties()
      .entrySet()) {
      properties.setSlot(current.getKey()
        .toString(), KlonString.newString(root, current.getValue()
        .toString()));
    }
    root.setSlot("Properties", properties);

    KlonObject environment = object.clone();
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

    asString = new Compiler(root).fromString("asString");

  }

  public void setRoot(KlonObject root) {
    this.root = root;
  }

  public KlonObject getRoot() {
    return root;
  }

  public Message getAsString() {
    return asString;
  }

  public KlonObject doString(String value) throws KlonObject {
    Message message = new Compiler(root).fromString(value);
    KlonObject result = message.eval(root, root);
    return result;
  }
}
