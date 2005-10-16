package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

@ExposedAs("Number")
@Bindings("Object")
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
    super.prototype();
    setSlot("pi", newNumber(this, Math.PI));
    setSlot("e", newNumber(this, Math.E));
  }

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        + evalAsNumber(context, message, 0));
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        - evalAsNumber(context, message, 0));
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        * evalAsNumber(context, message, 0));
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        / evalAsNumber(context, message, 0));
  }

  @ExposedAs("%")
  public static KlonObject modulus(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (Double) receiver.getData()
        % evalAsNumber(context, message, 0));
  }

  @ExposedAs("power")
  public static KlonObject power(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, Math.pow((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  @ExposedAs("abs")
  public static KlonObject absoluteValue(KlonObject receiver,
      KlonObject context, KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.abs((Double) receiver.getData()));
  }

  @ExposedAs("sin")
  public static KlonObject sin(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.sin((Double) receiver.getData()));
  }

  @ExposedAs("cos")
  public static KlonObject cos(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.cos((Double) receiver.getData()));
  }

  @ExposedAs("tan")
  public static KlonObject tan(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.tan((Double) receiver.getData()));
  }

  @ExposedAs("asin")
  public static KlonObject asin(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.asin((Double) receiver.getData()));
  }

  @ExposedAs("acos")
  public static KlonObject acos(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.acos((Double) receiver.getData()));
  }

  @ExposedAs("atan")
  public static KlonObject atan(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.atan((Double) receiver.getData()));
  }

  @ExposedAs("atan2")
  public static KlonObject atan2(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.atan2((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  @ExposedAs("floor")
  public static KlonObject floor(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.floor((Double) receiver.getData()));
  }

  @ExposedAs("ceiling")
  public static KlonObject ceiling(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.ceil((Double) receiver.getData()));
  }

  @ExposedAs("round")
  public static KlonObject round(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, (double) Math.round((Double) receiver.getData()));
  }

  @ExposedAs("log")
  public static KlonObject log(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.log((Double) receiver.getData()));
  }

  @ExposedAs("log10")
  public static KlonObject log10(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.log10((Double) receiver.getData()));
  }

  @ExposedAs("sqrt")
  public static KlonObject squareRoot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.sqrt((Double) receiver.getData()));
  }

  @ExposedAs("signum")
  public static KlonObject signum(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.signum((Double) receiver.getData()));
  }

  @ExposedAs("max")
  public static KlonObject max(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, Math.max((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  @ExposedAs("min")
  public static KlonObject min(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, Math.min((Double) receiver.getData(),
        evalAsNumber(context, message, 0)));
  }

  @ExposedAs( { "&", "and" })
  public static KlonObject and(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() & evalAsNumber(context, message, 0).intValue()));
  }

  @ExposedAs( { "|", "or" })
  public static KlonObject or(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() | evalAsNumber(context, message, 0).intValue()));
  }

  @ExposedAs( { "^", "xor" })
  public static KlonObject xor(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() ^ evalAsNumber(context, message, 0).intValue()));
  }

  @ExposedAs("~")
  public static KlonObject compliment(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) ~((Double) receiver.getData())
        .intValue());
  }

  @ExposedAs("<<")
  public static KlonObject leftShift(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() << evalAsNumber(context, message, 0).intValue()));
  }

  @ExposedAs(">>")
  public static KlonObject rightShift(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return newNumber(receiver, (double) (((Double) receiver.getData())
        .intValue() >> evalAsNumber(context, message, 0).intValue()));
  }

  @ExposedAs("integer")
  public static KlonObject integer(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newNumber(receiver, Math.floor((Double) receiver.getData()));
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    Buffer value = new Buffer(8);
    value.putDouble(0, (Double) receiver.getData());
    return KlonBuffer.newBuffer(receiver, value);
  }

  @ExposedAs("asCharacter")
  public static KlonObject asCharacter(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, String
        .valueOf((char) ((Double) receiver.getData()).intValue()));
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, format.format(receiver.getData()));
  }

  @SuppressWarnings("unused")
  @ExposedAs("asNumber")
  public static KlonObject asNumber(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    State state = receiver.getState();
    state.write(receiver);
    state.write("\n");
    return KlonNil.newNil(receiver);
  }

}
