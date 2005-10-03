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
  private KlonMessage asString;
  private KlonMessage init;
  private KlonObject nil;
  private KlonObject mirror;
  private KlonObject locals;

  private State() throws Exception {
    this(new String[]{});
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
      String name = "klon." + current.getKey()
        .toString();
      klonProperties.put(name, current.getValue()
        .toString());
    }
    klonProperties.putAll(System.getProperties());
    System.setProperties(klonProperties);

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

    nil = new KlonNil(this);
    prototypes.setSlot(nil.getName(), nil);
    nil.configure(root);

    mirror = new KlonMirror(this);
    prototypes.setSlot(mirror.getName(), mirror);
    mirror.configure(root);

    locals = new KlonLocals(this);
    prototypes.setSlot(locals.getName(), locals);
    locals.configure(root);

    Class[] types = new Class[]{
        KlonFunction.class,
        KlonBuffer.class,
        KlonDirectory.class,
        KlonException.class,
        KlonFile.class,
        KlonList.class,
        KlonMap.class,
        KlonMessage.class,
        KlonNumber.class,
        KlonImporter.class,
        KlonRandom.class,
        KlonStore.class};
    for (Class<? extends Object> current : types) {
      Constructor constructor = current.getDeclaredConstructor(new Class[]{State.class});
      KlonObject prototype = (KlonObject) constructor.newInstance(new Object[]{this});
      prototypes.setSlot(prototype.getName(), prototype);
      prototype.configure(root);
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

    List<KlonObject> args = new ArrayList<KlonObject>(arguments.length);
    for (String current : arguments) {
      args.add(KlonString.newString(root, current));
    }
    root.setSlot("Arguments", KlonList.newList(root, args));

    asString = KlonMessage.newMessageFromString(root, "asString");
    init = KlonMessage.newMessageFromString(root, "?init");

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

  public KlonObject getNil() {
    return nil;
  }

  public KlonObject getMirror() {
    return mirror;
  }

  public KlonObject getLocals() {
    return locals;
  }

  public KlonMessage getAsString() {
    return asString;
  }

  public KlonMessage getInit() {
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

  public KlonObject doString(String value) throws KlonObject {
    return doString(root, value);
  }

  public KlonObject doString(KlonObject target, String value) throws KlonObject {
    return KlonMessage.newMessageFromString(root, value)
      .eval(target, target);
  }
}
