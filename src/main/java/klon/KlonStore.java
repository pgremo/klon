package klon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@ExposedAs("Store")
@Bindings("Object")
public class KlonStore extends KlonObject {

  private static final long serialVersionUID = -4140594553364102878L;

  public KlonStore(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @Override
  public String getType() {
    return "Store";
  }

  @ExposedAs("store")
  public static KlonObject store(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    validate(receiver, message);
    ObjectOutputStream out = null;
    try {
      out = new ObjectOutputStream(new FileOutputStream(
        (String) receiver.getSlot("path")
          .getData()));
      out.writeObject(receiver.getState()
        .getRoot());
      return receiver;
    } catch (Exception e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), message);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
        }
      }
    }
  }

  @ExposedAs("load")
  public static KlonObject load(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    validate(receiver, message);
    ObjectInputStream in = null;
    try {
      in = new ObjectInputStream(new FileInputStream((String) receiver.getSlot(
        "path")
        .getData()));
      KlonObject root = (KlonObject) in.readObject();
      receiver.getState()
        .setRoot(root);
      return root;
    } catch (Exception e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), message);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
  }

  private static void validate(KlonObject receiver, Message message)
      throws KlonObject {
    KlonObject pathSlot = receiver.getSlot("path");
    if (pathSlot == null) {
      throw KlonException.newException(receiver, "Object.invalidArgument",
        "path is required", message);
    }
  }
}
