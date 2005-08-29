package klon;

import java.io.File;

@Prototype(name = "File", parent = "Object")
public class KlonFile extends KlonObject {

  private static final long serialVersionUID = 6550367839880573300L;

  public KlonFile() {
    super(null, new File("/"));
  }

  public KlonFile(KlonObject parent, Object data) {
    super(parent, data);
  }

  @Override
  public KlonObject duplicate() throws KlonException {
    return super.duplicate(new File("/"));
  }

  @ExposedAs("setPath")
  public static KlonObject setSeed(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    receiver.data = new File(KlonString.evalAsString(context, message, 0));
    return receiver;
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return ((KlonBuffer) receiver.getSlot("Buffer")).newBuffer((File) receiver.getData());
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String")
      .duplicate(String.valueOf(receiver.getData()));
  }

}
