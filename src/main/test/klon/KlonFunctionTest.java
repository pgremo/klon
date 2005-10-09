package klon;

import junit.framework.TestCase;

public class KlonFunctionTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new State(new String[0]).getRoot();
  }

  public void testCreate() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "setter := function(a,self result := a)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("function(a, self setSlot(\"result\", a))", value.getData()
      .toString());

    message = object.getState()
      .getAsString();
    value = KlonMessage.eval(message, value, value);
    assertNotNull(value);
    assertEquals("function(a, self setSlot(\"result\", a))", value.getData()
      .toString());

    message = KlonMessage.newMessageFromString(object, "setter(\"Hello\")");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("Hello", value.getData());
  }

  public void testActivatePrototype() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "Function");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);

    message = KlonMessage.newMessageFromString(object, "asString");
    value = KlonMessage.eval(message, value, value);
    assertNotNull(value);
    assertEquals("null", value.getData());
  }

  public void testInvalid() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "setter := function(\"a\",self result := a)");
    try {
      KlonMessage.eval(message, object, object);
      fail("expected exception");
    } catch (KlonObject e) {
      assertEquals("Object.invalidArgument:argument must evaluate to a Symbol",
        e.getMessage());
    }
  }

  public void testInsuficientArguments() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "setter := function(a,b,self result := a + b)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("function(a, b, self setSlot(\"result\", a +(b)))",
      value.getData()
        .toString());

    try {
      message = KlonMessage.newMessageFromString(object, "setter(\"Hello\")");
      value = KlonMessage.eval(message, object, object);
      fail("should have caught an exception");
    } catch (KlonObject e) {
      assertEquals("Object.doesNotRespond:Receiver does not respond to 'b'",
        e.getMessage());
    }
  }

  public void testIfTrue() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "function(1 == 1) ifTrue(13)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("13", value.toString());

    message = KlonMessage.newMessageFromString(object,
      "function(1 == 12) ifTrue(13)");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testIfFalse() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "function(1 == 12) ifFalse(13)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("13", value.toString());

    message = KlonMessage.newMessageFromString(object,
      "function(1 == 1) ifFalse(13)");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testWhileTrue() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "a := 0; function(a < 10) whileTrue(a = a + 1)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);
    assertEquals(KlonNumber.newNumber(object, 10.0D), object.getSlot("a"));
  }

  public void testWhileFalse() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "a := 20; function(a < 10) whileFalse(a = a - 1)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);
    assertEquals(KlonNumber.newNumber(object, 9.0D), object.getSlot("a"));
  }
}
