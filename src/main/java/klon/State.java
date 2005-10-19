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
  private KlonObject asNumber;
  private KlonObject asString;
  private KlonObject init;
  private KlonObject nilObject;
  private KlonObject voidObject;
  private KlonObject localsObject;

  private State() throws Exception {
    this(new String[]{});
  }

  public State(String[] arguments) throws Exception {
    this.arguments = arguments;

    Properties klonProperties = new Properties();
    Properties currentProperties = new Properties();
    Package spec = getClass().getPackage();
    if (spec != null) {
      String specificationTitle = spec.getSpecificationTitle();
      if (specificationTitle == null) {
        specificationTitle = "unkown";
      }
      currentProperties.put("klon.specification.name", specificationTitle);
      String specificationVersion = spec.getSpecificationVersion();
      if (specificationVersion == null) {
        specificationVersion = "unkown";
      }
      currentProperties.put("klon.specification.version", specificationVersion);
      String implementationVersion = spec.getImplementationVersion();
      if (implementationVersion == null) {
        implementationVersion = "unknown";
      }
      currentProperties.put("klon.implementation.version",
        implementationVersion);
    }
    currentProperties.put("klon.shell.prompt", "klon> ");
    klonProperties.putAll(currentProperties);
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
    prototypes.setSlot("Object", object);

    KlonObject string = new KlonString(this);
    prototypes.setSlot("String", string);

    KlonObject nativeMethod = new KlonNativeMethod(this);
    prototypes.setSlot("NativeMethod", nativeMethod);

    string.prototype();
    nativeMethod.prototype();
    object.prototype();

    Constructor constructor = KlonBuffer.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject buffer = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Buffer", buffer);

    constructor = KlonDirectory.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject directory = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Directory", directory);

    constructor = KlonException.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject exception = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Exception", exception);

    constructor = KlonFile.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject file = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("File", file);

    constructor = KlonFunction.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject function = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Function", function);

    constructor = KlonImporter.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject importer = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Importer", importer);

    constructor = KlonList.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject list = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("List", list);

    constructor = KlonLocals.class.getDeclaredConstructor(new Class[]{State.class});
    localsObject = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Locals", localsObject);

    constructor = KlonMap.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject map = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Map", map);

    constructor = KlonMessage.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject message = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Message", message);

    constructor = KlonNil.class.getDeclaredConstructor(new Class[]{State.class});
    nilObject = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Nil", nilObject);

    constructor = KlonNumber.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject number = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Number", number);

    constructor = KlonRandom.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject random = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Random", random);

    constructor = KlonStore.class.getDeclaredConstructor(new Class[]{State.class});
    KlonObject store = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Store", store);

    constructor = KlonVoid.class.getDeclaredConstructor(new Class[]{State.class});
    voidObject = (KlonObject) constructor.newInstance(new Object[]{this});
    prototypes.setSlot("Void", voidObject);

    buffer.prototype();
    directory.prototype();
    exception.prototype();
    file.prototype();
    function.prototype();
    importer.prototype();
    list.prototype();
    localsObject.prototype();
    map.prototype();
    message.prototype();
    nilObject.prototype();
    number.prototype();
    random.prototype();
    store.prototype();
    voidObject.prototype();

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
    asNumber = KlonMessage.newMessageFromString(root, "asNumber");
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

  public KlonObject getNilObject() {
    return nilObject;
  }

  public KlonObject getVoidObject() {
    return voidObject;
  }

  public KlonObject getLocalsObject() {
    return localsObject;
  }

  public KlonObject getAsNumber() {
    return asNumber;
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
