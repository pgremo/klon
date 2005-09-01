package klon;

import java.util.List;

public class Block {

  private String[] parameters;
  private Message code;

  public Block(String[] parameters, Message code) {
    this.parameters = parameters;
    this.code = code;
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
    result.append(code)
      .append(")");
    return result.toString();
  }

  @SuppressWarnings("unchecked")
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject scope = KlonLocals.newLocals(receiver, receiver);
    int limit = Math.min(message.getArgumentCount(), parameters.length);
    int i = 0;
    for (; i < limit; i++) {
      scope.setSlot(parameters[i], message.eval(context, i));
    }
    KlonObject nil = receiver.getSlot("Nil");
    for (; i < parameters.length; i++) {
      scope.setSlot(parameters[i], nil);
    }
    KlonObject result = nil;
    try {
      result = code.eval(scope, scope);
    } catch (KlonObject e) {
      ((List<KlonObject>) e.getSlot("stackTrace")
        .getData()).add(KlonString.newString(receiver, message.toString()));
      throw e;
    }
    return result;
  }
}
