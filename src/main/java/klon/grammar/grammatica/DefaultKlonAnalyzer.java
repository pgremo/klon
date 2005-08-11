package klon.grammar.grammatica;

import java.util.ArrayList;
import java.util.List;

import klon.Expression;
import klon.Message;
import klon.MessageChain;
import klon.NumberLiteral;
import klon.StringLiteral;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;

public class DefaultKlonAnalyzer extends KlonAnalyzer implements KlonConstants {

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
    Node result = node;
    if (result != null && result.getChildCount() == 1) {
      result = result.getChildAt(0);
    }
    return result;
  }

  @Override
  protected Node exitStandardMessage(Production node) {
    int child = 0;

    String selector = null;
    Node parsedSelector = node.getChildAt(child);
    if (parsedSelector instanceof Token) {
      selector = ((Token) parsedSelector).getImage();
      child++;
    }

    List<Expression> actualArgs = new ArrayList<Expression>();
    Node parsedArgs = node.getChildAt(child);
    if (parsedArgs != null && parsedArgs.getId() == ARGUMENTS) {
      if (selector == null) {
        selector = (String) parsedArgs.getValue(0);
      }
      actualArgs.addAll((List<Expression>) parsedArgs.getValue(1));
      child++;
    }

    Message actualAttached = null;
    Node parsedAtt = node.getChildAt(child);
    if (parsedAtt != null && parsedAtt.getId() == ATTACHED) {
      actualAttached = (Message) parsedAtt.getChildAt(0)
        .getValue(0);
      if (parsedSelector.getId() == OPERATOR) {
        actualArgs.add(actualAttached);
        Message newAttached = actualAttached.getAttached();
        actualAttached.setAttached(null);
        actualAttached = newAttached;
      }
    }

    node.addValue(new Message(selector, actualAttached, actualArgs));
    return node;
  }

  @Override
  protected Node exitSlotSet(Production node) {
    Token selector = (Token) node.getChildAt(0);
    Message attached = (Message) node.getChildAt(2)
      .getChildAt(0)
      .getValue(0);
    List<Expression> arguments = new ArrayList<Expression>();
    arguments.add(new StringLiteral(selector.getImage()));
    arguments.add(attached);
    node.addValue(new Message("setSlot", null, arguments));
    return node;
  }

  @Override
  protected Node exitSlotUpdate(Production node) {
    Token selector = (Token) node.getChildAt(0);
    Message attached = (Message) node.getChildAt(2)
      .getChildAt(0)
      .getValue(0);
    List<Expression> arguments = new ArrayList<Expression>();
    arguments.add(new StringLiteral(selector.getImage()));
    arguments.add(attached);
    node.addValue(new Message("updateSlot", null, arguments));
    return node;
  }

  @Override
  protected Node exitArguments(Production node) {
    Node parsedGroup = node.getChildAt(0);
    if (parsedGroup instanceof Token) {
      String selector = "";
      String group = ((Token) parsedGroup).getImage();
      if (group.equals("{")) {
        selector = "{}";
      } else if (group.equals("[")) {
        selector = "[]";
      }
      node.addValue(selector);
    }
    int count = node.getChildCount();
    List<Expression> values = new ArrayList<Expression>(count);
    for (int i = 1; i < count; i++) {
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
