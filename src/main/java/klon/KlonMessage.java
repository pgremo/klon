package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringReader;

public class KlonMessage extends KlonObject {

  private static final long serialVersionUID = 7244365877217781727L;

  public static KlonMessage newMessage(KlonObject root) throws KlonObject {
    KlonMessage result = (KlonMessage) root.getSlot("Message").clone();
    result.setData(new Message());
    return result;
  }

  public static KlonMessage newMessageWithLiteral(KlonObject root,
      KlonObject literal) throws KlonObject {
    KlonMessage result = (KlonMessage) root.getSlot("Message").clone();
    result.setData(new Message());
    setLiteral(result, literal);
    return result;
  }

  public static KlonMessage newMessageFromString(KlonObject root, String value)
      throws KlonObject {
    KlonMessage result;
    String message = value.trim();
    if ("".equals(message)) {
      result = newMessageWithLiteral(root, KlonVoid.newVoid(root, message));
    } else {
      try {
        KlonParser parser = new KlonParser(new StringReader(message),
            new MessageBuilder(root));
        result = (KlonMessage) parser.parse().getValue(0);
      } catch (Exception e) {
        throw KlonException.newException(root, e.getClass().getSimpleName(), e
            .getMessage(), null);
      }
    }
    return result;
  }

  public KlonMessage() {

  }

  public KlonMessage(State state) {
    super(state);
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("asString", KlonNativeMethod.newNativeMethod(root,
        KlonMessage.class.getMethod("asString",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("fromString", KlonNativeMethod.newNativeMethod(root,
        KlonMessage.class.getMethod("fromString",
            KlonNativeMethod.PARAMETER_TYPES)));
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    setData(in.readObject());
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(getData());
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonMessage(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  public static void setAttached(KlonObject message, KlonObject attached) {
    ((Message) message.getData()).setAttached(attached);
  }

  public static KlonObject getAttached(KlonObject message) {
    return ((Message) message.getData()).getAttached();
  }

  public static void setLiteral(KlonObject message, KlonObject literal) {
    ((Message) message.getData()).setLiteral(literal);
  }

  public static KlonObject getLiteral(KlonObject message) {
    return ((Message) message.getData()).getLiteral();
  }

  public static void setNext(KlonObject message, KlonObject selector) {
    ((Message) message.getData()).setNext(selector);
  }

  public static KlonObject getNext(KlonObject message) {
    return ((Message) message.getData()).getNext();
  }

  public static void setSelector(KlonObject message, KlonObject selector) {
    ((Message) message.getData()).setSelector(selector);
  }

  public static KlonObject getSelector(KlonObject message) {
    return ((Message) message.getData()).getSelector();
  }

  public static int getArgumentCount(KlonObject message) {
    return ((Message) message.getData()).getArgumentCount();
  }

  public static void addArgument(KlonObject message, KlonObject argument) {
    ((Message) message.getData()).addArgument(argument);
  }

  public static KlonObject getArgument(KlonObject message, int index) {
    return ((Message) message.getData()).getArgument(index);
  }

  public static KlonObject eval(KlonObject message, KlonObject receiver,
      KlonObject context) throws KlonObject {
    KlonObject self = receiver;
    for (KlonObject outer = message; outer != null; outer = KlonMessage
        .getNext(outer)) {
      for (KlonObject inner = outer; inner != null; inner = KlonMessage
          .getAttached(inner)) {
        KlonObject value = KlonMessage.getLiteral(inner);
        if (value == null) {
          value = self.perform(context, inner);
        }
        self = value;
      }
      if (KlonMessage.getNext(outer) != null) {
        self = context;
      }
    }
    return self;
  }

  public static KlonObject evalArgument(KlonObject message, KlonObject context,
      int index) throws KlonObject {
    KlonObject result;
    if (index >= getArgumentCount(message)) {
      result = KlonNil.newNil(context);
    } else {
      result = eval(getArgument(message, index), context, context);
    }
    return result;
  }

  public static void assertArgumentCount(KlonObject message, int count)
      throws KlonObject {
    if (getArgumentCount(message) < count) {
      throw KlonException.newException(message, "Message.invalidArgumentCount",
          "message must have " + count + " arguments", null);
    }
  }

  public static KlonObject fromString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    String string = KlonString.evalAsString(receiver, message, 0);
    try {
      return newMessageFromString(receiver, string);
    } catch (KlonObject e) {
      throw e;
    }
  }

  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
