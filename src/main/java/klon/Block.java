package klon;

public class Block {

  private String[] parameters;
  private Message code;

  public Block(String[] parameters, Message code) {
    this.parameters = parameters;
    this.code = code;
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

  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject scope = receiver.clone();
    int limit = Math.min(message.getArgumentCount(), parameters.length);
    int i = 0;
    for (; i < limit; i++) {
      scope.setSlot(parameters[i], message.eval(receiver, i));
    }
    KlonObject nil = Klon.ROOT.getSlot("Nil");
    for (; i < parameters.length; i++) {
      scope.setSlot(parameters[i], nil);
    }
    scope.setSlot("self", receiver);
    return code.eval(scope);
  }

}
