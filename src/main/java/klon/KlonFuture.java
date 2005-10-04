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
    KlonObject result = new KlonFuture(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

}
