package klon;

import java.io.Serializable;

public interface ExceptionListener extends Serializable {

  void onException(State state, KlonObject exception);
}
