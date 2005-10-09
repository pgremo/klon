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
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "\"Hello\" + \" \" + \"World\"");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("\"Hello World\"", value.toString());
  }

  public void testIsEqual() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "\"Hello\" == \"World\"");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals(KlonNil.newNil(object), value);

    message = KlonMessage.newMessageFromString(object, "\"Hello\" == \"Hello\"");
    value = KlonMessage.eval(message, object, object);
    assertNotNull(value);
    assertEquals("\"Hello\"", value.toString());
  }

}
