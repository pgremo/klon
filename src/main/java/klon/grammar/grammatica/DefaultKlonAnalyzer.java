package klon.grammar.grammatica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import klon.IdentifierLiteral;
import klon.KlonObject;
import klon.KlonMessage;
import klon.KlonNumber;
import klon.OperatorLiteral;
import klon.KlonString;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;

public class DefaultKlonAnalyzer extends KlonAnalyzer implements KlonConstants {

  @Override
  protected void childMessageChain(Production node, Node child) {
    KlonMessage next = (KlonMessage) child.getValue(0);
    if (node.getValueCount() == 0) {
      node.addValue(next);
    } else {
      KlonMessage current = (KlonMessage) node.getValue(node.getValueCount() - 1);
      current.setNext(next);
    }
    node.addValue(next);
  }

  @Override
  protected void childArguments(Production node, Node child) {
    if (child != null) {
      node.addValue(child.getValue(0));
    }
  }

  @Override
  protected Node exitMessage(Production node) {
    return compressPath(node);
  }

  @Override
  protected Node exitStandardMessage(Production node) {
    int child = 0;

    Node current = node.getChildAt(child++);
    KlonObject selector = (KlonObject) current.getValue(0);
    if (current instanceof Token) {
      current = node.getChildAt(child++);
    }

    List<KlonMessage> actualArgs = Collections.emptyList();
    if (current != null && current.getId() == ARGUMENTS) {
      ArrayList allValues = current.getAllValues();
      actualArgs = allValues.subList(1, allValues.size());
      current = node.getChildAt(child);
    }

    KlonMessage actualAttached = null;
    if (current != null && current.getId() == ATTACHED) {
      actualAttached = (KlonMessage) current.getChildAt(0)
        .getValue(0);
      if (selector instanceof OperatorLiteral) {
        actualArgs = Collections.singletonList(actualAttached);
        KlonMessage newAttached = actualAttached.getAttached();
        actualAttached.setAttached(null);
        actualAttached = newAttached;
      }
    }

    node.addValue(new KlonMessage(selector, actualAttached, actualArgs));
    return node;
  }

  @Override
  protected Node exitSlotSet(Production node) {
    createSlotOperation(node, new IdentifierLiteral("setSlot"));
    return node;
  }

  @Override
  protected Node exitSlotUpdate(Production node) {
    createSlotOperation(node, new IdentifierLiteral("updateSlot"));
    return node;
  }

  @Override
  protected Node exitSymbol(Production node) {
    return compressPath(node);
  }

  @Override
  protected Node exitIdentifier(Token node) {
    node.addValue(new IdentifierLiteral(node.getImage()));
    return node;
  }

  @Override
  protected Node exitOperator(Token node) {
    node.addValue(new OperatorLiteral(node.getImage()));
    return node;
  }

  @Override
  protected Node exitDecimalNumber(Token node) {
    node.addValue(new KlonNumber(Double.parseDouble(node.getImage())));
    return node;
  }

  @Override
  protected Node exitHexNumber(Token node) {
    node.addValue(new KlonNumber(Integer.parseInt(node.getImage()
      .substring(2), 16)));
    return node;
  }

  @Override
  protected Node exitString(Token node) {
    String value = node.getImage();
    node.addValue(new KlonString(value.substring(1, value.length() - 1)));
    return node;
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
    node.addValue(new IdentifierLiteral(""));
    return node;
  }

  @Override
  protected Node exitLbrace(Token node) {
    node.addValue(new IdentifierLiteral("brace"));
    return node;
  }

  @Override
  protected Node exitLbrack(Token node) {
    node.addValue(new IdentifierLiteral("bracket"));
    return node;
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

  private void createSlotOperation(Production node, KlonObject selector) {
    KlonString slotName = new KlonString(
      ((Token) node.getChildAt(0)).getImage());
    KlonMessage attached = (KlonMessage) node.getChildAt(2)
      .getChildAt(0)
      .getValue(0);
    List<KlonMessage> arguments = Arrays.asList(new KlonMessage(slotName, null,
      new ArrayList<KlonMessage>()), attached);
    node.addValue(new KlonMessage(selector, null, arguments));
  }

  private Node compressPath(Production node) {
    return node.getChildCount() == 1 ? node.getChildAt(0) : node;
  }

}
