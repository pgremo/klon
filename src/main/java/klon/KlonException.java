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

  @Override
  public KlonObject duplicate(Object... subject) throws KlonException {
    KlonObject result = super.duplicate(subject);
    if (subject != null) {
      KlonObject stringProto = getSlot("String");
      result.setSlot("name", stringProto.duplicate(subject[0]));
      result.setSlot("description", stringProto.duplicate(subject[1]));
    }
    return result;
  }

  @Override
  public String getMessage() {
    String result = null;
    try {
      result = (String) getSlot("description").getData();
    } catch (KlonException e) {
      e.printStackTrace();
    }
    return result;
  }

  @ExposedAs("raise")
  public static KlonObject raise(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    throw (KlonException) receiver.duplicate(message.evalAsString(context, 0),
      message.evalAsString(context, 1));
  }

  @ExposedAs("catch")
  public static KlonObject catchException(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    int index = 0;
    KlonObject result = message.eval(context, index++);
    if (receiver.getType()
      .equals(result.getType())) {
      KlonObject scope = context.duplicate();
      if (message.getArgumentCount() == 3) {
        String name = (String) message.getArgument(index++)
          .getSelector()
          .getData();
        scope.setSlot(name, receiver);
      }
      message.eval(scope, index);
      result = new KlonNoOp(null, result);
    }
    return result;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String")
      .duplicate(receiver.getSlot("name")
        .getData() + ":" + receiver.getSlot("description")
        .getData());
  }
}
