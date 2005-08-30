package klon;

import java.util.Arrays;
import java.util.Map;

@Prototype(name = "Object")
public class KlonRoot extends KlonObject {

  private static final long serialVersionUID = -5934417382511490890L;

  public KlonRoot(String[] args) {
    this(null, args);
  }

  public KlonRoot(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    setSlot("Klon", root);

    KlonObject object = new KlonObject();
    setSlot("Object", object);
    bind(object);

    KlonString string = new KlonString();
    setSlot("String", string);

    KlonObject exposedMethod = new KlonExposedMethod();
    setSlot("ExposedMethod", exposedMethod);

    KlonObject exception = new KlonException();
    setSlot("Exception", exception);

    exception.configure(root);
    object.configure(root);
    string.configure(root);
    exposedMethod.configure(root);

    KlonObject nil = new KlonNil();
    nil.configure(root);
    setSlot("Nil", nil);

    KlonObject number = new KlonNumber();
    number.configure(root);
    setSlot("Number", number);

    KlonObject block = new KlonBlock();
    block.configure(root);
    setSlot("Block", block);

    KlonObject set = new KlonSet();
    set.configure(root);
    setSlot("Set", set);

    KlonObject list = new KlonList();
    list.configure(root);
    setSlot("List", list);

    KlonObject message = new KlonMessage();
    message.configure(root);
    setSlot("Message", message);

    KlonObject random = new KlonRandom();
    random.configure(root);
    setSlot("Random", random);

    KlonObject file = new KlonFile();
    file.configure(root);
    setSlot("File", file);

    KlonObject buffer = new KlonBuffer();
    buffer.configure(root);
    setSlot("Buffer", buffer);

    KlonObject system = new KlonObject();
    system.bind(object);
    bind(system);

    KlonObject symbol = new KlonSymbol();
    symbol.configure(root);
    system.setSlot("Symbol", symbol);

    KlonObject noop = new KlonNoOp();
    noop.configure(root);
    system.setSlot("NoOp", noop);

    KlonObject locals = new KlonLocals();
    locals.configure(root);
    system.setSlot("Locals", locals);

    KlonObject properties = object.duplicate();
    for (Map.Entry<Object, Object> current : System.getProperties()
      .entrySet()) {
      properties.setSlot(current.getKey()
        .toString(), string.newString(current.getValue()
        .toString()));
    }
    setSlot("Properties", properties);

    setSlot("Arguments",
      ((KlonList) getSlot("List")).newList(Arrays.asList((String[]) data)));

  }

}
