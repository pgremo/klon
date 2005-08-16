package klon;

public class KlonBlock extends KlonObject {

  private String[] parameters;
  private KlonMessage code;

  public KlonBlock(String[] parameters, KlonMessage code) {
    this.parameters = parameters;
    this.code = code;
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonMessage message)
      throws KlonException {
    KlonObject scope = receiver.clone();
    int limit = Math.min(message.getArgumentCount(), parameters.length);
    for (int i = 0; i < limit; i++) {
      scope.setSlot(parameters[i], message.eval(receiver, i));
    }
    return code.eval(scope);
  }

}
