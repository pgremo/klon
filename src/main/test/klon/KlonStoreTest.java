package klon;

import java.io.File;

import junit.framework.TestCase;

public class KlonStoreTest extends TestCase {

  private KlonObject object;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    object = new State(new String[0]).getRoot();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    new File("testdump").delete();
  }

  public void testDuplicate() throws Exception {
    KlonObject expected = new KlonStore(object.getState());
    assertSame(expected, expected.clone());
  }

  public void testOperations() throws Exception {
    KlonObject message = KlonMessage.newMessageFromString(object,
      "Account:=Object clone;Store path:=\"testdump\";Store store");
    KlonObject value = KlonMessage.eval(message, object, object);
    assertEquals(object.getSlot("Store"), value);

    object = object.getState()
      .getRoot();
    message = KlonMessage.newMessageFromString(object,
      "Store path:=\"testdump\";Store load");
    KlonObject savedValue = KlonMessage.eval(message, object, object);
    assertNotNull(savedValue.getSlot("Account"));
  }

}
