package klon;

import junit.framework.TestCase;

public class KlonNumberTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new State(new String[0]).getRoot();
  }

  public void testAdd() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "1 + 1");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

  public void testSubtract() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "5 - 3");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

  public void testMultiply() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "7 * 3");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 21D), value);
  }

  public void testDivide() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "48 / 6");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 8D), value);
  }

  public void testModulus() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "1 % 4");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 1D), value);
  }

  public void testPower() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "2 power(3)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 8D), value);
  }

  public void testAbsoluteValue() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "2 abs");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);

    message = KlonMessage.newMessageFromString(object, "-2 abs");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

  public void testSquareRoot() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "4 sqrt");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

}
