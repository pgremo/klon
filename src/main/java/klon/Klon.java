package klon;

public class Klon {

  public static final KlonObject ROOT;

  static {
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

    try {
      object.configure();
      nil.configure();
      number.configure();
      string.configure();
      block.configure();
      symbol.configure();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
