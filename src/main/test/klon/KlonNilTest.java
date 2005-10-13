package klon;

import junit.framework.TestCase;

public class KlonNilTest extends TestCase {

  private KlonObject root;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    root = new State(new String[0]).getRoot();
  }

  public void testAsString() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(root, "Nil asString");
    KlonObject value = KlonMessage.eval(message, root, root);
    assertEquals(KlonString.newString(root, ""), value);
  }

  public void testAnd() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(root,
      "Nil and Object");
    KlonObject value = KlonMessage.eval(message, root, root);
    assertEquals(KlonNil.newNil(root), value);
  }

  public void testIfNil() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(root,
      "Nil ifNil(Object)");
    KlonObject value = KlonMessage.eval(message, root, root);
    assertEquals(root.getSlot("Object"), value);
  }

  public void testIsNil() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(root, "Nil isNil");
    KlonObject value = KlonMessage.eval(message, root, root);
    assertEquals(root, value);
  }

  public void testIsEquals() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(root, "Nil == Nil");
    KlonObject value = KlonMessage.eval(message, root, root);
    assertEquals(root, value);

    message = KlonMessage.newMessageFromString(root, "Nil == Object");
    value = KlonMessage.eval(message, root, root);
    assertEquals(KlonNil.newNil(root), value);
  }

  public void testOr() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(root, "Nil or Nil");
    KlonObject value = KlonMessage.eval(message, root, root);
    assertEquals(KlonNil.newNil(root), value);

    message = KlonMessage.newMessageFromString(root, "Nil or Object");
    value = KlonMessage.eval(message, root, root);
    assertEquals(root.getSlot("Object"), value);
  }

  public void testDuplicate() throws Exception {
    assertSame(KlonNil.newNil(root), KlonNil.newNil(root)
      .clone());
  }
}
