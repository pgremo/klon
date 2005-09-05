package klon;

import java.io.File;

import junit.framework.TestCase;

public class KlonStoreTest extends TestCase {

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    new File("testdump").delete();
  }

  public void testDuplicate() throws Exception {
    KlonObject expected = new KlonObject();
    assertSame(expected, new KlonStore().duplicate(expected));
  }

  public void testOperations() throws Exception {
    KlonRoot.setup(new String[0]);
    KlonObject object = KlonRoot.getROOT();
    Compiler compiler = new Compiler(object);
    Message message = compiler
        .fromString("Account:=Object clone;Store path:=\"testdump\";Store store");
    KlonObject value = message.eval(object, object);
    assertEquals(object.getSlot("Store"), value);

    object = KlonRoot.getROOT();
    compiler = new Compiler(object);
    message = compiler.fromString("Store path:=\"testdump\";Store load");
    KlonObject savedValue = message.eval(object, object);
    assertNotNull(savedValue.getSlot("Account"));
  }

}
