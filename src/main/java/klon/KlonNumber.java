package klon;

import java.text.NumberFormat;

@Prototype(name = "Number", parent = "Object")
public class KlonNumber {

  private static final long serialVersionUID = -4103647195957467063L;
  private static NumberFormat format = NumberFormat.getInstance();
  static {
    format.setGroupingUsed(false);
    format.setMinimumFractionDigits(0);
    format.setMaximumFractionDigits(Integer.MAX_VALUE);
  }

  public static KlonObject newNumber(KlonObject root, Double value)
      throws KlonObject {
    KlonObject result = root.getSlot("Number")
      .duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    result.setData(0D);
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
    throw KlonException.newException(receiver, "Illegal Argument",
      "argument must evaluate to a number", message);
  }

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        + evalAsNumber(context, message, 0));
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        - evalAsNumber(context, message, 0));
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        * evalAsNumber(context, message, 0));
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        / evalAsNumber(context, message, 0));
  }

  @ExposedAs("%")
  public static KlonObject modulus(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        % evalAsNumber(context, message, 0));
  }

  @ExposedAs("^")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.pow((Double) receiver.getData(),
      evalAsNumber(context, message, 0)));
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
    throw KlonException.newException(receiver, "Illegal Argument",
      "Illegal Argument for <", message);
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
    throw KlonException.newException(receiver, "Illegal Argument",
      "Illegal Argument for >", message);
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
    throw KlonException.newException(receiver, "Illegal Argument",
      "Illegal Argument for <=", message);
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
    throw KlonException.newException(receiver, "Illegal Argument",
      "Illegal Argument for >=", message);
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.abs((Double) receiver.getData()));
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.sqrt((Double) receiver.getData()));
  }

  @ExposedAs("integer")
  public static KlonObject integer(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (Math.floor((Double) receiver.getData())));
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, format.format(receiver.getData()));
  }
}
