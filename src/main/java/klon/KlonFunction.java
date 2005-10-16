package klon;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

@ExposedAs("Function")
@Bindings("Object")
public class KlonFunction extends KlonObject {

  private static final long serialVersionUID = -5339972783724473883L;

  public static KlonObject newFunction(KlonObject root,
      List<KlonObject> parameters, KlonObject message) throws KlonObject {
    KlonObject result = root.getSlot("Function").clone();
    Function value = new Function(parameters, message);
    result.setData(value);
    return result;
  }

  public KlonFunction() {

  }

  public KlonFunction(State state) {
    super(state);
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("parameters", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("parameters",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("message", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("message",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("setMessage", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("setMessage",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifTrue", KlonNativeMethod.newNativeMethod(root, KlonFunction.class
        .getMethod("ifTrue", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifFalse", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("ifFalse",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("whileTrue", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("whileTrue",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("whileFalse", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("whileFalse",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("scope", KlonNativeMethod.newNativeMethod(root, KlonFunction.class
        .getMethod("scope", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("setScope", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("setScope",
            KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("code", KlonNativeMethod.newNativeMethod(root, KlonFunction.class
        .getMethod("code", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asString", KlonNativeMethod.newNativeMethod(root,
        KlonFunction.class.getMethod("asString",
            KlonNativeMethod.PARAMETER_TYPES)));
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
  public KlonObject clone() {
    KlonObject result = new KlonFunction(getState());
    result.bind(this);
    Function source = (Function) getData();
    if (source != null) {
      result.setData(source.clone());
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    Function value = (Function) getData();
    KlonObject result;
    if (value == null) {
      result = this;
    } else {
      KlonObject scope = value.getScope();
      if (scope == null) {
        scope = receiver;
      }

      KlonObject locals = receiver.getState().getLocalsObject().clone();

      locals.setSlot("self", receiver);
      locals.setSlot("receiver", scope);
      locals.setSlot("sender", context);
      locals.setSlot("slot", this);
      locals.setSlot("message", message);

      locals.setData(receiver.getData());

      List<KlonObject> parameters = value.getParameters();
      int limit = Math.min(KlonMessage.getArgumentCount(message), parameters
          .size());
      for (int i = 0; i < limit; i++) {
        locals.setSlot((String) parameters.get(i).getData(), KlonMessage
            .evalArgument(message, context, i));
      }
      try {
        result = KlonMessage.eval(value.getMessage(), locals, locals);
      } catch (KlonObject e) {
        ((List<KlonObject>) e.getSlot("stackTrace").getData()).add(KlonString
            .newString(receiver, message.getData().toString()));
        throw e;
      }
    }
    return result;
  }

  public static KlonObject parameters(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonList.newList(receiver, ((Function) receiver.getData())
        .getParameters());
  }

  @SuppressWarnings("unused")
  public static KlonObject message(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return ((Function) receiver.getData()).getMessage();
  }

  @SuppressWarnings("unused")
  public static KlonObject setMessage(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    ((Function) receiver.getData()).setMessage(KlonMessage.getArgument(message,
        0));
    return receiver;
  }

  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject result = KlonNil.newNil(receiver);
    if (!result.equals(receiver.activate(context, context,
        ((Function) (receiver.getData())).getMessage()))) {
      result = KlonMessage.eval(KlonMessage.getArgument(message, 0), context,
          context);
    }
    return result;
  }

  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject result = KlonNil.newNil(receiver);
    if (result.equals(receiver.activate(context, context, ((Function) (receiver
        .getData())).getMessage()))) {
      result = KlonMessage.eval(KlonMessage.getArgument(message, 0), context,
          context);
    }
    return result;
  }

  public static KlonObject whileTrue(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject nil = KlonNil.newNil(receiver);
    KlonObject code = KlonMessage.getArgument(message, 0);
    while (!nil.equals(receiver.activate(context, context,
        ((Function) (receiver.getData())).getMessage()))) {
      KlonMessage.eval(code, context, context);
    }
    return nil;
  }

  public static KlonObject whileFalse(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject nil = KlonNil.newNil(receiver);
    KlonObject code = KlonMessage.getArgument(message, 0);
    while (nil.equals(receiver.activate(context, context, ((Function) (receiver
        .getData())).getMessage()))) {
      KlonMessage.eval(code, context, context);
    }
    return nil;
  }

  @SuppressWarnings("unused")
  public static KlonObject scope(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result = ((Function) receiver.getData()).getScope();
    if (result == null) {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  public static KlonObject setScope(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    ((Function) receiver.getData()).setScope(KlonMessage.evalArgument(message,
        context, 0));
    return receiver;
  }

  public static KlonObject code(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return code(receiver, context, message);
  }

}
