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
      Message message) throws KlonException {
    KlonException result = (KlonException) duplicate(name);
    KlonObject stringProto = getSlot("String");
    if (name != null) {
      result.setSlot("name", stringProto.duplicate(name));
    }
    if (description != null) {
      result.setSlot("description", stringProto.duplicate(description));
    }
    if (message != null) {
      result.setSlot("source", stringProto.duplicate(message.toString()));
    }
    return result;
  }

  @Override
  public String getMessage() {
    String result = null;
    try {
      result = (String) getSlot("name").getData() + ":"
          + (String) getSlot("description").getData();
    } catch (KlonException e) {
      e.printStackTrace();
    }
    return result;
  }

  @ExposedAs("raise")
  public static KlonObject raise(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    throw ((KlonException) receiver).newException(KlonString.evalAsString(
        context, message, 0), KlonString.evalAsString(context, message, 1),
        message);
  }

  @ExposedAs("catch")
  public static KlonObject catchException(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    int index = 0;
    KlonObject result = receiver;
    KlonObject target = message.eval(context, index++);
    if (receiver.getType().equals(target.getType())) {
      KlonObject scope = context.duplicate();
      if (message.getArgumentCount() == 3) {
        String name = (String) message
            .getArgument(index++)
              .getSelector()
              .getData();
        scope.setSlot(name, receiver);
      }
      message.eval(scope, index);
      result = receiver.getSlot("NoOp").duplicate(receiver);
    }
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result;
    KlonObject name = receiver.getSlot("name");
    KlonObject description = receiver.getSlot("description");
    if (name == null && description == null) {
      result = KlonObject.asString(receiver, context, message);
    } else {
      result = receiver.getSlot("String").duplicate(receiver.toString());
    }
    return result;
  }
}
