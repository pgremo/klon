package klon;

@ExposedAs("Message")
@Bindings("Object")
public class KlonMessage extends KlonObject {

  private static final long serialVersionUID = 7244365877217781727L;

  public static KlonMessage newMessage(KlonObject root)
      throws KlonObject {
    KlonMessage result = (KlonMessage) root.getSlot("Message")
      .clone();
    result.setData(new Message());
    return result;
  }

  public static KlonMessage newMessageWithLiteral(KlonObject root,
      KlonObject literal) throws KlonObject {
    KlonMessage result = (KlonMessage) root.getSlot("Message")
      .clone();
    result.setData(new Message());
    result.setLiteral(literal);
    return result;
  }

  public KlonMessage(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonMessage(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  @Override
  public String getType() {
    return "Message";
  }

  public void setAttached(KlonMessage attached) {
    ((Message) data).setAttached(attached);
  }

  public KlonMessage getAttached() {
    return ((Message) data).getAttached();
  }

  public void setLiteral(KlonObject literal) {
    ((Message) data).setLiteral(literal);
  }

  public KlonObject getLiteral() {
    return ((Message) data).getLiteral();
  }

  public void setNext(KlonMessage selector) {
    ((Message) data).setNext(selector);
  }

  public KlonMessage getNext() {
    return ((Message) data).getNext();
  }

  public void setSelector(KlonObject selector) {
    ((Message) data).setSelector(selector);
  }

  public KlonObject getSelector() {
    return ((Message) data).getSelector();
  }

  public int getArgumentCount() {
    return ((Message) data).getArgumentCount();
  }

  public void addArgument(KlonMessage argument) {
    ((Message) data).addArgument(argument);
  }

  public KlonMessage getArgument(int index) {
    return ((Message) data).getArgument(index);
  }

  public KlonObject eval(KlonObject receiver, KlonObject context)
      throws KlonObject {
    KlonObject self = receiver;
    for (KlonMessage outer = this; outer != null; outer = outer.getNext()) {
      for (KlonMessage inner = outer; inner != null; inner = inner.getAttached()) {
        KlonObject value = inner.getLiteral();
        if (value == null) {
          value = self.perform(context, inner);
        }
        self = value;
      }
      if (outer.getNext() != null) {
        self = context;
      }
    }
    return self;
  }

  public KlonObject evalArgument(KlonObject context, int index)
      throws KlonObject {
    KlonObject result;
    if (index >= getArgumentCount()) {
      result = KlonNil.newNil(context);
    } else {
      result = getArgument(index).eval(context, context);
    }
    return result;
  }

  public void assertArgumentCount(int count) throws KlonObject {
    if (getArgumentCount() < count) {
      throw KlonException.newException(this, "Message.illegalArgumentCount",
        "message must have " + count + " arguments", null);
    }
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
