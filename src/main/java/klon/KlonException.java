package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

@ExposedAs("Exception")
@Bindings("Object")
public class KlonException extends KlonObject {

  private static final long serialVersionUID = -6012596192533993069L;

  public static KlonObject newException(KlonObject root, String name,
      String description, KlonMessage message) throws KlonObject {
    KlonObject result = root.getSlot("Exception").clone();
    if (name != null) {
      result.setSlot("name", KlonString.newString(root, name));
    }
    if (description != null) {
      result.setSlot("description", KlonString.newString(root, description));
    }
    result.setSlot("stackTrace", KlonList.newList(root,
        new ArrayList<KlonObject>()));
    return result;
  }

  public KlonException() {

  }

  public KlonException(State state) {
    super(state);
  }

  @Override
  public String getType() {
    return "Exception";
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    data = in.readObject();
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(data);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonException(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  @ExposedAs("raise")
  public static KlonObject raise(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    throw KlonException.newException(receiver, KlonString.evalAsString(context,
        message, 0), KlonString.evalAsString(context, message, 1), message);
  }

  @ExposedAs("catch")
  public static KlonObject catchException(KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    int index = 0;
    KlonObject result = receiver;
    KlonObject target = message.evalArgument(context, index++);
    if (receiver.isBound(target)) {
      KlonObject scope = context.clone();
      if (message.getArgumentCount() == 3) {
        String name = (String) message
            .getArgument(index++)
              .getSelector()
              .getData();
        scope.setSlot(name, receiver);
      }
      message.evalArgument(scope, index);
      result = KlonMirror.newMirror(receiver, receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result;
    KlonObject name = receiver.getSlot("name");
    KlonObject description = receiver.getSlot("description");
    if (name == null && description == null) {
      result = KlonObject.asString(receiver, context, message);
    } else {
      KlonObject stackTrace = receiver.getSlot("stackTrace");
      StringBuilder buffer = new StringBuilder();
      buffer.append(receiver.getType()).append(" ").append(
          name.getData().toString()).append(":").append(
          description.getData().toString()).append("\n");
      for (KlonObject current : (Iterable<KlonObject>) stackTrace.getData()) {
        buffer.append(" at ").append(current.getData().toString()).append("\n");
      }
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }

}
