package klon;

import klon.reflection.ExposedAs;

public class Nil {

  @ExposedAs("==")
  public static KlonObject equals(KlonObject receiver, KlonObject arg) {
    return receiver == arg ? Lobby.Lobby : Lobby.Nil;
  }

}
