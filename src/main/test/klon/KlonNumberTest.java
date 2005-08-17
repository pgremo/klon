package klon;

import junit.framework.TestCase;

public class KlonNumberTest extends TestCase {

  /*
   * Test method for 'klon.KlonNumber.add(KlonObject, Message)'
   */
  public void testAdd() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("1 + 1");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.subtract(KlonObject, Message)'
   */
  public void testSubtract() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("5 - 3");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.multiply(KlonObject, Message)'
   */
  public void testMultiply() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("7 * 3");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("21", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.divide(KlonObject, Message)'
   */
  public void testDivide() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("48 / 6");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("8", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.power(KlonObject, Message)'
   */
  public void testPower() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("2 ^ 3");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("8", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.isEqual(KlonObject, Message)'
   */
  public void testIsEqual() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("2 == 3");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals("Nil", value.toString());

    message = (Message) compiler.forString("2 == 2");
    value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.lessThan(KlonObject, Message)'
   */
  public void testLessThan() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("2 < 3");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("3", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.greaterThan(KlonObject, Message)'
   */
  public void testGreaterThan() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("3 > 2");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.lessThanEquals(KlonObject, Message)'
   */
  public void testLessThanEquals() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("2 <= 2");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", ((KlonNumber) value).toString());

    message = (Message) compiler.forString("2 <= 3");
    value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("3", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.greaterThanEquals(KlonObject, Message)'
   */
  public void testGreaterThanEquals() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("2 >= 2");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());

    message = (Message) compiler.forString("3 >= 2");
    value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.absoluteValue(KlonObject, Message)'
   */
  public void testAbsoluteValue() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("2 abs");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());

    message = (Message) compiler.forString("-2 abs");
    value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.squareRoot(KlonObject, Message)'
   */
  public void testSquareRoot() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("4 sqrt");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("2", value.toString());
  }

}
