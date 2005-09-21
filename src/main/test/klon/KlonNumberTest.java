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
    KlonMessage message = KlonMessage.newMessageFromString(object, "1 + 1");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

  public void testSubtract() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "5 - 3");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

  public void testMultiply() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "7 * 3");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 21D), value);
  }

  public void testDivide() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "48 / 6");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 8D), value);
  }

  public void testModulus() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "1 % 4");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 1D), value);
  }

  public void testPower() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "2 power(3)");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 8D), value);
  }

  public void testAbsoluteValue() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "2 abs");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);

    message = KlonMessage.newMessageFromString(object, "-2 abs");
    value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

  public void testSquareRoot() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "4 sqrt");
    KlonObject value = message.eval(object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

}
