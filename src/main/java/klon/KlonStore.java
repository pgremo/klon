package klon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Bindings("Object")
public class KlonStore extends Identity {

  private static final long serialVersionUID = -4140594553364102878L;

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonStore());
    return result;
  }

  @Override
  public KlonObject duplicate(KlonObject value) {
    return value;
  }

  @ExposedAs("type")
  public static String type = "Store";

  @ExposedAs("store")
  public static KlonObject store(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    validate(receiver, message);
    ObjectOutputStream out = null;
    try {
      out = new ObjectOutputStream(new FileOutputStream(
        (String) receiver.getSlot("path")
          .getData()));
      out.writeObject(KlonRoot.getROOT());
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
      KlonRoot.setROOT(root);
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
      throw KlonException.newException(receiver, "Illegal Argument",
        "path is required", message);
    }
  }
}
