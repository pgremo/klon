package klon.grammar.grammatica;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.Parser;

public class KlonParserTest extends TestCase {

  public void testHelloWorld() throws Exception {
    Reader input = new StringReader("\"Hello world!\\n\" print");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
    assertEquals("\"Hello world!\\n\" print", actual.getValue(0)
      .toString());
  }

  public void testCloneAssignment() throws Exception {
    Reader input = new StringReader(
      "Account := Object clone\nAccount balance := 0");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
    assertEquals(
      "setSlot(\"Account\", Object clone);\nAccount setSlot(\"balance\", 0)",
      actual.getValue(0)
        .toString());
  }

  public void testMessage() throws Exception {
    Reader input = new StringReader("do(\"something\", 1)");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
    assertEquals("do(\"something\", 1)", actual.getValue(0)
      .toString());
  }

  public void testOperations() throws Exception {
    Reader input = new StringReader("1 + 2 + 3");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
    assertEquals("1 +(2) +(3)", actual.getValue(0)
      .toString());
  }

  public void testGroupingEnd() throws Exception {
    Reader input = new StringReader("1 + (2 * 3)");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
    assertEquals("1 +(2 *(3))", actual.getValue(0)
      .toString());
  }

  public void testGroupingBegin() throws Exception {
    Reader input = new StringReader("(1 + 2) * 3");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
    assertEquals("(1 +(2)) *(3)", actual.getValue(0)
      .toString());
  }

  public void testFactorial() throws Exception {
    Reader input = new FileReader("sample/factorial.klon");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
  }

  public void test99bob() throws Exception {
    Reader input = new FileReader("sample/99bob.klon");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
  }

  public void testAccount() throws Exception {
    Reader input = new FileReader("sample/account.klon");
    Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
    Node actual = parser.parse();
    assertNotNull(actual);
  }

}
