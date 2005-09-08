package klon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable, Cloneable {

  private static final long serialVersionUID = 8908253496842815003L;
  private List<KlonObject> parameters;
  private Message code;

  public Block(List<KlonObject> parameters, Message code) {
    this.parameters = parameters;
    this.code = code;
  }

  public List<KlonObject> getParameters() {
    return parameters;
  }

  public Message getCode() {
    return code;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("block(");
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
    result.append(code)
      .append(")");
    return result.toString();
  }

  @Override
  public Object clone() {
    return new Block(new ArrayList<KlonObject>(parameters),
      (Message) code.clone());
  }

}
