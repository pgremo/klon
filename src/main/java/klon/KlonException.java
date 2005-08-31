package klon;

@Prototype(name = "Exception", parent = "Object")
public class KlonException extends KlonObject {

  private static final long serialVersionUID = 8553657071125334749L;

  public KlonException() {
    super();
  }

  public KlonException(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  public KlonException newException(String name, String description,
      Message message) throws KlonObject {
    KlonException result = (KlonException) duplicate();
    KlonString stringProto = (KlonString) getSlot("String");
    if (name != null) {
      result.setSlot("name", stringProto.newString(name));
    }
    if (description != null) {
      result.setSlot("description", stringProto.newString(description));
    }
    result.setSlot("stackTrace", ((KlonList) getSlot("List")).duplicate());
    return result;
  }

  @Override
  public String getMessage() {
    String result = null;
    try {
      result = (String) getSlot("name").getData() + ":"
          + (String) getSlot("description").getData();
    } catch (KlonObject e) {
      e.printStackTrace();
    }
    return result;
  }

  @ExposedAs("raise")
  public static KlonObject raise(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    throw ((KlonException) receiver).newException(KlonString.evalAsString(
      context, message, 0), KlonString.evalAsString(context, message, 1),
      message);
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
        String name = (String) message.getArgument(index++)
          .getSelector()
          .getData();
        scope.setSlot(name, receiver);
      }
      message.eval(scope, index);
      result = ((KlonNoOp) receiver.getSlot("NoOp")).newNoOp(receiver);
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
      result = KlonObject.asString(receiver, context, message);
    } else {
      KlonList stackTrace = (KlonList) receiver.getSlot("stackTrace");
      StringBuilder buffer = new StringBuilder();
      buffer.append(receiver.getType())
        .append(" ")
        .append(name.getData()
          .toString())
        .append(":")
        .append(description.getData()
          .toString())
        .append("\n");
      for (KlonObject current : (Iterable<KlonObject>) stackTrace.getData()) {
        buffer.append(" at ")
          .append(current.getData()
            .toString())
          .append("\n");
      }
      result = ((KlonString) receiver.getSlot("String")).newString(buffer.toString());
    }
    return result;
  }
}
