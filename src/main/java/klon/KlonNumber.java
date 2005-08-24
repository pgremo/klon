package klon;

import java.text.NumberFormat;

@Prototype(name = "Number", parent = "Object")
public class KlonNumber extends KlonObject {

  private static NumberFormat formatter = NumberFormat.getInstance();
  static {
    formatter.setGroupingUsed(false);
    formatter.setMinimumFractionDigits(0);
    formatter.setMaximumFractionDigits(Integer.MAX_VALUE);
  }

  public KlonNumber() {
    super(null, 0D);
  }

  public KlonNumber(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public String toString() {
    return formatter.format(data);
  }

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(
        (Double) receiver.getData() + message.evalAsNumber(context, 0));
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(
        (Double) receiver.getData() - message.evalAsNumber(context, 0));
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(
        (Double) receiver.getData() * message.evalAsNumber(context, 0));
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(
        (Double) receiver.getData() / message.evalAsNumber(context, 0));
  }

  @ExposedAs("%")
  public static KlonObject modulus(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(
        (Double) receiver.getData() % message.evalAsNumber(context, 0));
  }

  @ExposedAs("^")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(
        Math.pow((Double) receiver.getData(), message.evalAsNumber(
          context, 0)));
  }

  @ExposedAs("<")
  public static KlonObject lessThan(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) < 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for <");
  }

  @ExposedAs(">")
  public static KlonObject greaterThan(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) > 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for >");
  }

  @ExposedAs("<=")
  public static KlonObject lessThanEquals(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) <= 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for <=");
  }

  @ExposedAs(">=")
  public static KlonObject greaterThanEquals(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) >= 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for >=");
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(Math.abs((Double) receiver.getData()));
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("Number")
      .duplicate(Math.sqrt((Double) receiver.getData()));
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String")
      .duplicate(formatter.format(receiver.getData()));
  }
}
