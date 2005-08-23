package klon;

import junit.framework.TestCase;

public class KlonStringTest extends TestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Klon.init(new String[0]);
  }

  /*
   * Test method for 'klon.KlonString.add(KlonObject, Message)'
   */
  public void testAdd() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("\"Hello\" + \" \" + \"World\"");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("Hello World", value.toString());
  }

  /*
   * Test method for 'klon.KlonNumber.isEqual(KlonObject, Message)'
   */
  public void testIsEqual() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = compiler.fromString("\"Hello\" == \"World\"");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("\"Hello\" == \"Hello\"");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("Hello", value.toString());
  }

}
