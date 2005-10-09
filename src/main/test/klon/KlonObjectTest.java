package klon;

import junit.framework.TestCase;

public class KlonObjectTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new State(new String[0]).getRoot();
  }

  public void testSlotOperations() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "Account := 1");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("1", value.toString());
    message = KlonMessage.newMessageFromString(object, "Account");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("1", value.toString());
    message = KlonMessage.newMessageFromString(object,
      "removeSlot(\"Account\")");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(object, value);
  }

  public void testUpdateNonSlot() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "dummy = 1");
    try {
      KlonMessage.eval(message, object, object);
      fail("expected exception");
    } catch (KlonObject e) {
      assertEquals("Object.doesNotExist:dummy does not exist", e.getMessage());
    }
  }

  public void testForIncrement() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "total := \"\"; for(a,1,10,total = total + \" \" + a); total");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("\" 1 2 3 4 5 6 7 8 9 10\"", value.toString());
  }

  public void testForDecrement() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "total := \"\"; for(a,10,1,total = total + \" \" + a); total");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("\" 10 9 8 7 6 5 4 3 2 1\"", value.toString());
  }

  public void testForStep() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "total := \"\"; for(a,10,1,-2,total = total + \" \" + a); total");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("\" 10 8 6 4 2\"", value.toString());
  }

  public void testForEqual() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "total := \"\"; for(a,10,10,total = total + \" \" + a); total");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("\" 10\"", value.toString());
  }

  public void testAndNonNil() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "Object and Object");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testAndNil() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "Object and Nil");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testOrNonNil() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "Object or Object");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testOrNil() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "Object or Nil");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testWhile() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "total := 0; while(total < 10, total = total + 1)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("10", value.toString());
  }

  public void testIf() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "total := 0; if(total == 0, total = 10)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("10", value.toString());
    message = KlonMessage.newMessageFromString(object,
      "total := 10; if(total == 0, total = 10, total = 5)");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("5", value.toString());
  }

  public void testCondition() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "Thing := Object clone; Thing total := 10; Thing ?total");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("10", value.toString());

    message = KlonMessage.newMessageFromString(object, "Thing ?blah");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testGreaterThan() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "3 > 2");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);

    message = KlonMessage.newMessageFromString(object, "2 > 3");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testGreaterThanEquals() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "2 >= 2");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);

    message = KlonMessage.newMessageFromString(object, "3 >= 2");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);

    message = KlonMessage.newMessageFromString(object, "2 >= 3");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testIsEqual() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "2 == 3");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);

    message = KlonMessage.newMessageFromString(object, "2 == 2");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);
  }

  public void testLessThan() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "2 < 3");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 3D), value);

    message = KlonMessage.newMessageFromString(object, "3 < 2");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testLessThanEquals() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object, "2 <= 2");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 2D), value);

    message = KlonMessage.newMessageFromString(object, "2 <= 3");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNumber.newNumber(object, 3D), value);

    message = KlonMessage.newMessageFromString(object, "3 <= 2");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);
  }
}
