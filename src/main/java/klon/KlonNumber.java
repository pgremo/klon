package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class KlonNumber extends KlonObject {

  private static final long serialVersionUID = -3735761349600472088L;

  private static DecimalFormat format = (DecimalFormat) NumberFormat
      .getInstance();

  static {
    DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
    symbols.setInfinity("Infinity");
    symbols.setNaN("NaN");
    format.setDecimalFormatSymbols(symbols);
    format.setGroupingUsed(false);
    format.setMinimumFractionDigits(0);
    format.setMaximumFractionDigits(Integer.MAX_VALUE);
  }

  public static KlonObject newNumber(KlonObject root, Double value)
      throws KlonObject {
    KlonObject result = root.getSlot("Number").clone();
    result.setData(value);
    return result;
  }

  public static Double evalAsNumber(KlonObject context, KlonObject message,
      int index) throws KlonObject {
    KlonObject argument = KlonMessage.evalArgument(message, context, index);
    KlonObject asNumber = context.getState().getAsNumber();
    return (Double) KlonMessage.eval(asNumber, argument, context).getData();
  }

  public KlonNumber() {

  }

  public KlonNumber(State state) {
    super(state);
    setData(0D);
  }

  @SuppressWarnings("unused")
  @Override
  public int compareTo(KlonObject other) {
    int result;
    if (other instanceof KlonNumber) {
      result = ((Double) getData()).compareTo((Double) other.getData());
    } else {
      result = super.compareTo(other);
    }
    return result;
  }

  @Override
  public String toString() {
    return format.format(getData());
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonNumber(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    setData(in.readObject());
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(getData());
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("abs", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("absoluteValue", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("acos", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("acos", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("+", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("add", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("&", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("and", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("and", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("and", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asBuffer", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("asBuffer", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asCharacter", KlonNativeMethod.newNativeMethod(root,
        KlonNumber.class.getMethod("asCharacter",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asin", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("asin", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asNumber", KlonNativeMethod.newNativeMethod(root, KlonObject.class
        .getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asString", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("asString", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("atan", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("atan", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("atan2", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("atan2", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ceiling", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("ceiling", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("~", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("compliment", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("cos", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("cos", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("/", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("divide", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("floor", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("floor", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("integer", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("integer", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("<<", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("leftShift", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("log", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("log", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("log10", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("log10", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("max", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("max", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("min", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("min", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("%", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("modulus", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("*", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("multiply", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("or", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("or", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("|", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("or", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("power", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("power", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("print", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("print", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot(">>", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("rightShift", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("round", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("round", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("signum", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("signum", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("sin", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("sin", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("sqrt", KlonNativeMethod.newNativeMethod(root,
        KlonNumber.class.getMethod("squareRoot",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("-", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("subtract", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("tan", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("tan", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("xor", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("xor", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("^", KlonNativeMethod.newNativeMethod(root, KlonNumber.class
        .getMethod("xor", KlonNativeMethod.PARAMETER_TYPES)));

    setSlot("pi", newNumber(this, Math.PI));
    setSlot("e", newNumber(this, Math.E));
  }

  public static KlonObject add(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        + evalAsNumber(context, message, 0));
  }

  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        - evalAsNumber(context, message, 0));
  }

  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        * evalAsNumber(context, message, 0));
  }

  public static KlonObject divide(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        / evalAsNumber(context, message, 0));
  }

  public static KlonObject modulus(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        % evalAsNumber(context, message, 0));
  }

  public static KlonObject power(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, Math.pow((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.abs((Double) receiver.getData()));
  }

  public static KlonObject sin(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.sin((Double) receiver.getData()));
  }

  public static KlonObject cos(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.cos((Double) receiver.getData()));
  }

  public static KlonObject tan(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.tan((Double) receiver.getData()));
  }

  public static KlonObject asin(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.asin((Double) receiver.getData()));
  }

  public static KlonObject acos(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.acos((Double) receiver.getData()));
  }

  public static KlonObject atan(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.atan((Double) receiver.getData()));
  }

  public static KlonObject atan2(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.atan2((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  public static KlonObject floor(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.floor((Double) receiver.getData()));
  }

  public static KlonObject ceiling(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.ceil((Double) receiver.getData()));
  }

  public static KlonObject round(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, (double) Math.round((Double) receiver.getData()));
  }

  public static KlonObject log(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.log((Double) receiver.getData()));
  }

  public static KlonObject log10(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.log10((Double) receiver.getData()));
  }

  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.sqrt((Double) receiver.getData()));
  }

  public static KlonObject signum(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.signum((Double) receiver.getData()));
  }

  public static KlonObject max(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, Math.max((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  public static KlonObject min(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, Math.min((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  public static KlonObject and(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() & evalAsNumber(context, message, 0).intValue()));
  }

  public static KlonObject or(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() | evalAsNumber(context, message, 0).intValue()));
  }

  public static KlonObject xor(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() ^ evalAsNumber(context, message, 0).intValue()));
  }

  public static KlonObject compliment(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) ~((Double) receiver.getData())
        .intValue());
  }

  public static KlonObject leftShift(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() << evalAsNumber(context, message, 0).intValue()));
  }

  public static KlonObject rightShift(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() >> evalAsNumber(context, message, 0).intValue()));
  }

  public static KlonObject integer(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.floor((Double) receiver.getData()));
  }

  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    Buffer value = new Buffer(8);
    value.putDouble(0, (Double) receiver.getData());
    return KlonBuffer.newBuffer(receiver, value);
  }

  public static KlonObject asCharacter(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, String
        .valueOf((char) ((Double) receiver.getData()).intValue()));
  }

  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, format.format(receiver.getData()));
  }

  @SuppressWarnings("unused")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    State state = receiver.getState();
    state.write(receiver);
    state.write("\n");
    return KlonNil.newNil(receiver);
  }

}
