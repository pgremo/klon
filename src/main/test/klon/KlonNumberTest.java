package klon;

import junit.framework.TestCase;

public class KlonNumberTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new KlonRoot(new String[0]);
    object.configure(object);
  }

  public void testAdd() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("1 + 1");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);
  }

  public void testSubtract() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("5 - 3");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);
  }

  public void testMultiply() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("7 * 3");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(21D), value);
  }

  public void testDivide() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("48 / 6");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(8D), value);
  }

  public void testModulus() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("1 % 4");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(1D), value);
  }

  public void testPower() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("2 ^ 3");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(8D), value);
  }

  public void testIsEqual() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("2 == 3");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("2 == 2");
    value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);
  }

  public void testLessThan() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("2 < 3");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(3D), value);

    message = compiler.fromString("3 < 2");
    value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("3 < \"2\"");
    try {
      value = message.eval(object, object);
      fail("expected exception");
    } catch (KlonException e) {
      assertEquals("Illegal Argument:Illegal Argument for <", e.getMessage());
    }
  }

  public void testGreaterThan() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("3 > 2");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);

    message = compiler.fromString("2 > 3");
    value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("3 > \"2\"");
    try {
      value = message.eval(object, object);
      fail("expected exception");
    } catch (KlonException e) {
      assertEquals("Illegal Argument:Illegal Argument for >", e.getMessage());
    }
  }

  public void testLessThanEquals() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("2 <= 2");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);

    message = compiler.fromString("2 <= 3");
    value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(3D), value);

    message = compiler.fromString("3 <= 2");
    value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("3 <= \"2\"");
    try {
      value = message.eval(object, object);
      fail("expected exception");
    } catch (KlonException e) {
      assertEquals("Illegal Argument:Illegal Argument for <=", e.getMessage());
    }
  }

  public void testGreaterThanEquals() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("2 >= 2");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);

    message = compiler.fromString("3 >= 2");
    value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);

    message = compiler.fromString("2 >= 3");
    value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("3 >= \"2\"");
    try {
      value = message.eval(object, object);
      fail("expected exception");
    } catch (KlonException e) {
      assertEquals("Illegal Argument:Illegal Argument for >=", e.getMessage());
    }
  }

  public void testAbsoluteValue() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("2 abs");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);

    message = compiler.fromString("-2 abs");
    value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);
  }

  public void testSquareRoot() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("4 sqrt");
    KlonObject value = message.eval(object, object);
    assertEquals(((KlonNumber) object.getSlot("Number")).newNumber(2D), value);
  }

}
