package klon;

@Prototype(name = "Exception", bindings = "Object")
public class KlonException extends Identity {

  private static final long serialVersionUID = -6012596192533993069L;

  public static KlonObject newException(KlonObject root, String name,
      String description, Message message) throws KlonObject {
    KlonObject result = root.getSlot("Exception").duplicate();
    if (name != null) {
      result.setSlot("name", KlonString.newString(root, name));
    }
    if (description != null) {
      result.setSlot("description", KlonString.newString(root, description));
    }
    result.setSlot("stackTrace", root.getSlot("List").duplicate());
    return result;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonException());
    return result;
  }

  @ExposedAs("raise")
  public static KlonObject raise(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    throw KlonException.newException(receiver, KlonString.evalAsString(context,
        message, 0), KlonString.evalAsString(context, message, 1), message);
  }

  @ExposedAs("catch")
  public static KlonObject catchException(KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    int index = 0;
    KlonObject result = receiver;
    KlonObject target = message.eval(context, index++);
    if (receiver.isBound(target)) {
      KlonObject scope = context.duplicate();
      if (message.getArgumentCount() == 3) {
        String name = (String) message
            .getArgument(index++)
              .getSelector()
              .getData();
        scope.setSlot(name, receiver);
      }
      message.eval(scope, index);
      result = KlonNoOp.newNoOp(receiver, receiver);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result;
    KlonObject name = receiver.getSlot("name");
    KlonObject description = receiver.getSlot("description");
    if (name == null && description == null) {
      result = Identity.asString(receiver, context, message);
    } else {
      KlonObject stackTrace = receiver.getSlot("stackTrace");
      StringBuilder buffer = new StringBuilder();
      buffer.append(receiver.getSlot("type").getData()).append(" ").append(
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
