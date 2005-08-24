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
    this.prototype = KlonNumber.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonNumber.class);
  }

  @Override
  public KlonObject clone(Object subject) {
    return new KlonNumber(this, subject);
  }

  @Override
  public String toString() {
    return formatter.format(primitive);
  }

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          (Double) receiver.getPrimitive() + message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          (Double) receiver.getPrimitive() - message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for -");
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          (Double) receiver.getPrimitive() * message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for *");
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          (Double) receiver.getPrimitive() / message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for /");
  }

  @ExposedAs("%")
  public static KlonObject modulus(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          (Double) receiver.getPrimitive() % message.evalAsNumber(context, 0));
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("^")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          Math.pow((Double) receiver.getPrimitive(), message.evalAsNumber(
              context, 0)));
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("<")
  public static KlonObject lessThan(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject argument = message.eval(context, 0);
    if ("Number".equals(receiver.getType())
        && "Number".equals(argument.getType())) {
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
    if ("Number".equals(receiver.getType())
        && "Number".equals(argument.getType())) {
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
    if ("Number".equals(receiver.getType())
        && "Number".equals(argument.getType())) {
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
    if ("Number".equals(receiver.getType())
        && "Number".equals(argument.getType())) {
      Double o1 = (Double) receiver.getPrimitive();
      Double o2 = (Double) argument.getPrimitive();
      return o1.compareTo(o2) >= 0 ? argument : receiver.getSlot("Nil");
    }
    throw new KlonException("Illegal Argument for >=");
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          Math.abs((Double) receiver.getPrimitive()));
    }
    throw new KlonException("Illegal Argument for abs");
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    if ("Number".equals(receiver.getType())) {
      return receiver.getSlot("Number").clone(
          Math.sqrt((Double) receiver.getPrimitive()));
    }
    throw new KlonException("Illegal Argument for sqrt");
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String").clone(
        formatter.format(receiver.getPrimitive()));
  }
}
