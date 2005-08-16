package klon;

import junit.framework.TestCase;

public class KlonObjectTest extends TestCase {

  public void testSlotOperations() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("Account := 1");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
    message = (Message) compiler.forString("Account");
    value = object.send(message);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
    message = (Message) compiler.forString("slotNames");
    System.out.println(object.send(message));
  }

}
