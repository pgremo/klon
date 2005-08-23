package klon;

import java.util.Arrays;
import java.util.Map;

public class KlonRoot extends KlonObject {

  public KlonRoot(String[] args) {
    this.primitive = args;
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    setSlot("Klon", root);

    KlonObject object = new KlonObject();
    setSlot("Object", object);
    setSlot("parent", object);

    KlonObject string = new KlonString();
    setSlot("String", string);

    KlonObject exposedMethod = new KlonExposedMethod();
    setSlot("ExposedMethod", exposedMethod);

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

    KlonObject symbol = new KlonSymbol();
    symbol.configure(root);
    setSlot("Symbol", symbol);

    KlonObject set = new KlonSet();
    set.configure(root);
    setSlot("Set", set);

    KlonObject list = new KlonList();
    list.configure(root);
    setSlot("List", list);

    KlonObject message = new KlonMessage();
    message.configure(root);
    setSlot("Message", message);

    KlonObject properties = object.clone();
    for (Map.Entry<Object, Object> current : System.getProperties().entrySet()) {
      properties.setSlot(current.getKey().toString(), root
          .getSlot("String")
            .clone(current.getValue().toString()));
    }
    setSlot("Properties", properties);

    setSlot("Arguments", getSlot("List").clone(
        Arrays.asList((String[]) primitive)));

  }

}
