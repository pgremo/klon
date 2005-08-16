package klon;

public class Block extends KlonObject {

  private String[] parameters;
  private Message code;

  public Block(Message code) {
    this.code = code;
  }

  public void setParameters(String[] parameters) {
    this.parameters = parameters;
  }

  @Override
  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject scope = receiver.clone();
    int limit = Math.min(message.getArgumentCount(), parameters.length);
    for (int i = 0; i < limit; i++) {
      scope.setSlot(parameters[i], message.eval(receiver, i));
    }
    scope.setSlot("self", receiver);
    return code.eval(scope);
  }

}
