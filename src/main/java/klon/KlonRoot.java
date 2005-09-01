package klon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Prototype(name = "Object")
public class KlonRoot {

  public static KlonObject protoType(String[] args) throws KlonObject {
    KlonObject root = new KlonObject();
    root.setSlot("Klon", root);

    KlonObject object = KlonObject.protoType();
    root.setSlot("Object", object);
    root.bind(object);

    KlonObject string = KlonString.protoType();
    root.setSlot("String", string);

    KlonObject nativeMethod = KlonNativeMethod.protoType();
    root.setSlot("NativeMethod", nativeMethod);

    KlonObject exception = KlonException.protoType();
    root.setSlot("Exception", exception);

    nativeMethod.configure(root, KlonNativeMethod.class);
    object.configure(root, KlonObject.class);
    exception.configure(root, KlonException.class);
    string.configure(root, KlonString.class);
    Configurator.configure(root, root, KlonRoot.class);

    KlonObject nil = KlonNil.protoType();
    nil.configure(root, KlonNil.class);
    root.setSlot("Nil", nil);

    KlonObject number = KlonNumber.protoType();
    number.configure(root, KlonNumber.class);
    root.setSlot("Number", number);

    KlonObject block = KlonBlock.protoType();
    block.configure(root, KlonBlock.class);
    root.setSlot("Block", block);

    KlonObject set = KlonSet.protoType();
    set.configure(root, KlonSet.class);
    root.setSlot("Set", set);

    KlonObject list = KlonList.protoType();
    list.configure(root, KlonList.class);
    root.setSlot("List", list);

    KlonObject message = KlonMessage.protoType();
    message.configure(root, KlonMessage.class);
    root.setSlot("Message", message);

    KlonObject random = KlonRandom.protoType();
    random.configure(root, KlonRandom.class);
    root.setSlot("Random", random);

    KlonObject file = KlonFile.protoType();
    file.configure(root, KlonFile.class);
    root.setSlot("File", file);

    KlonObject buffer = KlonBuffer.protoType();
    buffer.configure(root, KlonBuffer.class);
    root.setSlot("Buffer", buffer);

    KlonObject system = object.duplicate();
    system.bind(object);
    root.bind(system);

    KlonObject symbol = KlonSymbol.protoType();
    symbol.configure(root, KlonSymbol.class);
    system.setSlot("Symbol", symbol);

    KlonObject noop = KlonNoOp.protoType();
    noop.configure(root, KlonNoOp.class);
    system.setSlot("NoOp", noop);

    KlonObject locals = KlonLocals.protoType();
    locals.configure(root, KlonLocals.class);
    system.setSlot("Locals", locals);

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
