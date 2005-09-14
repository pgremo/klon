package klon;

public interface ExceptionListener {

  void onException(State state, KlonObject exception);
}
