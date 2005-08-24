package klon;

import junit.framework.TestCase;

public class KlonBlockTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new KlonRoot(new String[0]);
    object.configure(object);
  }

  public void testCreate() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("setter := block(a,self result := a)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonBlock);
    assertEquals("block(a, self setSlot(\"result\", a))", value.toString());

    message = compiler.fromString("asString");
    value = message.eval(value, value);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("block(a, self setSlot(\"result\", a))", value.toString());

    message = compiler.fromString("setter(\"Hello\")");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("Hello", value.toString());
  }

  public void testActivatePrototype() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("Block");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonBlock);

    message = compiler.fromString("asString");
    value = message.eval(value, value);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("null", value.toString());
  }

  public void testInvalid() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("setter := block(\"a\",self result := a)");
    try {
      message.eval(object, object);
      fail("expected exception");
    } catch (KlonException e) {
      assertEquals("null must be a Symbol", e.getMessage());
    }
  }

  public void testInsuficientArguments() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("setter := block(a,b,self result := a + b)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonBlock);
    assertEquals("block(a, b, self setSlot(\"result\", a +(b)))",
      value.toString());

    message = compiler.fromString("setter(\"Hello\")");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("Hello", value.toString());
  }

  public void testIfTrue() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("block(1 == 1) ifTrue(13)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("13", ((KlonNumber) value).toString());

    message = compiler.fromString("block(1 == 12) ifTrue(13)");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals(object.getSlot("Nil"), value);
  }

  public void testIfFalse() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("block(1 == 12) ifFalse(13)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("13", ((KlonNumber) value).toString());

    message = compiler.fromString("block(1 == 1) ifFalse(13)");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals(object.getSlot("Nil"), value);
  }

  public void testWhileTrue() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("a := 0; block(a < 10) whileTrue(a = a + 1)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals(object.getSlot("Nil"), value);
    assertEquals(object.getSlot("Number")
      .duplicate(10.0D), object.getSlot("a"));
  }

  public void testWhileFalse() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("a := 20; block(a < 10) whileFalse(a = a - 1)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals(object.getSlot("Nil"), value);
    assertEquals(object.getSlot("Number")
      .duplicate(9.0D), object.getSlot("a"));
  }
}
