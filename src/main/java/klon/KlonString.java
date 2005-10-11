package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

@ExposedAs("String")
@Bindings("Object")
public class KlonString extends KlonObject {

  private static final long serialVersionUID = -7547715800603443713L;

  public static KlonObject newString(KlonObject root, String value)
      throws KlonObject {
    KlonObject result = root.getSlot("String").clone();
    result.setData(value);
    return result;
  }

  public static String evalAsString(KlonObject context, KlonObject message,
      int index) throws KlonObject {
    KlonObject argument = KlonMessage.evalArgument(message, context, index);
    KlonObject asString = argument.getState().getAsString();
    return (String) KlonMessage.eval(asString, argument, context).getData();
  }

  public KlonString() {

  }

  public KlonString(State state) {
    super(state);
    setData("");
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonString(getState());
    result.bind(this);
    result.setData(getData());
    return result;
  }

  @SuppressWarnings("unused")
  @Override
  public int compareTo(KlonObject other) {
    int result;
    if (other instanceof KlonString) {
      result = ((String) getData()).compareTo((String) other.getData());
    } else {
      result = super.compareTo(other);
    }
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
  public String toString() {
    return "\"" + getData() + "\"";
  }

  @ExposedAs( { "+", "concatenate" })
  public static KlonObject append(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject printMessage = receiver.getState().getAsString();
    return KlonString.newString(receiver, receiver.getData()
        + String.valueOf(KlonMessage.evalArgument(message, context, 0).perform(
            context, printMessage).getData()));
  }

  @ExposedAs("beginsWith")
  public static KlonObject beginsWith(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return ((String) receiver.getData()).startsWith(KlonString.evalAsString(
        context, message, 0)) ? receiver : KlonNil.newNil(receiver);
  }

  @ExposedAs("endsWith")
  public static KlonObject endsWith(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return ((String) receiver.getData()).endsWith(KlonString.evalAsString(
        context, message, 0)) ? receiver : KlonNil.newNil(receiver);
  }

  @ExposedAs("lowerCase")
  public static KlonObject lowerCase(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, ((String) receiver.getData())
        .toLowerCase());
  }

  @ExposedAs("upperCase")
  public static KlonObject upperCase(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, ((String) receiver.getData())
        .toUpperCase());
  }

  @ExposedAs("split")
  public static KlonObject split(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    List<KlonObject> result = new ArrayList<KlonObject>();
    String delimiter = "\\s";
    if (KlonMessage.getArgumentCount(message) > 0) {
      delimiter = KlonString.evalAsString(context, message, 0);
    }
    for (String current : ((String) receiver.getData()).split(delimiter)) {
      result.add(KlonString.newString(receiver, current));
    }
    return KlonList.newList(receiver, result);
  }

  @ExposedAs("replace")
  public static KlonObject replace(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    String search = KlonString.evalAsString(context, message, 0);
    String replace = KlonString.evalAsString(context, message, 1);
    String result = ((String) receiver.getData()).replaceAll(search, replace);
    return KlonString.newString(receiver, result);
  }

  @ExposedAs("asBuffer")
  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonBuffer.newBuffer(receiver, new Buffer(((String) receiver
        .getData()).getBytes()));
  }

  @SuppressWarnings("unused")
  @ExposedAs("asNumber")
  public static KlonObject asNumber(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver, Double.valueOf((String) receiver
        .getData()));
  }

  @SuppressWarnings("unused")
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    State state = receiver.getState();
    state.write(receiver.getData());
    state.write("\n");
    return KlonNil.newNil(receiver);
  }

}
