package klon;

import klon.reflection.ExposedAs;

public class Nil extends KlonObject {

  @Override
  public KlonObject activate(KlonObject receiver, Message message) {
    return Lobby.Nil;
  }

  @ExposedAs("==")
  public KlonObject equals(KlonObject locals, Message message) {
    return equals(message.getArguments()
      .get(0)) ? Lobby.Lobby : Lobby.Nil;
  }

}
