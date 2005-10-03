package klon;

public class KlonFuture extends KlonObject {

  private static final long serialVersionUID = 1L;

  public KlonFuture() {

  }

  public KlonFuture(State state) {
    super(state);
    type = "Future";
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonFuture(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

}
