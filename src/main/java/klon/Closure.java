package klon;

public abstract class Closure {

  protected KlonObject code;
  protected KlonObject result;

  public Closure(KlonObject result) {
    this.result = result;
  }

  public void setCode(KlonObject code) {
    this.code = code;
  }

  public KlonObject result() {
    return result;
  }

  public abstract boolean invoke(KlonObject current, KlonObject context)
      throws KlonObject;

}
