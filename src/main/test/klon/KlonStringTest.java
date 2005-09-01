package klon;

import junit.framework.TestCase;

public class KlonStringTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new KlonRoot(new String[0]);
    object.configure(object, KlonRoot.class);
  }

  public void testAdd() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("\"Hello\" + \" \" + \"World\"");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("\"Hello World\"", value.toString());
  }

  public void testIsEqual() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("\"Hello\" == \"World\"");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("\"Hello\" == \"Hello\"");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("\"Hello\"", value.toString());
  }

}
