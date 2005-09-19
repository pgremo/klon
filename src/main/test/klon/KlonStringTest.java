package klon;

import junit.framework.TestCase;

public class KlonStringTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new State(new String[0]).getRoot();
  }

  public void testAdd() throws Exception {
    Compiler compiler = new Compiler(object);
    KlonMessage message = compiler.fromString("\"Hello\" + \" \" + \"World\"");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals("\"Hello World\"", value.toString());
  }

  public void testIsEqual() throws Exception {
    Compiler compiler = new Compiler(object);
    KlonMessage message = compiler.fromString("\"Hello\" == \"World\"");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);

    message = compiler.fromString("\"Hello\" == \"Hello\"");
    value = message.eval(object, object);
    assertNotNull(value);
    assertEquals("\"Hello\"", value.toString());
  }

}
