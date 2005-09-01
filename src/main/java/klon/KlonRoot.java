package klon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class KlonRoot {

  private KlonRoot() {

  }

  public static KlonObject protoType(String[] args) throws Exception {

    KlonObject object = KlonObject.protoType();
    KlonObject root = object.duplicate();
    root.setSlot("Klon", root);
    root.setSlot("Object", object);
    root.bind(object);

    KlonObject string = KlonString.protoType();
    root.setSlot("String", string);

    KlonObject nativeMethod = KlonNativeMethod.protoType();
    root.setSlot("NativeMethod", nativeMethod);

    KlonObject exception = KlonException.protoType();
    root.setSlot("Exception", exception);

    string.configure(root, KlonString.class);
    nativeMethod.configure(root, KlonNativeMethod.class);
    object.configure(root, KlonObject.class);
    exception.configure(root, KlonException.class);

    KlonObject nil = KlonNil.protoType();
    root.setSlot("Nil", nil);
    nil.configure(root, KlonNil.class);

    KlonObject number = KlonNumber.protoType();
    root.setSlot("Number", number);
    number.configure(root, KlonNumber.class);

    KlonObject block = KlonBlock.protoType();
    root.setSlot("Block", block);
    block.configure(root, KlonBlock.class);

    KlonObject set = KlonSet.protoType();
    root.setSlot("Set", set);
    set.configure(root, KlonSet.class);

    KlonObject list = KlonList.protoType();
    root.setSlot("List", list);
    list.configure(root, KlonList.class);

    KlonObject message = KlonMessage.protoType();
    root.setSlot("Message", message);
    message.configure(root, KlonMessage.class);

    KlonObject random = KlonRandom.protoType();
    root.setSlot("Random", random);
    random.configure(root, KlonRandom.class);

    KlonObject file = KlonFile.protoType();
    root.setSlot("File", file);
    file.configure(root, KlonFile.class);

    KlonObject buffer = KlonBuffer.protoType();
    root.setSlot("Buffer", buffer);
    buffer.configure(root, KlonBuffer.class);

    KlonObject system = object.duplicate();
    root.bind(system);
    system.bind(object);

    KlonObject symbol = KlonSymbol.protoType();
    system.setSlot("Symbol", symbol);
    symbol.configure(root, KlonSymbol.class);

    KlonObject noop = KlonNoOp.protoType();
    system.setSlot("NoOp", noop);
    noop.configure(root, KlonNoOp.class);

    KlonObject locals = KlonLocals.protoType();
    system.setSlot("Locals", locals);
    locals.configure(root, KlonLocals.class);

    KlonObject properties = object.duplicate();
    for (Map.Entry<Object, Object> current : System.getProperties()
      .entrySet()) {
      properties.setSlot(current.getKey()
        .toString(), KlonString.newString(root, current.getValue()
        .toString()));
    }
    root.setSlot("Properties", properties);

    List<KlonObject> arguments = new ArrayList<KlonObject>(args.length);
    for (String current : args) {
      arguments.add(KlonString.newString(root, current));
    }
    root.setSlot("Arguments", KlonList.newList(root, arguments));

    return root;

  }

}
