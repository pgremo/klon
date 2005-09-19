package klon;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import klon.grammar.grammatica.KlonAnalyzer;
import klon.grammar.grammatica.KlonConstants;
import klon.grammar.grammatica.KlonParser;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.ParseException;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;

public class Compiler extends KlonAnalyzer implements KlonConstants {

  private KlonObject root;

  public Compiler(KlonObject root) {
    this.root = root;
  }

  public KlonMessage fromString(String value) throws KlonObject {
    KlonMessage result;
    String message = value.trim();
    if ("".equals(message)) {
      result = KlonMessage.newMessage(root, new Message());
      result.setSelector(KlonString.newString(root, ""));
    } else {
      try {
        result = (KlonMessage) new KlonParser(new StringReader(message), this).parse()
          .getValue(0);
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause == null) {
          cause = e;
        }
        if (cause instanceof KlonObject) {
          throw (KlonObject) cause;
        }
        throw KlonException.newException(root, cause.getClass()
          .getSimpleName(), cause.getMessage(), null);
      }
    }
    return result;
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
      KlonMessage message = (KlonMessage) node.getValue(0);
      if (message == null) {
        message = KlonMessage.newMessage(root, new Message());
        node.addValue(message);
        Object type = child.getValue(1);
        node.addValue(type);
        KlonObject subject = (KlonObject) child.getValue(0);
        if (type.equals(IDENTIFIER) || type.equals(OPERATOR)) {
          message.setSelector(subject);
        } else {
          message.setLiteral(subject);
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
        if ((Integer) node.getValue(1) == OPERATOR
            && message.getArgumentCount() == 0) {
          message.addArgument(attached);
          KlonMessage newAttached = attached.getAttached();
          attached.setAttached(null);
          attached = newAttached;
        }
        message.setAttached(attached);
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
      KlonMessage identifier = KlonMessage.newMessage(root, new Message());
      identifier.setLiteral(slotName);
      KlonMessage result = KlonMessage.newMessage(root, new Message());
      result.setSelector((KlonObject) node.getChildAt(1)
        .getValue(0));
      result.addArgument(identifier);
      result.addArgument(attached);
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
