package klon;

import junit.framework.TestCase;
import klon.reflection.Up;

public class KlonObjectTest extends TestCase {

//  public void testPerform() throws KlonException {
//    KlonObject target = Up.UP.up(Boolean.TRUE);
//    KlonObject result = KlonObject.perform(target, Up.UP.up(new Message(
//      "booleanValue", new KlonObject[0])));
//    assertTrue(KlonObject.equals(target, target) == target);
//    assertTrue(KlonObject.equals(target, result) == Lobby.Nil);
//  }

  public void testConstructor() throws Exception {
    KlonObject expected = new KlonObject();
  }

}
