package klon;

import java.util.Arrays;
import java.util.Map;

public class Klon {

  public static KlonObject ROOT;

  public static void init(String[] args) throws KlonException {
    KlonObject object = new KlonObject();
    ROOT = new KlonObject<Object>(object, null);
    object.setSlot("parent", ROOT);
    ROOT.setSlot("Klon", ROOT);
    ROOT.setSlot("Object", object);

    KlonObject nil = new KlonNil();
    ROOT.setSlot("Nil", nil);

    KlonObject number = new KlonNumber();
    ROOT.setSlot("Number", number);

    KlonObject string = new KlonString();
    ROOT.setSlot("String", string);

    KlonObject block = new KlonBlock();
    ROOT.setSlot("Block", block);

    KlonObject symbol = new KlonSymbol();
    ROOT.setSlot("Symbol", symbol);

    KlonObject exposedMethod = new KlonExposedMethod();
    ROOT.setSlot("ExposedMethod", exposedMethod);

    KlonObject set = new KlonSet();
    ROOT.setSlot("Set", set);

    KlonObject list = new KlonList();
    ROOT.setSlot("List", list);

    KlonObject message = new KlonMessage();
    ROOT.setSlot("Message", message);

    KlonObject properties = object.clone();
    for (Map.Entry<Object, Object> current : System.getProperties().entrySet()) {
      properties.setSlot(current.getKey().toString(), new KlonString(current
          .getValue()
            .toString()));
    }
    ROOT.setSlot("Properties", properties);

    Klon.ROOT.setSlot("Arguments", new KlonList(Arrays.asList(args)));

    object.configure(ROOT);
    nil.configure(ROOT);
    number.configure(ROOT);
    string.configure(ROOT);
    block.configure(ROOT);
    symbol.configure(ROOT);
    exposedMethod.configure(ROOT);
    set.configure(ROOT);
    list.configure(ROOT);
    message.configure(ROOT);
  }

}
