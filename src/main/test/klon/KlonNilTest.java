package klon;

import junit.framework.TestCase;

public class KlonNilTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new State(new String[0]).getRoot();
  }

  public void testAsString() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "Nil asString");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonString.newString(object, ""), value);
  }

  public void testAnd() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "Nil and Object");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testIfNil() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object,
      "Nil ifNil(Object)");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testIsNil() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "Nil isNil");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(object.getSlot("Klon"), value);
  }

  public void testIsEquals() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "Nil == Nil");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(object.getSlot("Klon"), value);

    message = KlonMessage.newMessageFromString(object, "Nil == Object");
    value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);
  }

  public void testOr() throws Exception {
    KlonMessage message = KlonMessage.newMessageFromString(object, "Nil or Nil");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(KlonNil.newNil(object), value);

    message = KlonMessage.newMessageFromString(object, "Nil or Object");
    value = KlonMessage.eval(message, object, object);
    assertEquals(object.getSlot("Object"), value);
  }

  public void testDuplicate() throws Exception {
    assertSame(KlonNil.newNil(object), KlonNil.newNil(object)
      .clone());
  }
}
