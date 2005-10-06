package klon;

import java.util.ArrayList;

@ExposedAs("Exception")
@Bindings("Object")
public class KlonException extends KlonObject {

  private static final long serialVersionUID = -6012596192533993069L;

  public static KlonObject newException(KlonObject root, String name,
      String description, KlonMessage message) throws KlonObject {
    KlonObject result = root.getSlot("Exception")
      .clone();
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
    setType("Exception");
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonException(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  @ExposedAs("raise")
  public static KlonObject raise(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    throw newException(receiver, KlonString.evalAsString(context, message, 0),
      KlonString.evalAsString(context, message, 1), message);
  }

  @ExposedAs("catch")
  public static KlonObject catchException(KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    message.assertArgumentCount(2);
    int index = 0;
    KlonObject result = receiver;
    KlonObject target = message.evalArgument(context, index++);
    if (receiver.isBound(target)) {
      KlonObject scope = context.clone();
      if (message.getArgumentCount() == 3) {
        scope.setSlot((String) message.getArgument(index++)
          .getSelector()
          .getData(), receiver);
      }
      message.evalArgument(scope, index);
      result = KlonVoid.newVoid(receiver, receiver);
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
      StringBuilder buffer = new StringBuilder();
      if (name != null) {
        buffer.append(String.valueOf(name.getData()));
      }
      buffer.append(":");
      if (description != null) {
        buffer.append(String.valueOf(description.getData()));
      }
      buffer.append("\n");
      KlonObject stackTrace = receiver.getSlot("stackTrace");
      for (KlonObject current : (Iterable<KlonObject>) stackTrace.getData()) {
        buffer.append(" at ")
          .append(current.getData()
            .toString())
          .append("\n");
      }
      result = KlonString.newString(receiver, buffer.toString());
    }
    return result;
  }
}
