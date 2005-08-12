package klon;

import java.util.List;

public class Message extends Expression {

  private Literal selector;
  private List<Message> arguments;
  private Message attached;
  private Message next;

  public Message(Literal selector, Message attached, List<Message> arguments) {
    this.selector = selector;
    this.attached = attached;
    this.arguments = arguments;
  }

  public List<Message> getArguments() {
    return arguments;
  }

  public Literal getSelector() {
    return selector;
  }

  public Message getAttached() {
    return attached;
  }

  public void setAttached(Message value) {
    attached = value;
  }

  public Message getNext() {
    return next;
  }

  public void setNext(Message next) {
    this.next = next;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Expression current : arguments) {
      if (result.length() > 0) {
        result.append(", ");
      }
      result.append(current);
    }
    if (arguments.size() > 0) {
      result.insert(0, "(")
        .append(")");
    }
    result.insert(0, selector);
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
