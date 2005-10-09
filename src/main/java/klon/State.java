package klon;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class State implements Serializable {

  private static final long serialVersionUID = -6877483386219227949L;
  private ExitListener exitListener;
  private WriteListener writeListener;
  private String[] arguments;
  private KlonObject root;
  private KlonObject asString;
  private KlonObject init;
  private KlonObject nilObject;
  private KlonObject voidObject;
  private KlonObject localsObject;

  private State() throws Exception {
    this(new String[] {});
  }

  public State(String[] arguments) throws Exception {
    this.arguments = arguments;

    Properties klonProperties = new Properties();
    Properties currentProperties = new Properties();
    currentProperties.load(getClass().getResourceAsStream(
        "/klon/version.properties"));
    currentProperties.load(getClass().getResourceAsStream(
        "/klon/klon.properties"));
    for (Map.Entry<Object, Object> current : currentProperties.entrySet()) {
      String name = "klon." + current.getKey().toString();
      klonProperties.put(name, current.getValue().toString());
    }
    klonProperties.putAll(System.getProperties());
    System.setProperties(klonProperties);

    KlonObject object = new KlonObject(this);
    root = object.clone();
    root.bind(object);
    root.setSlot("Klon", root);

    object.bind(root);

    KlonObject prototypes = object.clone();
    root.bind(prototypes);
    root.setSlot("Prototypes", prototypes);
    prototypes.setSlot(getName(KlonObject.class), object);

    KlonObject string = new KlonString(this);
    prototypes.setSlot(getName(KlonString.class), string);

    KlonObject nativeMethod = new KlonNativeMethod(this);
    prototypes.setSlot(getName(KlonNativeMethod.class), nativeMethod);

    Configurator.configure(root, string);
    Configurator.configure(root, nativeMethod);
    Configurator.configure(root, object);

    Class[] types = new Class[] { KlonFunction.class, KlonBuffer.class,
        KlonDirectory.class, KlonException.class, KlonFile.class,
        KlonList.class, KlonLocals.class, KlonMap.class, KlonMessage.class,
        KlonNil.class, KlonNumber.class, KlonImporter.class, KlonRandom.class,
        KlonStore.class, KlonVoid.class };
    for (Class<? extends Object> current : types) {
      Constructor constructor = current
          .getDeclaredConstructor(new Class[] { State.class });
      KlonObject prototype = (KlonObject) constructor
          .newInstance(new Object[] { this });
      prototypes.setSlot(getName(current), prototype);
      Configurator.configure(root, prototype);
    }

    nilObject = root.getSlot("Nil");
    voidObject = root.getSlot("Void");
    localsObject = root.getSlot("Locals");

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

    List<KlonObject> args = new ArrayList<KlonObject>(arguments.length);
    for (String current : arguments) {
      args.add(KlonString.newString(root, current));
    }
    root.setSlot("Arguments", KlonList.newList(root, args));

    asString = KlonMessage.newMessageFromString(root, "asString");
    init = KlonMessage.newMessageFromString(root, "?init");

  }

  private static String getName(Class<? extends Object> type) {
    ExposedAs exposedAs = type.getAnnotation(ExposedAs.class);
    if (exposedAs == null) {
      throw new RuntimeException(type + " is not exposed.");
    }
    return exposedAs.value()[0];
  }

  public String[] getArguments() {
    return arguments;
  }

  public void setRoot(KlonObject root) {
    this.root = root;
  }

  public KlonObject getRoot() {
    return root;
  }

  public KlonObject getNilObject() {
    return nilObject;
  }

  public KlonObject getVoidObject() {
    return voidObject;
  }

  public KlonObject getLocalsObject() {
    return localsObject;
  }

  public KlonObject getAsString() {
    return asString;
  }

  public KlonObject getInit() {
    return init;
  }

  public ExitListener getExitListener() {
    return exitListener;
  }

  public void setExitListener(ExitListener exitListener) {
    this.exitListener = exitListener;
  }

  public WriteListener getWriteListener() {
    return writeListener;
  }

  public void setWriteListener(WriteListener writeListener) {
    this.writeListener = writeListener;
  }

  public void exit(int result) {
    if (exitListener != null) {
      exitListener.onExit(this, result);
    }
  }

  public void write(String value) {
    if (writeListener != null) {
      writeListener.onWrite(this, value);
    }
  }

  public void write(Object value) {
    write(String.valueOf(value));
  }

  public KlonObject doString(String value) throws KlonObject {
    return doString(root, value);
  }

  public KlonObject doString(KlonObject target, String value) throws KlonObject {
    return KlonMessage.eval(KlonMessage.newMessageFromString(root, value),
        target, target);
  }
}
