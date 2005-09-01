package klon;

import java.text.NumberFormat;

@Prototype(name = "Number", parent = "Object")
public class KlonNumber extends KlonObject {

  private static final long serialVersionUID = -4103647195957467063L;
  private static NumberFormat format = NumberFormat.getInstance();
  static {
    format.setGroupingUsed(false);
    format.setMinimumFractionDigits(0);
    format.setMaximumFractionDigits(Integer.MAX_VALUE);
  }

  public KlonNumber() {
    super();
    setData(0D);
  }

  public KlonObject newNumber(Double value) throws KlonObject {
    KlonObject result = duplicate();
    result.setData(value);
    return result;
  }

  public static String format(KlonObject value) {
    return format.format(value.getData());
  }

  public static Double evalAsNumber(KlonObject receiver, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(receiver, index);
    if ("Number".equals(result.getType())) {
      return (Double) result.getData();
    }
    throw ((KlonException) receiver.getSlot("Exception")).newException(
      "Illegal Argument", "argument must evaluate to a number", message);
  }

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber((Double) receiver.getData()
        + evalAsNumber(context, message, 0));
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber((Double) receiver.getData()
        - evalAsNumber(context, message, 0));
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber(

    (Double) receiver.getData() * evalAsNumber(context, message, 0));
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber(

    (Double) receiver.getData() / evalAsNumber(context, message, 0));
  }

  @ExposedAs("%")
  public static KlonObject modulus(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber(

    (Double) receiver.getData() % evalAsNumber(context, message, 0));
  }

  @ExposedAs("^")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber(

    Math.pow((Double) receiver.getData(), evalAsNumber(context, message, 0)));
  }

  @ExposedAs("<")
  public static KlonObject lessThan(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) < 0 ? argument : receiver.getSlot("Nil");
    }
    throw ((KlonException) receiver.getSlot("Exception")).newException(
      "Illegal Argument", "Illegal Argument for <", message);
  }

  @ExposedAs(">")
  public static KlonObject greaterThan(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) > 0 ? argument : receiver.getSlot("Nil");
    }
    throw ((KlonException) receiver.getSlot("Exception")).newException(
      "Illegal Argument", "Illegal Argument for >", message);
  }

  @ExposedAs("<=")
  public static KlonObject lessThanEquals(KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) <= 0 ? argument : receiver.getSlot("Nil");
    }
    throw ((KlonException) receiver.getSlot("Exception")).newException(
      "Illegal Argument", "Illegal Argument for <=", message);
  }

  @ExposedAs(">=")
  public static KlonObject greaterThanEquals(KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getData();
      Double o2 = (Double) argument.getData();
      return o1.compareTo(o2) >= 0 ? argument : receiver.getSlot("Nil");
    }
    throw ((KlonException) receiver.getSlot("Exception")).newException(
      "Illegal Argument", "Illegal Argument for >=", message);
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber(Math.abs((Double) receiver.getData()));
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber(Math.sqrt((Double) receiver.getData()));
  }

  @ExposedAs("integer")
  public static KlonObject integer(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonNumber) receiver.getSlot("Number")).newNumber((Math.floor((Double) receiver.getData())));
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return ((KlonString) receiver.getSlot("String")).newString(format.format(receiver.getData()));
  }
}
