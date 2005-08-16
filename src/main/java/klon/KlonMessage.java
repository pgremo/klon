package klon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KlonMessage extends KlonObject {

  private KlonSymbol selector;
  private KlonObject literal;
  private List<KlonMessage> arguments = new ArrayList<KlonMessage>();
  private KlonMessage attached;
  private KlonMessage next;

  public List<KlonMessage> getArguments() {
    return arguments;
  }

  public int getArgumentCount() {
    return arguments.size();
  }

  public void addArgument(KlonMessage message) {
    arguments.add(message);
  }

  public KlonMessage getArgument(int index) {
    return arguments.get(index);
  }

  public KlonMessage getAttached() {
    return attached;
  }

  public void setAttached(KlonMessage attached) {
    this.attached = attached;
  }

  public KlonObject getLiteral() {
    return literal;
  }

  public void setLiteral(KlonObject literal) {
    this.literal = literal;
  }

  public KlonMessage getNext() {
    return next;
  }

  public void setNext(KlonMessage next) {
    this.next = next;
  }

  public KlonSymbol getSelector() {
    return selector;
  }

  public void setSelector(KlonSymbol selector) {
    this.selector = selector;
  }

  public KlonObject eval(KlonObject receiver) throws KlonException {
    KlonObject self = receiver;
    for (KlonMessage outer = this; outer != null; outer = outer.getNext()) {
      receiver = self;
      for (KlonMessage inner = outer; inner != null; inner = inner.getAttached()) {
        KlonObject result = inner.getLiteral();
        if (result == null) {
          result = receiver.send(inner);
        }
        receiver = result;
      }
    }
    return receiver;
  }

  public KlonObject eval(KlonObject receiver, int index) throws KlonException {
    KlonObject self = receiver;
    for (KlonMessage outer = arguments.get(index); outer != null; outer = outer.getNext()) {
      receiver = self;
      for (KlonMessage inner = outer; inner != null; inner = inner.getAttached()) {
        KlonObject result = inner.getLiteral();
        if (result == null) {
          result = receiver.send(inner);
        }
        receiver = result;
      }
    }
    return receiver;
  }

  public double evalAsNumber(KlonObject receiver, int index)
      throws KlonException {
    KlonObject result = eval(receiver, index);
    if (!(result instanceof KlonNumber)) {
      throw new KlonException("result must be a number");
    }
    return ((KlonNumber) result).getValue();
  }

  public String evalAsString(KlonObject receiver, int index)
      throws KlonException {
    KlonObject result = eval(receiver, index);
    if (!(result instanceof KlonString)) {
      throw new KlonException("result must be a string");
    }
    return ((KlonString) result).getValue();
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (selector != null) {
      result.append(selector);
    }
    if (literal != null) {
      result.append(literal);
    }
    if (arguments.size() > 0) {
      result.append("(");
      Iterator<KlonMessage> iterator = arguments.iterator();
      while (iterator.hasNext()) {
        result.append(iterator.next());
        if (iterator.hasNext()) {
          result.append(", ");
        }
      }
      result.append(")");
    }
    if (attached != null) {
      result.append(" ")
        .append(attached);
    }
    if (next != null) {
      result.append(";\n")
        .append(next);
    }
    return result.toString();
  }

}
