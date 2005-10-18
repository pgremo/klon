package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class KlonString extends KlonObject {

  private static final long serialVersionUID = -7547715800603443713L;

  public static KlonObject newString(KlonObject root, String value)
      throws KlonObject {
    KlonObject result = root.getSlot("String")
      .clone();
    result.setData(value);
    return result;
  }

  public static String evalAsString(KlonObject context, KlonObject message,
      int index) throws KlonObject {
    KlonObject argument = KlonMessage.evalArgument(message, context, index);
    KlonObject asString = argument.getState()
      .getAsString();
    return (String) KlonMessage.eval(asString, argument, context)
      .getData();
  }

  public KlonString() {

  }

  public KlonString(State state) {
    super(state);
    setData("");
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("+", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("append", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asBuffer", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("asBuffer", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asString", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("asString", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asNumber", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("asNumber", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("beginsWith",
      KlonNativeMethod.newNativeMethod(root, KlonString.class.getMethod(
        "beginsWith", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("endsWith", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("endsWith", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("forEach", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("forEach", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("lowerCase",
      KlonNativeMethod.newNativeMethod(root, KlonString.class.getMethod(
        "lowerCase", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("print", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("print", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("replace", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("replace", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("split", KlonNativeMethod.newNativeMethod(root,
      KlonString.class.getMethod("split", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("upperCase",
      KlonNativeMethod.newNativeMethod(root, KlonString.class.getMethod(
        "upperCase", KlonNativeMethod.PARAMETER_TYPES)));
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

  public static KlonObject append(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject asString = receiver.getState()
      .getAsString();
    return newString(receiver, receiver.getData()
        + String.valueOf(KlonMessage.eval(asString,
          KlonMessage.evalArgument(message, context, 0), context)
          .getData()));
  }

  public static KlonObject beginsWith(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return ((String) receiver.getData()).startsWith(KlonString.evalAsString(
      context, message, 0)) ? receiver : KlonNil.newNil(receiver);
  }

  public static KlonObject endsWith(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return ((String) receiver.getData()).endsWith(KlonString.evalAsString(
      context, message, 0)) ? receiver : KlonNil.newNil(receiver);
  }

  public static KlonObject lowerCase(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newString(receiver, ((String) receiver.getData()).toLowerCase());
  }

  public static KlonObject upperCase(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return newString(receiver, ((String) receiver.getData()).toUpperCase());
  }

  public static KlonObject split(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    List<KlonObject> result = new ArrayList<KlonObject>();
    String delimiter = "\\s";
    if (KlonMessage.getArgumentCount(message) > 0) {
      delimiter = KlonString.evalAsString(context, message, 0);
    }
    for (String current : ((String) receiver.getData()).split(delimiter)) {
      result.add(newString(receiver, current));
    }
    return KlonList.newList(receiver, result);
  }

  public static KlonObject replace(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    String search = KlonString.evalAsString(context, message, 0);
    String replace = KlonString.evalAsString(context, message, 1);
    String result = ((String) receiver.getData()).replaceAll(search, replace);
    return newString(receiver, result);
  }

  @SuppressWarnings("unchecked")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject result = KlonNil.newNil(receiver);
    int arg = 0;
    String index = null;
    if (KlonMessage.getArgumentCount(message) == 3) {
      index = (String) KlonMessage.getSelector(
        KlonMessage.getArgument(message, arg++))
        .getData();
    }
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, arg++))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, arg);
    String string = (String) receiver.getData();
    for (int i = 0; i < string.length(); i++) {
      if (index != null) {
        context.setSlot(index, KlonNumber.newNumber(receiver, (double) i));
      }
      context.setSlot(value, newString(receiver,
        String.valueOf(string.charAt(i))));
      result = KlonMessage.eval(code, context, context);
    }
    return result;
  }

  public static KlonObject asBuffer(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonBuffer.newBuffer(receiver, new Buffer(
      ((String) receiver.getData()).getBytes()));
  }

  @SuppressWarnings("unused")
  public static KlonObject asNumber(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      Double.valueOf((String) receiver.getData()));
  }

  @SuppressWarnings("unused")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver;
  }

  @SuppressWarnings("unused")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    State state = receiver.getState();
    state.write(receiver.getData());
    state.write("\n");
    return KlonNil.newNil(receiver);
  }

}
