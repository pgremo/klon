package klon;

import java.io.Serializable;

public class Block implements Serializable {

  private static final long serialVersionUID = 8908253496842815003L;
  private String[] parameters;
  private Message code;

  public Block(String[] parameters, Message code) {
    this.parameters = parameters;
    this.code = code;
  }

  public String[] getParameters() {
    return parameters;
  }

  public Message getCode() {
    return code;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("block(");
    for (int i = 0; i < parameters.length; i++) {
      if (i > 0) {
        result.append(", ");
      }
      result.append(parameters[i]);
    }
    if (parameters.length > 0) {
      result.append(", ");
    }
    result.append(code).append(")");
    return result.toString();
  }

}
