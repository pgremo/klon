package klon.reflection;

import junit.framework.TestCase;
import klon.KlonObject;

public class UpTest extends TestCase {

  public void testClone() {
    KlonObject expected = Up.UP.up(new KlonObject());
    KlonObject actual = KlonObject.clone(expected);
  }

}
