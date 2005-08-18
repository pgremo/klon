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
    super(0);
  }

  public KlonNumber(Object value) throws KlonException {
    super(Klon.ROOT.getSlot("Number"), value);
  }

  public KlonNumber(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonNumber.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonNumber(this, primitive);
  }

  @Override
  public String toString() {
    return formatter.format(primitive);
  }

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) receiver.getPrimitive()
          + message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) receiver.getPrimitive()
          - message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for -");
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) receiver.getPrimitive()
          * message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for *");
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber((Double) receiver.getPrimitive()
          / message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for /");
  }

  @ExposedAs("^")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(Math.pow((Double) receiver.getPrimitive(), message
          .evalAsNumber(context, 0)));
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("<")
  public static KlonObject lessThan(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getPrimitive();
      Double o2 = (Double) argument.getPrimitive();
      return o1.compareTo(o2) < 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for <");
  }

  @ExposedAs(">")
  public static KlonObject greaterThan(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getPrimitive();
      Double o2 = (Double) argument.getPrimitive();
      return o1.compareTo(o2) > 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for >");
  }

  @ExposedAs("<=")
  public static KlonObject lessThanEquals(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getPrimitive();
      Double o2 = (Double) argument.getPrimitive();
      return o1.compareTo(o2) <= 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for <=");
  }

  @ExposedAs(">=")
  public static KlonObject greaterThanEquals(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if (receiver instanceof KlonNumber && argument instanceof KlonNumber) {
      Double o1 = (Double) receiver.getPrimitive();
      Double o2 = (Double) argument.getPrimitive();
      return o1.compareTo(o2) >= 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for >=");
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(Math.abs((Double) receiver.getPrimitive()));
    }
    throw new KlonException("Illegal Argument for abs");
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if (receiver instanceof KlonNumber) {
      return new KlonNumber(Math.sqrt((Double) receiver.getPrimitive()));
    }
    throw new KlonException("Illegal Argument for sqrt");
  }

}
