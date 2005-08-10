package klon;

import klon.reflection.Up;

public class Lobby {

  public static final KlonObject Lobby;
  public static final KlonObject Nil;
  public static final KlonObject Object;

  static {
    Nil = Up.UP.up(new Nil());
    Object = Up.UP.up(new Object());
    Lobby = Up.UP.up(Lobby.class);
  }

  private Lobby() {
  }
}
