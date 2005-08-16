package klon.grammar.grammatica;

import java.util.ArrayList;
import java.util.List;

import klon.KlonMessage;
import klon.KlonNumber;
import klon.KlonObject;
import klon.KlonString;
import klon.KlonSymbol;
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
  protected void childStandardMessage(Production node, Node child) {
    KlonMessage message = (KlonMessage) node.getValue(0);
    if (message == null) {
      message = new KlonMessage();
      node.addValue(message);
      node.addValue(child.getValue(1));
      Object subject = child.getValue(0);
      if (subject instanceof KlonSymbol) {
        message.setSelector((KlonSymbol) subject);
      } else {
        message.setLiteral((KlonObject) subject);
      }
    }

    if (child.getId() == GROUP) {
      for (KlonMessage arg : (Iterable<KlonMessage>) child.getValue(2)) {
        message.addArgument(arg);
      }
    }

    if (child.getId() == ATTACHED) {
      KlonMessage attached = (KlonMessage) child.getChildAt(0)
        .getValue(0);
      if ((Integer) node.getValue(1) == OPERATOR) {
        message.addArgument(attached);
        KlonMessage newAttached = attached.getAttached();
        attached.setAttached(null);
        attached = newAttached;
      }
      message.setAttached(attached);
    }
  }

  @Override
  protected void childGroup(Production node, Node child) {
    if (child != null) {
      if (node.getValueCount() == 0) {
        node.addValue(child.getValue(0));
        node.addValue(child.getValue(1));
        node.addValue(new ArrayList<KlonMessage>());
      } else {
        List<KlonMessage> group = (List<KlonMessage>) node.getValue(2);
        group.add((KlonMessage) child.getValue(0));
      }
    }
  }

  @Override
  protected Node exitMessage(Production node) {
    return compressPath(node);
  }

  @Override
  protected Node exitSlotOperation(Production node) {
    KlonString slotName = new KlonString(
      ((Token) node.getChildAt(0)).getImage());
    KlonMessage attached = (KlonMessage) node.getChildAt(2)
      .getChildAt(0)
      .getValue(0);
    KlonMessage identifier = new KlonMessage();
    identifier.setLiteral(slotName);
    KlonMessage result = new KlonMessage();
    result.setSelector((KlonSymbol) node.getChildAt(1)
      .getValue(0));
    result.addArgument(identifier);
    result.addArgument(attached);
    node.addValue(result);
    return node;
  }

  @Override
  protected Node exitSymbol(Production node) {
    return compressPath(node);
  }

  @Override
  protected Node exitIdentifier(Token node) {
    node.addValue(new KlonSymbol(node.getImage()));
    node.addValue(node.getId());
    return node;
  }

  @Override
  protected Node exitOperator(Token node) {
    node.addValue(new KlonSymbol(node.getImage()));
    node.addValue(node.getId());
    return node;
  }

  @Override
  protected Node exitNumber(Token node) {
    String image = node.getImage();
    if (image.startsWith("0x") || image.startsWith(("0X"))) {
      node.addValue(new KlonNumber(Integer.parseInt(image.substring(2), 16)));
    } else {
      node.addValue(new KlonNumber(Double.parseDouble(image)));
    }
    node.addValue(node.getId());
    return node;
  }

  @Override
  protected Node exitString(Token node) {
    String value = node.getImage();
    node.addValue(new KlonString(value.substring(1, value.length() - 1)));
    node.addValue(node.getId());
    return node;
  }

  @Override
  protected Node exitSet(Token node) {
    node.addValue(new KlonSymbol("setSlot"));
    return node;
  }

  @Override
  protected Node exitUpdate(Token node) {
    node.addValue(new KlonSymbol("updateSlot"));
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
    node.addValue(new KlonSymbol(""));
    node.addValue(node.getId());
    return node;
  }

  @Override
  protected Node exitLbrace(Token node) {
    node.addValue(new KlonSymbol("brace"));
    node.addValue(node.getId());
    return node;
  }

  @Override
  protected Node exitLbrack(Token node) {
    node.addValue(new KlonSymbol("bracket"));
    node.addValue(node.getId());
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

  private Node compressPath(Production node) {
    return node.getChildCount() == 1 ? node.getChildAt(0) : node;
  }

}
