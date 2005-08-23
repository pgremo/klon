package klon;

import junit.framework.TestCase;

public class KlonObjectTest extends TestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Klon.init(new String[0]);
  }

  public void testSlotOperations() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("Account := 1");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
    message = compiler.fromString("Account");
    value = object.perform(object, message);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
    message = compiler.fromString("removeSlot(\"Account\")");
    value = object.perform(object, message);
    assertNotNull(value);
    assertEquals(Klon.ROOT, value);

    message = compiler.fromString("slotNames");
    System.out.println(object.getSlot("Object").slotNames());
  }

  public void testUpdateNonSlot() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("dummy = 1");
    try {
      message.eval(object, object);
      fail("expected exception");
    } catch (KlonException e) {
      assertEquals("dummy does not exist", e.getMessage());
    }
  }

  public void testForIncrement() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("total := \"\"; for(a,1,10,total = total + \" \" + a)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(" 1 2 3 4 5 6 7 8 9 10", value.toString());
  }

  public void testForDecrement() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("total := \"\"; for(a,10,1,total = total + \" \" + a)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(" 10 9 8 7 6 5 4 3 2 1", value.toString());
  }

  public void testForStep() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("total := \"\"; for(a,10,1,-2,total = total + \" \" + a)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(" 10 8 6 4 2", value.toString());
  }

  public void testForEqual() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("total := \"\"; for(a,10,10,total = total + \" \" + a)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(" 10", value.toString());
  }

  public void testAndNonNil() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("Object and Object");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testAndNil() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("Object and Nil");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Nil"), value);
  }

  public void testOrNonNil() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("Object or Object");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testOrNil() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("Object or Nil");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testWhile() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("total := 0; while(total < 10, total = total + 1)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("10", ((KlonNumber) value).toString());
  }

  public void testIf() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("total := 0; if(total == 0, total = 10)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("10", ((KlonNumber) value).toString());
    message = compiler.fromString("total := 10; if(total == 0, total = 10, total = 5)");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("5", ((KlonNumber) value).toString());
  }

  public void testCondition() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler
        .fromString("Thing := Object clone; Thing total := 10; Thing ?total");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("10", ((KlonNumber) value).toString());

    message = compiler.fromString("Thing ?blah");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals(object.getSlot("Nil"), value);
  }
}
