package klon;

import java.util.ArrayList;
import java.util.List;

import klon.grammar.grammatica.KlonAnalyzer;
import klon.grammar.grammatica.KlonConstants;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.ParseException;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;

public class MessageBuilder extends KlonAnalyzer implements KlonConstants {

  private KlonObject root;

  public MessageBuilder(KlonObject root) {
    this.root = root;
  }

  @Override
  protected void childMessageChain(Production node, Node child) {
    KlonMessage next = (KlonMessage) child.getValue(0);
    if (node.getValueCount() == 0) {
      node.addValue(next);
    } else {
      ((KlonMessage) node.getValue(node.getValueCount() - 1)).setNext(next);
    }
    node.addValue(next);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void childStandardMessage(Production node, Node child)
      throws ParseException {
    try {
      KlonMessage result = (KlonMessage) node.getValue(0);
      if (result == null) {
        result = KlonMessage.newMessage(root);
        node.addValue(result);
        Object type = child.getValue(1);
        node.addValue(type);
        KlonObject subject = (KlonObject) child.getValue(0);
        if (type.equals(IDENTIFIER) || type.equals(OPERATOR)) {
          result.setSelector(subject);
        } else {
          result.setLiteral(subject);
        }
        Message message = (Message) result.getData();
        message.setLine(node.getStartLine());
        message.setColumn(node.getStartColumn());
      }

      if (child.getId() == GROUP) {
        for (KlonMessage arg : (Iterable<KlonMessage>) child.getValue(2)) {
          result.addArgument(arg);
        }
      }

      if (child.getId() == ATTACHED) {
        KlonMessage attached = (KlonMessage) child.getChildAt(0)
          .getValue(0);
        if ((Integer) node.getValue(1) == OPERATOR
            && result.getArgumentCount() == 0) {
          result.addArgument(attached);
          KlonMessage newAttached = attached.getAttached();
          attached.setAttached(null);
          attached = newAttached;
        }
        result.setAttached(attached);
      }
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void childGroup(Production node, Node child) {
    if (child != null && child.getValueCount() > 0) {
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
  protected Node exitSlotOperation(Production node) throws ParseException {
    try {
      KlonObject slotName = KlonString.newString(root,
        ((Token) node.getChildAt(0)).getImage());
      KlonMessage attached = (KlonMessage) node.getChildAt(2)
        .getChildAt(0)
        .getValue(0);
      KlonMessage identifier = KlonMessage.newMessage(root);
      identifier.setLiteral(slotName);
      KlonMessage result = KlonMessage.newMessage(root);
      Message message = (Message) result.getData();
      message.setSelector((KlonObject) node.getChildAt(1)
        .getValue(0));
      message.addArgument(identifier);
      message.addArgument(attached);
      message.setLine(node.getStartLine());
      message.setColumn(node.getStartColumn());
      node.addValue(result);
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitSymbol(Production node) {
    return compressPath(node);
  }

  @Override
  protected Node exitIdentifier(Token node) throws ParseException {
    try {
      node.addValue(KlonString.newString(root, node.getImage()));
      node.addValue(IDENTIFIER);
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitOperator(Token node) throws ParseException {
    try {
      node.addValue(KlonString.newString(root, node.getImage()));
      node.addValue(OPERATOR);
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitNumber(Token node) throws ParseException {
    try {
      String image = node.getImage();
      if (image.startsWith("0x") || image.startsWith("0X")) {
        node.addValue(KlonNumber.newNumber(root, (double) Integer.parseInt(
          image.substring(2), 16)));
      } else {
        node.addValue(KlonNumber.newNumber(root, Double.parseDouble(image)));
      }
      node.addValue(node.getId());
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitString(Token node) throws ParseException {
    try {
      String value = node.getImage();
      value = value.substring(1, value.length() - 1);
      StringBuilder buffer = new StringBuilder();
      boolean escaping = false;
      for (int i = 0; i < value.length(); i++) {
        char current = value.charAt(i);
        if ('\\' == current) {
          if (!escaping) {
            escaping = true;
            current = 0;
          }
        } else {
          if (escaping) {
            if ('n' == current) {
              current = '\n';
            } else if ('r' == current) {
              current = '\r';
            } else if ('\t' == current) {
              current = '\t';
            }
            escaping = false;
          }
        }
        if (current > 0) {
          buffer.append(current);
        }
      }
      node.addValue(KlonString.newString(root, buffer.toString()));
      node.addValue(node.getId());
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }

  }

  @Override
  protected Node exitSet(Token node) throws ParseException {
    try {
      node.addValue(KlonString.newString(root, "setSlot"));
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitUpdate(Token node) throws ParseException {
    try {
      node.addValue(KlonString.newString(root, "updateSlot"));
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
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
  protected Node exitLparen(Token node) throws ParseException {
    try {
      node.addValue(KlonString.newString(root, ""));
      node.addValue(IDENTIFIER);
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitLbrace(Token node) throws ParseException {
    try {
      node.addValue(KlonString.newString(root, "brace"));
      node.addValue(IDENTIFIER);
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitLbrack(Token node) throws ParseException {
    try {
      node.addValue(KlonString.newString(root, "bracket"));
      node.addValue(IDENTIFIER);
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
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
