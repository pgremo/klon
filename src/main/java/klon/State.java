package klon;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class State implements Serializable {

  private static final long serialVersionUID = -6877483386219227949L;
  private ExceptionListener exceptionListener;
  private ExitListener exitListener;
  private WriteListener writeListener;
  private KlonObject root;
  private Message asString;
  private KlonObject nil;
  private KlonObject noOp;
  private KlonObject locals;

  public State(String[] args) throws Exception {
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

    noOp = new KlonNoOp(this);
    prototypes.setSlot(noOp.getName(), noOp);
    noOp.configure(root);

    locals = new KlonLocals(this);
    prototypes.setSlot(locals.getName(), locals);
    locals.configure(root);

    Class[] types = new Class[]{
        KlonBlock.class,
        KlonBuffer.class,
        KlonCompiler.class,
        KlonDirectory.class,
        KlonException.class,
        KlonFile.class,
        KlonList.class,
        KlonMap.class,
        KlonMessage.class,
        KlonNumber.class,
        KlonPrototyper.class,
        KlonRandom.class,
        KlonStore.class};
    for (Class<? extends Object> current : types) {
      Constructor constructor = current.getDeclaredConstructor(new Class[]{State.class});
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

  public KlonObject getNil() {
    return nil;
  }

  public KlonObject getNoOp() {
    return noOp;
  }

  public KlonObject getLocals() {
    return locals;
  }

  public Message getAsString() {
    return asString;
  }

  public void setExceptionListener(ExceptionListener exceptionListener) {
    this.exceptionListener = exceptionListener;
  }

  public void setExitListener(ExitListener exitListener) {
    this.exitListener = exitListener;
  }

  public void setWriteListener(WriteListener writeListener) {
    this.writeListener = writeListener;
  }

  public void exception(KlonObject exception) {
    if (exceptionListener != null) {
      exceptionListener.onException(this, exception);
    }
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

  public KlonObject doString(String value) {
    KlonObject result = null;
    try {
      result = new Compiler(root).fromString(value)
        .eval(root, root);
    } catch (KlonObject e) {
      exception(e);
    }
    return result;
  }
}
