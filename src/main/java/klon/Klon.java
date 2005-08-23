package klon;

import java.util.Arrays;
import java.util.Map;

public class Klon {

  public static KlonObject ROOT;

  public static void init(String[] args) throws KlonException {
    ROOT = new KlonObject();
    ROOT.setSlot("Klon", ROOT);

    KlonObject object = new KlonObject();
    ROOT.setSlot("Object", object);
    ROOT.setSlot("parent", object);

    KlonObject string = new KlonString();
    ROOT.setSlot("String", string);

    KlonObject exposedMethod = new KlonExposedMethod();
    ROOT.setSlot("ExposedMethod", exposedMethod);

    object.configure(ROOT);
    string.configure(ROOT);
    exposedMethod.configure(ROOT);

    KlonObject nil = new KlonNil();
    nil.configure(ROOT);
    ROOT.setSlot("Nil", nil);

    KlonObject number = new KlonNumber();
    number.configure(ROOT);
    ROOT.setSlot("Number", number);

    KlonObject block = new KlonBlock();
    block.configure(ROOT);
    ROOT.setSlot("Block", block);

    KlonObject symbol = new KlonSymbol();
    symbol.configure(ROOT);
    ROOT.setSlot("Symbol", symbol);


    KlonObject set = new KlonSet();
    set.configure(ROOT);
    ROOT.setSlot("Set", set);

    KlonObject list = new KlonList();
    list.configure(ROOT);
    ROOT.setSlot("List", list);

    KlonObject message = new KlonMessage();
    message.configure(ROOT);
    ROOT.setSlot("Message", message);

    KlonObject properties = object.clone();
    for (Map.Entry<Object, Object> current : System.getProperties().entrySet()) {
      properties.setSlot(current.getKey().toString(), new KlonString(current
          .getValue()
            .toString()));
    }
    ROOT.setSlot("Properties", properties);

    Klon.ROOT.setSlot("Arguments", new KlonList(Arrays.asList(args)));

  }

}
