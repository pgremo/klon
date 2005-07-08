package klon;

import junit.framework.TestCase;
import klon.reflection.Up;

public class NilTest extends TestCase {

  public void testEquals() {
    assertTrue(Nil.equals(Lobby.Nil, Up.UP.up(null)) == Lobby.Nil);
  }

  public void testNotEquals() {
    assertTrue(Nil.equals(Lobby.Nil, Up.UP.up(Boolean.TRUE)) == Lobby.Nil);
  }

}
