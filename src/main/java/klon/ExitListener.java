package klon;

import java.io.Serializable;

public interface ExitListener extends Serializable {

  void onExit(State state, int result);
}
