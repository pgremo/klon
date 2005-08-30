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
    KlonObject result = super.duplicate();
    result.data = new File("/");
    return result;
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
    return ((KlonString) receiver.getSlot("String")).newString(String.valueOf(receiver.getData()));
  }

}
