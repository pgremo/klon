package klon.grammar.grammatica;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import junit.framework.TestCase;
import klon.Compiler;
import klon.KlonObject;

public class KlonParserTest extends TestCase {

  public void testParseNumber() throws Exception {
    String[][] expected = new String[][]{
        {"123.0", "123"},
        {"123.456", "123.456"},
        {"0.456", "0.456"},
        {".456", "0.456"},
        {"123e-2", "1.23"},
        {"123e2", "12300"},
        {"123.456e-2", "1.23456"},
        {"123.456e2", "12345.6"},
        {"0x0", "0"},
        {"0x0F", "15"},
        {"0XeE", "238"}};
    for (String[] current : expected) {
      KlonObject message = new Compiler().forString(current[0]);
      assertNotNull(message);
      assertEquals(current[1], message.toString());
    }
  }

  public void testSuperOperator() throws Exception {
    KlonObject message = new Compiler().forString("super(do stuff)");
    assertNotNull(message);
    assertEquals("super(do stuff)", message.toString());
  }

  public void testHelloWorld() throws Exception {
    KlonObject message = new Compiler().forString("\"Hello world!\\n\" print");
    assertNotNull(message);
    assertEquals("\"Hello world!\\n\" print", message.toString());
  }

  public void testCloneAssignment() throws Exception {
    KlonObject message = new Compiler().forString("Account := Object clone\nAccount balance := 0");
    assertNotNull(message);
    assertEquals(
      "setSlot(\"Account\", Object clone);\nAccount setSlot(\"balance\", 0)",
      message.toString());
  }

  public void testMessage() throws Exception {
    KlonObject message = new Compiler().forString("do(\"something\", 1)");
    assertNotNull(message);
    assertEquals("do(\"something\", 1)", message.toString());
  }

  public void testOperations() throws Exception {
    KlonObject message = new Compiler().forString("1 + 2 + 3");
    assertNotNull(message);
    assertEquals("1 +(2) +(3)", message.toString());
  }

  public void testBraceGrouping() throws Exception {
    KlonObject message = new Compiler().forString("{2 * 3}");
    assertNotNull(message);
    assertEquals("brace(2 *(3))", message.toString());
  }

  public void testBracketGrouping() throws Exception {
    KlonObject message = new Compiler().forString("[2 * 3]");
    assertNotNull(message);
    assertEquals("bracket(2 *(3))", message.toString());
  }

  public void testGroupingEnd() throws Exception {
    KlonObject message = new Compiler().forString("1 + (2 * 3)");
    assertNotNull(message);
    assertEquals("1 +(2 *(3))", message.toString());
  }

  public void testGroupingBegin() throws Exception {
    KlonObject message = new Compiler().forString("(1 + 2) * 3");
    assertNotNull(message);
    assertEquals("(1 +(2)) *(3)", message.toString());
  }

  public void testFactorial() throws Exception {
    File file = new File("sample/factorial.klon");
    char[] data = new char[(int) file.length()];
    Reader input = new FileReader(file);
    input.read(data);
    KlonObject message = new Compiler().forString(new String(data));
    assertNotNull(message);
  }

  public void test99bob() throws Exception {
    File file = new File("sample/99bob.klon");
    char[] data = new char[(int) file.length()];
    Reader input = new FileReader(file);
    input.read(data);
    KlonObject message = new Compiler().forString(new String(data));
    assertNotNull(message);
  }

  public void testAccount() throws Exception {
    File file = new File("sample/account.klon");
    char[] data = new char[(int) file.length()];
    Reader input = new FileReader(file);
    input.read(data);
    KlonObject message = new Compiler().forString(new String(data));
    assertNotNull(message);
  }

  public void testComments() throws Exception {
    File file = new File("sample/comments.klon");
    char[] data = new char[(int) file.length()];
    Reader input = new FileReader(file);
    input.read(data);
    KlonObject message = new Compiler().forString(new String(data));
    assertNotNull(message);
    assertEquals("setSlot(\"a\", b)", message.toString());
  }

}
