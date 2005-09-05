package klon;

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
    KlonObject system = object.duplicate();
    system.setSlot("Klon", root);
    root.setSlot("Prototypes", prototypes);
    prototypes.setSlot("Object", object);
    root.bind(object);
    root.bind(prototypes);
    root.bind(system);

    KlonObject string = KlonString.prototype();
    prototypes.setSlot("String", string);

    KlonObject nativeMethod = KlonNativeMethod.prototype();
    prototypes.setSlot("NativeMethod", nativeMethod);

    KlonObject exception = KlonException.prototype();
    prototypes.setSlot("Exception", exception);

    string.configure(root, KlonString.class);
    nativeMethod.configure(root, KlonNativeMethod.class);
    object.configure(root, Identity.class);
    exception.configure(root, KlonException.class);

    KlonObject nil = KlonNil.prototype();
    prototypes.setSlot("Nil", nil);
    nil.configure(root, KlonNil.class);

    KlonObject number = KlonNumber.prototype();
    prototypes.setSlot("Number", number);
    number.configure(root, KlonNumber.class);

    KlonObject block = KlonBlock.prototype();
    prototypes.setSlot("Block", block);
    block.configure(root, KlonBlock.class);

    KlonObject list = KlonList.prototype();
    prototypes.setSlot("List", list);
    list.configure(root, KlonList.class);

    KlonObject message = KlonMessage.prototype();
    prototypes.setSlot("Message", message);
    message.configure(root, KlonMessage.class);

    KlonObject random = KlonRandom.prototype();
    prototypes.setSlot("Random", random);
    random.configure(root, KlonRandom.class);

    KlonObject file = KlonFile.prototype();
    prototypes.setSlot("File", file);
    file.configure(root, KlonFile.class);

    KlonObject buffer = KlonBuffer.prototype();
    prototypes.setSlot("Buffer", buffer);
    buffer.configure(root, KlonBuffer.class);

    KlonObject store = KlonStore.prototype();
    prototypes.setSlot("Store", store);
    store.configure(root, KlonStore.class);

    KlonObject compiler = KlonCompiler.prototype();
    prototypes.setSlot("Compiler", compiler);
    compiler.configure(root, KlonCompiler.class);

    KlonObject symbol = KlonSymbol.prototype();
    system.setSlot("Symbol", symbol);
    symbol.configure(root, KlonSymbol.class);

    KlonObject noop = KlonNoOp.prototype();
    system.setSlot("NoOp", noop);
    noop.configure(root, KlonNoOp.class);

    KlonObject locals = KlonLocals.prototype();
    system.setSlot("Locals", locals);
    locals.configure(root, KlonLocals.class);

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
    KlonObject properties = object.duplicate();
    for (Map.Entry<Object, Object> current : System.getProperties().entrySet()) {
      properties.setSlot(current.getKey().toString(), KlonString.newString(
          root, current.getValue().toString()));
    }
    root.setSlot("Properties", properties);

    List<KlonObject> arguments = new ArrayList<KlonObject>(args.length);
    for (String current : args) {
      arguments.add(KlonString.newString(root, current));
    }
    root.setSlot("Arguments", KlonList.newList(root, arguments));

    ROOT = root;

  }

}
