package klon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Function implements Serializable, Cloneable {

  private static final long serialVersionUID = 8908253496842815003L;
  private List<KlonObject> parameters;
  private KlonMessage message;
  private KlonObject scope;

  public Function(List<KlonObject> parameters, KlonMessage message) {
    this.parameters = parameters;
    this.message = message;
  }

  public List<KlonObject> getParameters() {
    return parameters;
  }

  public KlonMessage getMessage() {
    return message;
  }

  public void setMessage(KlonMessage message) {
    this.message = message;
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
      result.append("function");
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
    result.append(message.getData())
      .append(")");
    return result.toString();
  }

  @Override
  public Object clone() {
    Function result = new Function(new ArrayList<KlonObject>(parameters),
      (KlonMessage) message.clone());
    result.setScope(scope);
    return result;
  }

}
