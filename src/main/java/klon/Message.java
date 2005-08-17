package klon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Message extends KlonObject {

  private Symbol selector;
  private KlonObject literal;
  private List<Message> arguments = new ArrayList<Message>();
  private Message attached;
  private Message next;

  public List<Message> getArguments() {
    return arguments;
  }

  public int getArgumentCount() {
    return arguments.size();
  }

  public void addArgument(Message message) {
    arguments.add(message);
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

  public Symbol getSelector() {
    return selector;
  }

  public void setSelector(Symbol selector) {
    this.selector = selector;
  }

  public KlonObject eval(KlonObject receiver) throws KlonException {
    KlonObject self = receiver;
    for (Message outer = this; outer != null; outer = outer.getNext()) {
      receiver = self;
      for (Message inner = outer; inner != null; inner = inner.getAttached()) {
        KlonObject result = inner.getLiteral();
        if (result == null) {
          result = receiver.send(inner);
        }
        receiver = result;
      }
    }
    return receiver;
  }

  public KlonObject eval(KlonObject receiver, int index) throws KlonException {
    KlonObject self = receiver;
    for (Message outer = arguments.get(index); outer != null; outer = outer.getNext()) {
      receiver = self;
      for (Message inner = outer; inner != null; inner = inner.getAttached()) {
        KlonObject result = inner.getLiteral();
        if (result == null) {
          result = receiver.send(inner);
        }
        receiver = result;
      }
    }
    return receiver;
  }

  public Double evalAsNumber(KlonObject receiver, int index)
      throws KlonException {
    KlonObject result = eval(receiver, index);
    if (!(result instanceof KlonNumber)) {
      throw new KlonException("result must be a number");
    }
    return (Double) ((KlonNumber) result).getAttached();
  }

  public String evalAsString(KlonObject receiver, int index)
      throws KlonException {
    KlonObject result = eval(receiver, index);
    if (!(result instanceof KlonString)) {
      throw new KlonException("result must be a string");
    }
    return (String) ((KlonString) result).getAttached();
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