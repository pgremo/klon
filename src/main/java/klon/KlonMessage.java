package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringReader;

import klon.grammar.grammatica.KlonParser;

@ExposedAs("Message")
@Bindings("Object")
public class KlonMessage extends KlonObject {

  private static final long serialVersionUID = 7244365877217781727L;

  public static KlonMessage newMessage(KlonObject root) throws KlonObject {
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

  public static KlonMessage newMessageFromString(KlonObject root, String value)
      throws KlonObject {
    KlonMessage result;
    String message = value.trim();
    if ("".equals(message)) {
      result = newMessageWithLiteral(root, KlonString.newString(root, message));
    } else {
      try {
        KlonParser parser = new KlonParser(new StringReader(message),
          new MessageBuilder(root));
        result = (KlonMessage) parser.parse()
          .getValue(0);
      } catch (Exception e) {
        throw KlonException.newException(root, e.getClass()
          .getSimpleName(), e.getMessage(), null);
      }
    }
    return result;
  }

  public KlonMessage() {

  }

  public KlonMessage(State state) {
    super(state);
    type = "Message";
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
    KlonObject result = new KlonMessage(state);
    result.bind(this);
    result.setData(data);
    return result;
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
      throw KlonException.newException(this, "Message.invalidArgumentCount",
        "message must have " + count + " arguments", null);
    }
  }

  @ExposedAs("fromString")
  public static KlonObject fromString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    String string = KlonString.evalAsString(receiver, message, 0);
    try {
      return newMessageFromString(receiver, string);
    } catch (KlonObject e) {
      throw e;
    }
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
