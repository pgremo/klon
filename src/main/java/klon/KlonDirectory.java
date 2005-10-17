package klon;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class KlonDirectory extends KlonObject {

  private static final long serialVersionUID = -1107185693710331200L;

  public static KlonObject newDirectory(KlonObject root, File file)
      throws KlonObject {
    KlonObject result = root.getSlot("Directory").clone();
    result.setData(file);
    return result;
  }

  public KlonDirectory() {

  }

  public KlonDirectory(State state) {
    super(state);
    setData(new File("").getAbsoluteFile());
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("path", KlonNativeMethod.newNativeMethod(root, KlonFile.class
        .getMethod("path", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("setPath", KlonNativeMethod.newNativeMethod(root, KlonFile.class
        .getMethod("setPath", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("name", KlonNativeMethod.newNativeMethod(root, KlonFile.class
        .getMethod("name", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("parent", KlonNativeMethod.newNativeMethod(root, KlonFile.class
        .getMethod("parent", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("exists", KlonNativeMethod.newNativeMethod(root, KlonFile.class
        .getMethod("exists", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("size", KlonNativeMethod.newNativeMethod(root, KlonDirectory.class
        .getMethod("size", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("create", KlonNativeMethod.newNativeMethod(root,
        KlonDirectory.class.getMethod("create",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("forEach", KlonNativeMethod.newNativeMethod(root,
        KlonDirectory.class.getMethod("forEach",
            KlonNativeMethod.PARAMETER_TYPES)));
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

  public static KlonObject size(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (double) ((File) receiver.getData())
        .list().length);
  }

  @SuppressWarnings("unused")
  public static KlonObject create(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    ((File) receiver.getData()).mkdirs();
    return receiver;
  }

  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject result = KlonNil.newNil(receiver);
    String value = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, 0)).getData();
    KlonObject code = KlonMessage.getArgument(message, 1);
    for (File current : ((File) receiver.getData()).listFiles()) {
      context.setSlot(value, KlonFile.newFile(receiver, current));
      result = KlonMessage.eval(code, context, context);
    }
    return result;
  }

}
