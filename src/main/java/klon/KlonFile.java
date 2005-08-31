package klon;

import java.io.File;

@Prototype(name = "File", parent = "Object")
public class KlonFile extends KlonObject {

  private static final long serialVersionUID = 6550367839880573300L;

  public KlonFile() {
    super();
    setData(new File("/"));
  }

  @ExposedAs("setPath")
  public static KlonObject setSeed(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    receiver.setData(new File(KlonString.evalAsString(context, message, 0)));
    return receiver;
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonBuffer) receiver.getSlot("Buffer")).newBuffer((File) receiver.getData());
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonString) receiver.getSlot("String")).newString((File) receiver.getData());
  }

}
