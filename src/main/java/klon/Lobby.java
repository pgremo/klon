package klon;

public final class Lobby extends KlonObject {

  public static final KlonObject Lobby;
  public static final KlonObject Nil;
  public static final KlonObject Object;

  static {
    Nil = new Nil();
    Object = new KlonObject();
    Lobby = new Lobby();
  }

  private Lobby() {
  }
}
