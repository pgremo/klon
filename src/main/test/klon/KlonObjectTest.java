package klon;

import junit.framework.TestCase;

public class KlonObjectTest extends TestCase {

  public void testSlotOperations() throws Exception {
    KlonObject object = new KlonObject();
    object.configure();
    KlonCompiler compiler = new KlonCompiler();
    KlonMessage message = (KlonMessage) compiler.forString("Account := 1");
    KlonObject value = object.send(message);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
    message = (KlonMessage) compiler.forString("Account");
    value = object.send(message);
    assertNotNull(value);
    assertTrue(value instanceof KlonNumber);
    assertEquals("1", ((KlonNumber) value).toString());
  }

}
