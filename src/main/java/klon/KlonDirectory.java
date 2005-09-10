package klon;

import java.io.File;
import java.io.IOException;

@ExposedAs("Directory")
@Bindings("Object")
public class KlonDirectory extends KlonObject {

  private static final long serialVersionUID = -1107185693710331200L;

  public static KlonObject newDirectory(KlonObject root, File file)
      throws KlonObject {
    KlonObject result = root.getSlot("Directory").clone();
    result.setData(file);
    return result;
  }

  public KlonDirectory() {
    super();
    data = new File("").getAbsoluteFile();
  }

  @Override
  public String getType() {
    return "Directory";
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonDirectory();
    result.bind(this);
    result.setData(data);
    return result;
  }

  @ExposedAs("path")
  public static KlonObject path(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    try {
      return KlonString.newString(receiver, ((File) receiver.getData())
          .getCanonicalPath());
    } catch (IOException e) {
      throw KlonException.newException(receiver, e.getClass().getSimpleName(),
          e.getMessage(), message);
    }
  }

  @ExposedAs("setPath")
  public static KlonObject setPath(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    receiver.setData(new File(KlonString.evalAsString(context, message, 0))
        .getAbsoluteFile());
    return receiver;
  }

  @ExposedAs("name")
  public static KlonObject name(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString
        .newString(receiver, ((File) receiver.getData()).getName());
  }

  @ExposedAs("parent")
  public static KlonObject parent(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return newDirectory(receiver, ((File) receiver.getData()).getParentFile());
  }

  @ExposedAs("count")
  public static KlonObject count(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (double) ((File) receiver.getData())
        .list().length);
  }

  @ExposedAs("create")
  public static KlonObject create(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    File file = (File) receiver.getData();
    if (message.getArgumentCount() > 0) {
      file = new File(file, KlonString.evalAsString(receiver, message, 0));
    }
    file.mkdirs();
    return receiver;
  }

  @ExposedAs("exists")
  public static KlonObject exists(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result;
    if (((File) receiver.getData()).exists()) {
      result = receiver;
    } else {
      result = receiver.getSlot("Nil");
    }
    return result;
  }

  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    String value = (String) message.getArgument(0).getSelector().getData();
    Message code = message.getArgument(1);
    for (File current : ((File) receiver.getData()).listFiles()) {
      context.setSlot(value, KlonFile.newFile(receiver, current));
      result = code.eval(context, context);
    }
    return result;
  }

}
