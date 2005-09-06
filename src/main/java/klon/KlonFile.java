package klon;

import java.io.File;

@Prototype(name = "File", bindings = "Object")
public class KlonFile extends Identity {

  private static final long serialVersionUID = 3159106516324355579L;

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setData(new File("/"));
    result.setIdentity(new KlonFile());
    return result;
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
    return KlonBuffer.newBuffer(receiver, (File) receiver.getData());
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, (File) receiver.getData());
  }

}
