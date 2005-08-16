package klon;

import klon.reflection.ExposedAs;

public class KlonNil extends KlonObject {

  @Override
  public KlonObject activate(KlonObject receiver, KlonMessage message) {
    return Lobby.Nil;
  }

  @ExposedAs("==")
  public KlonObject equals(KlonObject locals, KlonMessage message) {
    return equals(message.getArguments()
      .get(0)) ? Lobby.Lobby : Lobby.Nil;
  }

}
