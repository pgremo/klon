package klon;

import java.io.Serializable;

public interface WriteListener extends Serializable {

  void onWrite(State state, String value);
}
