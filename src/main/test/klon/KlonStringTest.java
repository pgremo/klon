package klon;

import junit.framework.TestCase;

public class KlonStringTest extends TestCase {

  /*
   * Test method for 'klon.KlonString.add(KlonObject, Message)'
   */
  public void testAdd() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("\"Hello\" + \" \" + \"World\"");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("\"Hello World\"", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.isEqual(KlonObject, Message)'
   */
  public void testIsEqual() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("\"Hello\" == \"World\"");
    KlonObject value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonNil);
    assertEquals("Nil", value.toString());

    message = (Message) compiler.forString("\"Hello\" == \"Hello\"");
    value = message.eval(object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("\"Hello\"", value.toString());
  }

}
