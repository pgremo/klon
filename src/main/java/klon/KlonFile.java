package klon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExposedAs("File")
@Bindings("Object")
public class KlonFile extends KlonObject {

  private static final long serialVersionUID = 3159106516324355579L;

  public static KlonObject newFile(KlonObject root, File file)
      throws KlonObject {
    KlonObject result = root.getSlot("File").clone();
    result.setData(file);
    return result;
  }

  public KlonFile() {
    super();
    data = new File("").getAbsoluteFile();
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonFile();
    result.bind(this);
    result.setData(data);
    return result;
  }

  @Override
  public String getType() {
    return "File";
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
    return KlonDirectory.newDirectory(receiver, ((File) receiver.getData())
        .getParentFile());
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

  @ExposedAs("size")
  public static KlonObject size(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (double) ((File) receiver.getData())
        .length());
  }

  @ExposedAs("remove")
  public static KlonObject remove(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    File file = (File) receiver.getData();
    if (file.exists()) {
      file.delete();
    } else {
      throw KlonException.newException(receiver, "File.doesNotExist", file
          .getAbsolutePath()
          + " does not exist", message);
    }
    return receiver;
  }

  @ExposedAs("moveTo")
  public static KlonObject moveTo(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    message.assertArgumentCount(receiver, 1);
    File target = new File(KlonString.evalAsString(receiver, message, 0));
    File file = (File) receiver.getData();
    if (file.exists()) {
      if (target.exists()) {
        throw KlonException.newException(receiver, "File.nameConflict", target
            .getAbsolutePath()
            + " already exist", message);
      }
      file.renameTo(target);
    } else {
      throw KlonException.newException(receiver, "File.doesNotExist", file
          .getAbsolutePath()
          + " does not exist", message);
    }
    return receiver;
  }

  @ExposedAs("readLines")
  public static KlonObject readLines(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    List<KlonObject> result = new ArrayList<KlonObject>();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader((File) receiver.getData()));
      String line = in.readLine();
      while (line != null) {
        result.add(KlonString.newString(receiver, line));
        line = in.readLine();
      }
    } catch (Exception e) {
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
    return KlonList.newList(receiver, result);
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject nil = receiver.getSlot("Nil");
    KlonObject result = nil;
    int arg = 0;
    String index = null;
    if (message.getArgumentCount() == 3) {
      index = (String) message.getArgument(arg++).getSelector().getData();
    }
    String value = (String) message.getArgument(arg++).getSelector().getData();
    Message code = message.getArgument(arg);
    FileInputStream in = null;
    try {
      in = new FileInputStream((File) receiver.getData());
      int current = in.read();
      int count = 1;
      while (current != -1) {
        if (index != null) {
          context
              .setSlot(index, KlonNumber.newNumber(receiver, (double) count));
        }
        context
            .setSlot(value, KlonNumber.newNumber(receiver, (double) current));
        result = code.eval(context, context);
        current = in.read();
        count++;
      }
    } catch (Exception e) {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e1) {
        }
      }
    }
    return result;
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
