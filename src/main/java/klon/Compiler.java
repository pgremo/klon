package klon;

import java.io.Reader;
import java.io.StringReader;

import klon.grammar.grammatica.DefaultKlonAnalyzer;
import klon.grammar.grammatica.KlonParser;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.Parser;

public class Compiler {

  public KlonObject forString(String value) throws KlonException {
    try {
      Reader input = new StringReader(value);
      Parser parser = new KlonParser(input, new DefaultKlonAnalyzer());
      Node actual = parser.parse();
      return (KlonObject) actual.getValue(0);
    } catch (Exception e) {
      throw new KlonException(e);
    }
  }

}
