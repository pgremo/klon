package klon;

import klon.reflection.ExposedAs;

public class KlonNil extends KlonObject {

  @ExposedAs("==")
  public KlonObject equals(KlonObject locals, KlonMessage message) {
    return equals(message.getArguments()
      .get(0)) ? Lobby.Lobby : Lobby.Nil;
  }

}
