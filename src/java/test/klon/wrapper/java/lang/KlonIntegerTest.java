package klon.wrapper.java.lang;

import junit.framework.TestCase;
import klon.KlonException;
import klon.KlonObject;
import klon.reflection.Up;

public class KlonIntegerTest extends TestCase {

  public void testAdd() throws KlonException {
    KlonObject right = Up.UP.up(1);
    KlonObject left = Up.UP.up(1);
    assertEquals(new Integer(2), KlonInteger.add(right, left)
      .down());
  }

  public void testSubtract() throws KlonException {
    KlonObject right = Up.UP.up(1);
    KlonObject left = Up.UP.up(1);
    assertEquals(new Integer(0), KlonInteger.subtract(right, left)
      .down());
  }

  public void testMultiply() throws KlonException {
    KlonObject right = Up.UP.up(2);
    KlonObject left = Up.UP.up(3);
    assertEquals(new Integer(6), KlonInteger.multiply(right, left)
      .down());
  }

  public void testDivide() throws KlonException {
    KlonObject right = Up.UP.up(8);
    KlonObject left = Up.UP.up(2);
    assertEquals(new Integer(4), KlonInteger.divide(right, left)
      .down());
  }
}
