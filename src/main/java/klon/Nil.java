package klon;

import klon.reflection.ExposedAs;

public class Nil {

  @ExposedAs("==")
  public KlonObject equals(KlonObject locals, Message message) {
    return equals(message.getArguments()
      .get(0)) ? Lobby.Lobby : Lobby.Nil;
  }

}
