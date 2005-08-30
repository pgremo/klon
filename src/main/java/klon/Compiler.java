package klon;

import java.io.Reader;
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

  public Message fromString(String value) throws KlonException {
    return fromReader(new StringReader(value));
  }

  public Message fromReader(Reader input) throws KlonException {
    try {
      return (Message) new KlonParser(input, this).parse()
        .getValue(0);
    } catch (Throwable e) {
      Throwable cause = e.getCause();
      if (cause != null) {
        e = cause;
      }
      if (e instanceof KlonException) {
        throw (KlonException) cause;
      }
      throw ((KlonException) root.getSlot("Exception")).newException(
        e.getClass()
          .getSimpleName(), e.getMessage(), null);
    }
  }

  @Override
  protected void childMessageChain(Production node, Node child) {
    Message next = (Message) child.getValue(0);
    if (node.getValueCount() == 0) {
      node.addValue(next);
    } else {
      Message current = (Message) node.getValue(node.getValueCount() - 1);
      current.setNext(next);
    }
    node.addValue(next);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void childStandardMessage(Production node, Node child) {
    Message message = (Message) node.getValue(0);
    if (message == null) {
      message = new Message();
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
      for (Message arg : (Iterable<Message>) child.getValue(2)) {
        message.addArgument(arg);
      }
    }

    if (child.getId() == ATTACHED) {
      Message attached = (Message) child.getChildAt(0)
        .getValue(0);
      if ((Integer) node.getValue(1) == OPERATOR) {
        message.addArgument(attached);
        Message newAttached = attached.getAttached();
        attached.setAttached(null);
        attached = newAttached;
      }
      message.setAttached(attached);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void childGroup(Production node, Node child) {
    if (child != null) {
      if (node.getValueCount() == 0) {
        node.addValue(child.getValue(0));
        node.addValue(child.getValue(1));
        node.addValue(new ArrayList<Message>());
      } else {
        List<Message> group = (List<Message>) node.getValue(2);
        group.add((Message) child.getValue(0));
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
      KlonObject slotName = ((KlonString) root.getSlot("String")).newString(((Token) node.getChildAt(0)).getImage());
      Message attached = (Message) node.getChildAt(2)
        .getChildAt(0)
        .getValue(0);
      Message identifier = new Message();
      identifier.setLiteral(slotName);
      Message result = new Message();
      result.setSelector((KlonSymbol) node.getChildAt(1)
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
      node.addValue(((KlonSymbol) root.getSlot("Symbol")).newSymbol(node.getImage()));
      node.addValue(node.getId());
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitOperator(Token node) throws ParseException {
    try {
      node.addValue(((KlonSymbol) root.getSlot("Symbol")).newSymbol(node.getImage()));
      node.addValue(node.getId());
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
        node.addValue(((KlonNumber) root.getSlot("Number")).newNumber((double) Integer.parseInt(
          image.substring(2), 16)));
      } else {
        node.addValue(((KlonNumber) root.getSlot("Number")).newNumber(Double.parseDouble(image)));
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
      node.addValue(((KlonString) root.getSlot("String")).newString(buffer.toString()));
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
      node.addValue(((KlonSymbol) root.getSlot("Symbol")).newSymbol("setSlot"));
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitUpdate(Token node) throws ParseException {
    try {
      node.addValue(((KlonSymbol) root.getSlot("Symbol")).newSymbol("updateSlot"));
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
      node.addValue(((KlonSymbol) root.getSlot("Symbol")).newSymbol("parenthesis"));
      node.addValue(node.getId());
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitLbrace(Token node) throws ParseException {
    try {
      node.addValue(((KlonSymbol) root.getSlot("Symbol")).newSymbol("brace"));
      node.addValue(node.getId());
      return node;
    } catch (Exception e) {
      throw new ParseException(node.getId(), e.getMessage(),
        node.getStartLine(), node.getStartColumn());
    }
  }

  @Override
  protected Node exitLbrack(Token node) throws ParseException {
    try {
      node.addValue(((KlonSymbol) root.getSlot("Symbol")).newSymbol("bracket"));
      node.addValue(node.getId());
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
