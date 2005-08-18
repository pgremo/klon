package klon;

import junit.framework.TestCase;

public class KlonBlockTest extends TestCase {

  public void testCreate() throws Exception {
    KlonObject object = Klon.ROOT;
    Compiler compiler = new Compiler();
    Message message = (Message) compiler.forString("setter := block(a,self result := a)");
    KlonObject value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonBlock);
    assertEquals("block(a, self setSlot(\"result\", a))", value.toString());

    message = (Message) compiler.forString("setter(\"Hello\")");
    value = message.eval(object, object);
    assertNotNull(value);
    assertTrue(value instanceof KlonString);
    assertEquals("\"Hello\"", value.toString());
  }

}
