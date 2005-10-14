package klon;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@ExposedAs("Directory")
@Bindings("Object")
public class KlonDirectory extends KlonObject {

  private static final long serialVersionUID = -1107185693710331200L;

  public static KlonObject newDirectory(KlonObject root, File file)
      throws KlonObject {
    KlonObject result = root.getSlot("Directory")
      .clone();
    result.setData(file);
    return result;
  }

  public KlonDirectory() {

  }

  public KlonDirectory(State state) {
    super(state);
    setData(new File("").getAbsoluteFile());
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    setData(in.readObject());
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(getData());
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonDirectory(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  @ExposedAs("path")
  public static KlonObject path(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonFile.path(receiver, context, message);
  }

  @ExposedAs("setPath")
  public static KlonObject setPath(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonFile.setPath(receiver, context, message);
  }

  @ExposedAs("name")
  public static KlonObject name(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonFile.name(receiver, context, message);
  }

  @ExposedAs("parent")
  public static KlonObject parent(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonFile.parent(receiver, context, message);
  }

  @ExposedAs("exists")
  public static KlonObject exists(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonFile.exists(receiver, context, message);
  }

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ((File) receiver.getData()).list().length);
  }

  @SuppressWarnings("unused")
  @ExposedAs("create")
  public static KlonObject create(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    ((File) receiver.getData()).mkdirs();
    return receiver;
  }

  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject result = KlonNil.newNil(receiver);
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, 0))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, 1);
    for (File current : ((File) receiver.getData()).listFiles()) {
      context.setSlot(value, KlonFile.newFile(receiver, current));
      result = KlonMessage.eval(code, context, context);
    }
    return result;
  }

}
