package klon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Message implements Serializable, Cloneable {

  private static final long serialVersionUID = 735141555296332120L;
  private KlonObject selector;
  private KlonObject literal;
  private List<Message> arguments = new ArrayList<Message>();
  private Message attached;
  private Message next;

  public Message() {
  }

  public Message(KlonObject literal) {
    this.literal = literal;
  }

  public int getArgumentCount() {
    return arguments.size();
  }

  public void addArgument(Message message) {
    arguments.add(message);
  }

  public void addArgument(KlonObject argument) {
    arguments.add(new Message(argument));
  }

  public Message getArgument(int index) {
    return arguments.get(index);
  }

  public Message getAttached() {
    return attached;
  }

  public void setAttached(Message attached) {
    this.attached = attached;
  }

  public KlonObject getLiteral() {
    return literal;
  }

  public void setLiteral(KlonObject literal) {
    this.literal = literal;
  }

  public Message getNext() {
    return next;
  }

  public void setNext(Message next) {
    this.next = next;
  }

  public KlonObject getSelector() {
    return selector;
  }

  public void setSelector(KlonObject selector) {
    this.selector = selector;
  }

  @Override
  public Object clone() {
    Message result = new Message();
    for (Message current : arguments) {
      result.addArgument(current);
    }
    result.setAttached(attached);
    result.setLiteral(literal);
    result.setNext(next);
    result.setSelector(selector);
    return result;
  }

  public KlonObject eval(KlonObject receiver, KlonObject context)
      throws KlonObject {
    KlonObject self = receiver;
    for (Message outer = this; outer != null; outer = outer.getNext()) {
      for (Message inner = outer; inner != null; inner = inner.getAttached()) {
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

  public KlonObject eval(KlonObject context, int index) throws KlonObject {
    KlonObject result;
    if (index >= arguments.size()) {
      result = context.getSlot("Nil");
    } else {
      result = arguments.get(index)
        .eval(context, context);
    }
    return result;
  }

  public void assertArgumentCount(KlonObject receiver, int count)
      throws KlonObject {
    if (arguments.size() < count) {
      throw KlonException.newException(receiver, "Message.illegalArgumentCount",
        "message must have " + count + " arguments", null);
    }
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (selector != null) {
      result.append(selector.getData());
    }
    if (literal != null) {
      result.append(literal);
    }
    argumentsToString(result);
    if (attached != null) {
      result.append(" ")
        .append(attached);
    }
    if (next != null) {
      result.append(";\n")
        .append(next);
    }
    return result.toString();
  }

  private void argumentsToString(StringBuilder result) {
    if (arguments.size() > 0) {
      result.append("(");
      Iterator<Message> iterator = arguments.iterator();
      while (iterator.hasNext()) {
        result.append(iterator.next());
        if (iterator.hasNext()) {
          result.append(", ");
        }
      }
      result.append(")");
    }
  }

}
