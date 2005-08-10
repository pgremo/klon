package klon.grammar.grammatica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import klon.Expression;
import klon.Message;
import klon.MessageChain;
import klon.NumberLiteral;
import klon.StringLiteral;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;

public class DefaultKlonAnalyzer extends KlonAnalyzer implements KlonConstants {

  private static final Map<String, String> SPECIAL_OPERATORS = new HashMap<String, String>();

  static {
    SPECIAL_OPERATORS.put(":=", "setSlot");
    SPECIAL_OPERATORS.put("=", "updateSlot");
  }

  @Override
  protected Node exitMessageChain(Production node) {
    Message[] messages = new Message[node.getChildCount()];
    for (int i = 0; i < node.getChildCount(); i++) {
      messages[i] = (Message) node.getChildAt(i)
        .getValue(0);
    }
    node.addValue(new MessageChain(messages));
    return node;
  }

  @Override
  protected Node exitMessage(Production node) {
    int child = 0;

    Token selector = (Token) node.getChildAt(child++);

    List<Expression> actualArgs = new ArrayList<Expression>();
    Production parsedArgs = (Production) node.getChildAt(child);
    if (parsedArgs != null && parsedArgs.getId() == ARGUMENTS) {
      actualArgs.addAll((List<Expression>) parsedArgs.getValue(0));
      child++;
    }

    Message actualAttached = null;
    Production parsedAtt = (Production) node.getChildAt(child);
    if (parsedAtt != null && parsedAtt.getId() == ATTACHED) {
      actualAttached = (Message) parsedAtt.getChildAt(0)
        .getValue(0);
      if (selector.getId() == OPERATOR) {
        actualArgs.add(actualAttached);
        actualAttached = null;
      }
    }

    // convert special operators, ie: x := thing to setSlot("x", thing)
    String selectorString = selector.getImage();
    if (selector.getId() == IDENTIFIER && actualAttached != null) {
      String newSelector = SPECIAL_OPERATORS.get(actualAttached.getSelector());
      if (newSelector != null) {
        actualArgs = actualAttached.getArguments();
        actualArgs.add(0, new StringLiteral(selectorString));
        selectorString = newSelector;
        actualAttached = null;
      }
    }

    node.addValue(new Message(selectorString, actualAttached, actualArgs));
    return node;
  }

  @Override
  protected Node exitArguments(Production node) {
    int count = node.getChildCount();
    List<Expression> values = new ArrayList<Expression>(count);
    for (int i = 0; i < count; i++) {
      values.add((Expression) node.getChildAt(i)
        .getValue(0));
    }
    node.addValue(values);
    return node;
  }

  @Override
  protected Node exitNumber(Token node) {
    node.addValue(new NumberLiteral(Double.parseDouble(node.getImage())));
    return node;
  }

  @Override
  protected Node exitString(Token node) {
    String value = node.getImage();
    node.addValue(new StringLiteral(value.substring(1, value.length() - 1)));
    return node;
  }

  @Override
  protected Node exitSymbol(Production node) {
    Node result = node;
    if (result != null && result.getChildCount() == 1) {
      result = result.getChildAt(0);
    }
    return result;
  }

  @Override
  protected Node exitTerminator(Token node) {
    return null;
  }

  @Override
  protected Node exitComma(Token node) {
    return null;
  }

  @Override
  protected Node exitLparen(Token node) {
    return null;
  }

  @Override
  protected Node exitRparen(Token node) {
    return null;
  }

  @Override
  protected Node exitRbrace(Token node) {
    return null;
  }

  @Override
  protected Node exitRbrack(Token node) {
    return null;
  }

}
