package klon;

import junit.framework.TestCase;

public class KlonNumberTest extends TestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Klon.init(new String[0]);
  }

  /*
   * Test method for 'klon.KlonNumber.add(KlonObject, Message)'
   */
  public void testAdd() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("1 + 1");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.subtract(KlonObject, Message)'
   */
  public void testSubtract() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("5 - 3");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.multiply(KlonObject, Message)'
   */
  public void testMultiply() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("7 * 3");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(21D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.divide(KlonObject, Message)'
   */
  public void testDivide() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("48 / 6");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(8D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.power(KlonObject, Message)'
   */
  public void testPower() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("2 ^ 3");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(8D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.isEqual(KlonObject, Message)'
   */
  public void testIsEqual() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("2 == 3");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("2 == 2");
    value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.lessThan(KlonObject, Message)'
   */
  public void testLessThan() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("2 < 3");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(3D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.greaterThan(KlonObject, Message)'
   */
  public void testGreaterThan() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("3 > 2");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.lessThanEquals(KlonObject, Message)'
   */
  public void testLessThanEquals() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("2 <= 2");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), ((KlonNumber) value));

    message = compiler.fromString("2 <= 3");
    value = message.eval(object, object);
    assertEquals(new KlonNumber(3D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.greaterThanEquals(KlonObject, Message)'
   */
  public void testGreaterThanEquals() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("2 >= 2");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);

    message = compiler.fromString("3 >= 2");
    value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.absoluteValue(KlonObject, Message)'
   */
  public void testAbsoluteValue() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("2 abs");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);

    message = compiler.fromString("-2 abs");
    value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);
  }

  /*
   * Test method for 'klon.KlonNumber.squareRoot(KlonObject, Message)'
   */
  public void testSquareRoot() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("4 sqrt");
    KlonObject value = message.eval(object, object);
    assertEquals(new KlonNumber(2D), value);
  }

}
