package klon;

import java.text.NumberFormat;

@ExposedAs("Number")
@Bindings("Object")
public class KlonNumber extends KlonObject {

  private static final long serialVersionUID = -3735761349600472088L;

  private static NumberFormat format = NumberFormat.getInstance();
  static {
    format.setGroupingUsed(false);
    format.setMinimumFractionDigits(0);
    format.setMaximumFractionDigits(Integer.MAX_VALUE);
  }

  public static KlonObject newNumber(KlonObject root, Double value)
      throws KlonObject {
    KlonObject result = root.getSlot("Number")
      .clone();
    result.setData(value);
    return result;
  }

  public static KlonObject newNumber(KlonObject root, int size, Buffer value)
      throws KlonObject {
    KlonObject result = root.getSlot("Number")
      .clone();
    result.setData(value.getNumber(0, size));
    return result;
  }

  public static Double evalAsNumber(KlonObject context, KlonMessage message,
      int index) throws KlonObject {
    KlonObject result = message.eval(context, index);
    if ("Number".equals(result.getType())) {
      return (Double) result.getData();
    }
    throw KlonException.newException(context, "Object.invalidArgument",
      "argument must evaluate to a number", message);
  }

  public KlonNumber(State state) {
    super(state);
    data = 0D;
  }

  @SuppressWarnings("unused")
  @Override
  public int compareTo(KlonObject other) {
    int result;
    if ("Number".equals(other.getType())) {
      result = ((Double) getData()).compareTo((Double) other.getData());
    } else {
      result = hashCode() - other.hashCode();
    }
    return result;
  }

  @Override
  public String getType() {
    return "Number";
  }

  @Override
  public String toString() {
    return format.format(data);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonNumber(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  @ExposedAs("pi")
  public static final Double PI = Math.PI;

  @ExposedAs("e")
  public static final Double E = Math.E;

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        + evalAsNumber(context, message, 0));
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        - evalAsNumber(context, message, 0));
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        * evalAsNumber(context, message, 0));
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        / evalAsNumber(context, message, 0));
  }

  @ExposedAs("%")
  public static KlonObject modulus(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (Double) receiver.getData()
        % evalAsNumber(context, message, 0));
  }

  @ExposedAs("power")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.pow((Double) receiver.getData(),
      evalAsNumber(context, message, 0)));
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.abs((Double) receiver.getData()));
  }

  @ExposedAs("sin")
  public static KlonObject sin(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.sin((Double) receiver.getData()));
  }

  @ExposedAs("cos")
  public static KlonObject cos(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.cos((Double) receiver.getData()));
  }

  @ExposedAs("tan")
  public static KlonObject tan(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.tan((Double) receiver.getData()));
  }

  @ExposedAs("asin")
  public static KlonObject asin(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.asin((Double) receiver.getData()));
  }

  @ExposedAs("acos")
  public static KlonObject acos(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.acos((Double) receiver.getData()));
  }

  @ExposedAs("atan")
  public static KlonObject atan(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.atan((Double) receiver.getData()));
  }

  @ExposedAs("atan2")
  public static KlonObject atan2(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.atan2(
      (Double) receiver.getData(), evalAsNumber(context, message, 0)));
  }

  @ExposedAs("floor")
  public static KlonObject floor(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.floor((Double) receiver.getData()));
  }

  @ExposedAs("ceiling")
  public static KlonObject ceiling(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.ceil((Double) receiver.getData()));
  }

  @ExposedAs("round")
  public static KlonObject round(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) Math.round((Double) receiver.getData()));
  }

  @ExposedAs("log")
  public static KlonObject log(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.log((Double) receiver.getData()));
  }

  @ExposedAs("log10")
  public static KlonObject log10(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.log10((Double) receiver.getData()));
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.sqrt((Double) receiver.getData()));
  }

  @ExposedAs("signum")
  public static KlonObject signum(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.signum((Double) receiver.getData()));
  }

  @ExposedAs("max")
  public static KlonObject max(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.max((Double) receiver.getData(),
      evalAsNumber(context, message, 0)));
  }

  @ExposedAs("min")
  public static KlonObject min(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.min((Double) receiver.getData(),
      evalAsNumber(context, message, 0)));
  }

  @ExposedAs({"&", "and"})
  public static KlonObject and(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() & evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs({"|", "or"})
  public static KlonObject or(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() | evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs({"^", "xor"})
  public static KlonObject xor(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() ^ evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs("~")
  public static KlonObject compliment(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ~((Double) receiver.getData()).intValue());
  }

  @ExposedAs("<<")
  public static KlonObject leftShift(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() << evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs(">>")
  public static KlonObject rightShift(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() >> evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs("integer")
  public static KlonObject integer(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.floor((Double) receiver.getData()));
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    int size = 8;
    if (message.getArgumentCount() > 0) {
      size = KlonNumber.evalAsNumber(receiver, message, 0)
        .intValue();
    }
    size = Math.min(size, 8);
    Buffer value = new Buffer(8);
    value.putNumber(0, size, (Double) receiver.getData());
    return KlonBuffer.newBuffer(receiver, value);
  }

  @ExposedAs("asCharacter")
  public static KlonObject asCharacter(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver,
      String.valueOf((char) ((Double) receiver.getData()).intValue()));
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, format.format(receiver.getData()));
  }
}
