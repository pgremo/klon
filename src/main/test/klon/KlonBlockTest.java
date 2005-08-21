package klon;

import junit.framework.TestCase;

public class KlonBlockTest extends TestCase {

  public void testCreate() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("setter := block(a,self result := a)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonBlock);
    assertEquals("block(a, self setSlot(\"result\", a))", value.toString());

    message = compiler.fromString("setter(\"Hello\")");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("\"Hello\"", value.toString());
  }

  public void testIfTrue() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
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
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
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
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("a := 0; block(a < 10) whileTrue(a = a + 1)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals(object.getSlot("Nil"), value);
    assertEquals(new KlonNumber(10.0D), object.getSlot("a"));
  }

  public void testWhileFalse() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("a := 20; block(a < 10) whileFalse(a = a - 1)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals(object.getSlot("Nil"), value);
    assertEquals(new KlonNumber(9.0D), object.getSlot("a"));
  }
}
