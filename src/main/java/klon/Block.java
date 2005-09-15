package klon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable, Cloneable {

  private static final long serialVersionUID = 8908253496842815003L;
  private List<KlonObject> parameters;
  private Message message;
  private KlonObject scope;

  public Block(List<KlonObject> parameters, Message message) {
    this.parameters = parameters;
    this.message = message;
  }

  public List<KlonObject> getParameters() {
    return parameters;
  }

  public Message getMessage() {
    return message;
  }

  public KlonObject getScope() {
    return scope;
  }

  public void setScope(KlonObject blockLocals) {
    this.scope = blockLocals;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (scope == null) {
      result.append("method");
    } else {
      result.append("block");
    }
    result.append("(");
    for (int i = 0; i < parameters.size(); i++) {
      if (i > 0) {
        result.append(", ");
      }
      result.append(parameters.get(i)
        .getData());
    }
    if (parameters.size() > 0) {
      result.append(", ");
    }
    result.append(message)
      .append(")");
    return result.toString();
  }

  @Override
  public Object clone() {
    return new Block(new ArrayList<KlonObject>(parameters),
      (Message) message.clone());
  }

}
