package klon;

public class Detect extends Closure {

  public Detect(KlonObject result) {
    super(result);
  }

  @Override
  public boolean invoke(KlonObject current, KlonObject context)
      throws KlonObject {
    result = KlonMessage.eval(code, context, context);
    return !KlonNil.newNil(result)
      .equals(result);
  }

}
