package klon;

import java.util.List;

public class Message extends Expression {

  private String selector;
  private List<Expression> arguments;
  private Message attached;

  public Message(String selector, Message attached, List<Expression> arguments) {
    this.selector = selector;
    this.attached = attached;
    this.arguments = arguments;
  }

  public List<Expression> getArguments() {
    return arguments;
  }

  public String getSelector() {
    return selector;
  }

  public Message getAttached() {
    return attached;
  }
  
  public void setAttached(Message value){
    attached = value;
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
    if (attached != null) {
      result.append(" ")
        .append(attached);
    }
    return result.insert(0, selector)
      .toString();
  }

}
