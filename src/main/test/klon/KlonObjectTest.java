package klon;

import junit.framework.TestCase;

public class KlonObjectTest extends TestCase {

  public void testSlotOperations() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("Account := 1");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
    message = (Message) compiler.forString("Account");
    value = object.perform(object, message);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
    message = (Message) compiler.forString("slotNames");
    System.out.println(object.getSlot("Object").slotNames());
  }

  public void testFor() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler
        .forString("total := 0; for(a,1,10,total = a * 10)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("90", ((KlonNumber) value).toString());
  }

  public void testWhile() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler
        .forString("total := 0; while(total < 10, total = total + 1)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("10", ((KlonNumber) value).toString());
  }

  public void testIf() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler
        .forString("total := 0; if(total == 0, total = 10)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("10", ((KlonNumber) value).toString());
  }

}
