package klon;

import java.io.File;

@Prototype(name = "File", parent = "Object")
public class KlonFile {

  private static final long serialVersionUID = 6550367839880573300L;

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    result.setData(new File("/"));
    Configurator.setActivator(result, KlonFile.class);
    Configurator.setDuplicator(result, KlonFile.class);
    Configurator.setFormatter(result, KlonFile.class);
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
