package klon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Message {

  private KlonSymbol selector;
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

  public KlonSymbol getSelector() {
    return selector;
  }

  public void setSelector(KlonSymbol selector) {
    this.selector = selector;
  }

  public KlonObject eval(KlonObject receiver, KlonObject context)
      throws KlonException {
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

  public KlonObject eval(KlonObject context, int index) throws KlonException {
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
      throws KlonException {
    if (arguments.size() != count) {
      throw ((KlonException) receiver.getSlot("Exception")).newException(
        "Illegal Argument", "message must have " + count + " arguments", null);
    }
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (selector != null) {
      result.append(selector);
    }
    if (literal != null) {
      result.append(literal);
    }
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

}
