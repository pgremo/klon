package klon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Message implements Serializable, Cloneable {

  private static final long serialVersionUID = 735141555296332120L;
  private KlonObject selector;
  private KlonObject literal;
  private List<KlonMessage> arguments;
  private KlonMessage attached;
  private KlonMessage next;

  public Message() {
  }

  public Message(KlonObject literal) {
    this.literal = literal;
  }

  public int getArgumentCount() {
    return arguments == null ? 0 : arguments.size();
  }

  public void addArgument(KlonMessage message) {
    if (arguments == null) {
      arguments = new ArrayList<KlonMessage>();
    }
    arguments.add(message);
  }

  public KlonMessage getArgument(int index) {
    return arguments == null ? null : arguments.get(index);
  }

  public KlonMessage getAttached() {
    return attached;
  }

  public void setAttached(KlonMessage attached) {
    this.attached = attached;
  }

  public KlonObject getLiteral() {
    return literal;
  }

  public void setLiteral(KlonObject literal) {
    this.literal = literal;
  }

  public KlonMessage getNext() {
    return next;
  }

  public void setNext(KlonMessage next) {
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
    if (arguments != null) {
      for (KlonMessage current : arguments) {
        result.addArgument(current);
      }
    }
    result.setAttached(attached);
    result.setLiteral(literal);
    result.setNext(next);
    result.setSelector(selector);
    return result;
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
        .append(attached.getData());
    }
    if (next != null) {
      result.append(";\n")
        .append(next.getData());
    }
    return result.toString();
  }

  private void argumentsToString(StringBuilder result) {
    if (arguments != null && arguments.size() > 0) {
      result.append("(");
      Iterator<KlonMessage> iterator = arguments.iterator();
      while (iterator.hasNext()) {
        result.append(iterator.next()
          .getData());
        if (iterator.hasNext()) {
          result.append(", ");
        }
      }
      result.append(")");
    }
  }

}
