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
    root.setSlot("Klon", root);
    root.setSlot("Object", object);
    root.bind(object);

    KlonObject string = KlonString.prototype();
    root.setSlot("String", string);

    KlonObject nativeMethod = KlonNativeMethod.prototype();
    root.setSlot("NativeMethod", nativeMethod);

    KlonObject exception = KlonException.prototype();
    root.setSlot("Exception", exception);

    string.configure(root, KlonString.class);
    nativeMethod.configure(root, KlonNativeMethod.class);
    object.configure(root, KlonObject.class);
    exception.configure(root, KlonException.class);

    KlonObject nil = KlonNil.prototype();
    root.setSlot("Nil", nil);
    nil.configure(root, KlonNil.class);

    KlonObject number = KlonNumber.prototype();
    root.setSlot("Number", number);
    number.configure(root, KlonNumber.class);

    KlonObject block = KlonBlock.prototype();
    root.setSlot("Block", block);
    block.configure(root, KlonBlock.class);

    KlonObject list = KlonList.prototype();
    root.setSlot("List", list);
    list.configure(root, KlonList.class);

    KlonObject message = KlonMessage.prototype();
    root.setSlot("Message", message);
    message.configure(root, KlonMessage.class);

    KlonObject random = KlonRandom.prototype();
    root.setSlot("Random", random);
    random.configure(root, KlonRandom.class);

    KlonObject file = KlonFile.prototype();
    root.setSlot("File", file);
    file.configure(root, KlonFile.class);

    KlonObject buffer = KlonBuffer.prototype();
    root.setSlot("Buffer", buffer);
    buffer.configure(root, KlonBuffer.class);

    KlonObject store = KlonStore.prototype();
    root.setSlot("Store", store);
    store.configure(root, KlonStore.class);

    KlonObject compiler = KlonCompiler.prototype();
    root.setSlot("Compiler", compiler);
    compiler.configure(root, KlonCompiler.class);

    KlonObject system = object.duplicate();
    root.bind(system);
    system.bind(object);

    KlonObject symbol = KlonSymbol.prototype();
    system.setSlot("Symbol", symbol);
    symbol.configure(root, KlonSymbol.class);

    KlonObject noop = KlonNoOp.prototype();
    system.setSlot("NoOp", noop);
    noop.configure(root, KlonNoOp.class);

    KlonObject locals = KlonLocals.prototype();
    system.setSlot("Locals", locals);
    locals.configure(root, KlonLocals.class);

    KlonObject properties = object.duplicate();
    for (Map.Entry<Object, Object> current : System.getProperties()
      .entrySet()) {
      properties.setSlot(current.getKey()
        .toString(), KlonString.newString(root, current.getValue()
        .toString()));
    }
    Properties version = new Properties();
    version.load(KlonRoot.class.getResourceAsStream("/klon/version.properties"));
    System.getProperties()
      .putAll(version);
    for (Map.Entry<Object, Object> current : version.entrySet()) {
      properties.setSlot("klon." + current.getKey()
        .toString(), KlonString.newString(root, current.getValue()
        .toString()));
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
