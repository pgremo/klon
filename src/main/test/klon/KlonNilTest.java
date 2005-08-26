package klon;

import junit.framework.TestCase;

public class KlonNilTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new KlonRoot(new String[0]);
    object.configure(object);
  }

  public void testAsString() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("Nil asString");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("String")
      .duplicate(""), value);
  }

  public void testAnd() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("Nil and Object");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);
  }

  public void testIfNil() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("Nil ifNil(Object)");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testIsNil() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("Nil isNil");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Klon"), value);
  }

  public void testIsEquals() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("Nil == Nil");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Klon"), value);

    message = compiler.fromString("Nil == Object");
    value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);
  }

  public void testOr() throws Exception {
    Compiler compiler = new Compiler(object);
    Message message = compiler.fromString("Nil or Nil");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Nil"), value);

    message = compiler.fromString("Nil or Object");
    value = message.eval(object, object);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testDuplicate() throws KlonException {
    assertSame(object.getSlot("Nil"), object.getSlot("Nil")
      .duplicate());
  }
}