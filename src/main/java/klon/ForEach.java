package klon;

public class ForEach extends Closure {

  public ForEach(KlonObject result) {
    super(result);
  }

  @Override
  public boolean invoke(KlonObject current, KlonObject context)
      throws KlonObject {
    result = KlonMessage.eval(code, context, context);
    return false;
  }

}
