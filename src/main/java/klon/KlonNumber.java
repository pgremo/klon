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
    parent = Klon.ROOT.getSlot("Object");
    Configurator.configure(KlonNumber.class, slots);
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
      return new KlonNumber((Double) receiver.getAttached()
          + message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) receiver.getAttached()
          - message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for -");
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) receiver.getAttached()
          * message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for *");
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) receiver.getAttached()
          / message.evalAsNumber(receiver, 0));
    }
    throw new KlonException("Illegal Argument for /");
  }

  @ExposedAs("^")
  public static KlonObject power(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(Math.pow((Double) receiver.getAttached(),
        message.evalAsNumber(receiver, 0)));
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("<")
  public static Object lessThan(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject argument = message.eval(receiver, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getAttached();
      Double o2 = (Double) argument.getAttached();
      return o1.compareTo(o2) < 0 ? argument : Klon.ROOT.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for <");
  }

  @ExposedAs(">")
  public static Object greaterThan(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject argument = message.eval(receiver, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getAttached();
      Double o2 = (Double) argument.getAttached();
      return o1.compareTo(o2) > 0 ? argument : Klon.ROOT.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for >");
  }

  @ExposedAs("<=")
  public static Object lessThanEquals(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject argument = message.eval(receiver, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getAttached();
      Double o2 = (Double) argument.getAttached();
      return o1.compareTo(o2) <= 0 ? argument : Klon.ROOT.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for <=");
  }

  @ExposedAs(">=")
  public static Object greaterThanEquals(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject argument = message.eval(receiver, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getAttached();
      Double o2 = (Double) argument.getAttached();
      return o1.compareTo(o2) >= 0 ? argument : Klon.ROOT.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for >=");
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(Math.abs((Double) receiver.getAttached()));
    }
    throw new KlonException("Illegal Argument for abs");
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, Message message)
      throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(Math.sqrt((Double) receiver.getAttached()));
    }
    throw new KlonException("Illegal Argument for sqrt");
  }

}
