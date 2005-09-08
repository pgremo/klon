package klon;

import java.text.NumberFormat;

@Bindings("Object")
public class KlonNumber extends Identity {

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
      .duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject newNumber(KlonObject root, int size, Buffer value)
      throws KlonObject {
    KlonObject result = root.getSlot("Number")
      .duplicate();
    result.setData(value.getNumber(0, size));
    return result;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setData(0D);
    result.setIdentity(new KlonNumber());
    return result;
  }

  @SuppressWarnings("unused")
  @Override
  public int compare(KlonObject receiver, KlonObject other) throws KlonObject {
    int result;
    if ("Number".equals(other.getSlot("type")
      .getData())) {
      result = ((Double) receiver.getData()).compareTo((Double) other.getData());
    } else {
      result = receiver.hashCode() - other.hashCode();
    }
    return result;
  }

  @Override
  public String format(KlonObject value) {
    return format.format(value.getData());
  }

  public static Double evalAsNumber(KlonObject receiver, Message message,
      int index) throws KlonObject {
    KlonObject result = message.eval(receiver, index);
    if ("Number".equals(result.getSlot("type")
      .getData())) {
      return (Double) result.getData();
    }
    throw KlonException.newException(receiver, "Illegal Argument",
      "argument must evaluate to a number", message);
  }

  @ExposedAs("type")
  public static String type = "Number";

  @ExposedAs("pi")
  public static final Double PI = Math.PI;

  @ExposedAs("e")
  public static final Double E = Math.E;

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

  @ExposedAs("power")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.pow((Double) receiver.getData(),
      evalAsNumber(context, message, 0)));
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.abs((Double) receiver.getData()));
  }

  @ExposedAs("sin")
  public static KlonObject sin(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.sin((Double) receiver.getData()));
  }

  @ExposedAs("cos")
  public static KlonObject cos(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.cos((Double) receiver.getData()));
  }

  @ExposedAs("tan")
  public static KlonObject tan(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.tan((Double) receiver.getData()));
  }

  @ExposedAs("asin")
  public static KlonObject asin(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.asin((Double) receiver.getData()));
  }

  @ExposedAs("acos")
  public static KlonObject acos(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.acos((Double) receiver.getData()));
  }

  @ExposedAs("atan")
  public static KlonObject atan(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.atan((Double) receiver.getData()));
  }

  @ExposedAs("atan2")
  public static KlonObject atan2(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.atan2(
      (Double) receiver.getData(), evalAsNumber(context, message, 0)));
  }

  @ExposedAs("floor")
  public static KlonObject floor(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.floor((Double) receiver.getData()));
  }

  @ExposedAs("ceiling")
  public static KlonObject ceiling(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.ceil((Double) receiver.getData()));
  }

  @ExposedAs("round")
  public static KlonObject round(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) Math.round((Double) receiver.getData()));
  }

  @ExposedAs("log")
  public static KlonObject log(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.log((Double) receiver.getData()));
  }

  @ExposedAs("log10")
  public static KlonObject log10(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.log10((Double) receiver.getData()));
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.sqrt((Double) receiver.getData()));
  }

  @ExposedAs("signum")
  public static KlonObject signum(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.signum((Double) receiver.getData()));
  }

  @ExposedAs("max")
  public static KlonObject max(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.max((Double) receiver.getData(),
      evalAsNumber(context, message, 0)));
  }

  @ExposedAs("min")
  public static KlonObject min(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Math.min((Double) receiver.getData(),
      evalAsNumber(context, message, 0)));
  }

  @ExposedAs({"&", "and"})
  public static KlonObject and(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() & evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs({"|", "or"})
  public static KlonObject or(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() | evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs({"^", "xor"})
  public static KlonObject xor(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() ^ evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs("~")
  public static KlonObject compliment(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) ~((Double) receiver.getData()).intValue());
  }

  @ExposedAs("<<")
  public static KlonObject leftShift(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() << evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs(">>")
  public static KlonObject rightShift(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) (((Double) receiver.getData()).intValue() >> evalAsNumber(
        context, message, 0).intValue()));
  }

  @ExposedAs("integer")
  public static KlonObject integer(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Math.floor((Double) receiver.getData()));
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
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
      Message message) throws KlonObject {
    return KlonString.newString(receiver,
      String.valueOf((char) ((Double) receiver.getData()).intValue()));
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, format.format(receiver.getData()));
  }
}
