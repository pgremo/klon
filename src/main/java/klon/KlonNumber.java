package klon;

import java.text.NumberFormat;

import klon.reflection.ExposedAs;

public class KlonNumber extends KlonObject {

  private static NumberFormat formatter = NumberFormat.getInstance();
  static {
    formatter.setGroupingUsed(false);
    formatter.setMinimumFractionDigits(0);
    formatter.setMaximumFractionDigits(Integer.MAX_VALUE);
  }

  public KlonNumber() {
    super();
  }

  public KlonNumber(double value) throws KlonException {
    super(Klon.ROOT.getSlot("Number"), value);
  }

  public KlonNumber(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws MessageNotUnderstood {
    Configurator.configure(KlonNumber.class, slots);
    parent = Klon.ROOT.getSlot("Object");
  }

  @Override
  public KlonObject clone() {
    return new KlonNumber(this, attached);
  }

  @Override
  public String toString() {
    return formatter.format(attached);
  }

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) ((KlonNumber) receiver).getAttached()
          + message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) ((KlonNumber) receiver).getAttached()
          - message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for -");
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) ((KlonNumber) receiver).getAttached()
          * message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for *");
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) ((KlonNumber) receiver).getAttached()
          / message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for /");
  }

  @ExposedAs("^")
  public static KlonObject power(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(Math.pow(
        (Double) ((KlonNumber) receiver).getAttached(), message.evalAsNumber(
          receiver, 0)));
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("<")
  public static Object lessThan(KlonObject receiver, Message message)
      throws KlonException {
    if (!(receiver instanceof KlonNumber)) {
      throw new KlonException("Illegal Argument for <");
    }
    KlonObject result = Klon.ROOT.getSlot("Nil");
    KlonObject argument = message.eval(receiver, 0);
    if (argument instanceof KlonNumber
        && (Double) ((KlonNumber) receiver).getAttached() < (Double) argument.getAttached()) {
      result = argument;
    }
    return result;
  }

  @ExposedAs(">")
  public static Object greaterThan(KlonObject receiver, Message message)
      throws KlonException {
    if (!(receiver instanceof KlonNumber)) {
      throw new KlonException("Illegal Argument for >");
    }
    KlonObject result = Klon.ROOT.getSlot("Nil");
    KlonObject argument = message.eval(receiver, 0);
    if (argument instanceof KlonNumber
        && (Double) ((KlonNumber) receiver).getAttached() > (Double) argument.getAttached()) {
      result = argument;
    }
    return result;
  }

  @ExposedAs("<=")
  public static Object lessThanEquals(KlonObject receiver, Message message)
      throws KlonException {
    if (!(receiver instanceof KlonNumber)) {
      throw new KlonException("Illegal Argument for <=");
    }
    KlonObject result = Klon.ROOT.getSlot("Nil");
    KlonObject argument = message.eval(receiver, 0);
    if (argument instanceof KlonNumber
        && (Double) ((KlonNumber) receiver).getAttached() <= (Double) argument.getAttached()) {
      result = argument;
    }
    return result;
  }

  @ExposedAs(">=")
  public static Object greaterThanEquals(KlonObject receiver, Message message)
      throws KlonException {
    if (!(receiver instanceof KlonNumber)) {
      throw new KlonException("Illegal Argument for >=");
    }
    KlonObject result = Klon.ROOT.getSlot("Nil");
    KlonObject argument = message.eval(receiver, 0);
    if (argument instanceof KlonNumber
        && (Double) ((KlonNumber) receiver).getAttached() >= (Double) argument.getAttached()) {
      result = argument;
    }
    return result;
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(
        Math.abs((Double) ((KlonNumber) receiver).getAttached()));
    }
    throw new KlonException("Illegal Argument for abs");
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(
        Math.sqrt((Double) ((KlonNumber) receiver).getAttached()));
    }
    throw new KlonException("Illegal Argument for sqrt");
  }

}
