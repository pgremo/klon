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
      List<KlonObject> parameters, KlonMessage message) throws KlonObject {
    KlonObject result = root.getSlot("Function")
      .clone();
    Function value = new Function(parameters, message);
    result.setData(value);
    return result;
  }

  public KlonFunction() {

  }

  public KlonFunction(State state) {
    super(state);
    type = "Function";
  }

  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    super.readExternal(in);
    data = in.readObject();
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(data);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonFunction(state);
    result.bind(this);
    Function source = (Function) data;
    if (source != null) {
      result.setData(source.clone());
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    Function value = (Function) slot.getData();
    KlonObject result;
    if (value == null) {
      result = slot;
    } else {
      KlonObject scope = ((Function) slot.getData()).getScope();
      if (scope == null) {
        scope = receiver;
      }

      KlonObject locals = receiver.getState()
        .getLocals()
        .clone();

      locals.setSlot("self", receiver);
      locals.setSlot("receiver", scope);
      locals.setSlot("sender", context);
      locals.setSlot("slot", slot);
      locals.setSlot("message", message);

      locals.setData(receiver.getData());

      List<KlonObject> parameters = value.getParameters();
      int limit = Math.min(message.getArgumentCount(), parameters.size());
      int i = 0;
      for (; i < limit; i++) {
        locals.setSlot((String) parameters.get(i)
          .getData(), message.evalArgument(context, i));
      }
      KlonObject nil = KlonNil.newNil(receiver);
      for (; i < parameters.size(); i++) {
        locals.setSlot((String) parameters.get(i)
          .getData(), nil);
      }
      result = nil;
      try {
        result = value.getMessage()
          .eval(locals, locals);
      } catch (KlonObject e) {
        ((List<KlonObject>) e.getSlot("stackTrace")
          .getData()).add(KlonString.newString(receiver, message.getData()
          .toString()));
        throw e;
      }
    }
    return result;
  }

  @ExposedAs("parameters")
  public static KlonObject parameters(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonList.newList(receiver,
      ((Function) receiver.getData()).getParameters());
  }

  @SuppressWarnings("unused")
  @ExposedAs("message")
  public static KlonObject message(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return ((Function) receiver.getData()).getMessage();
  }

  @SuppressWarnings("unused")
  @ExposedAs("setMessage")
  public static KlonObject setMessage(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    ((Function) receiver.getData()).setMessage(message.getArgument(0));
    return receiver;
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject result = KlonNil.newNil(receiver);
    if (!result.equals(receiver.activate(context, context,
      ((Function) (receiver.getData())).getMessage()))) {
      result = message.getArgument(0)
        .eval(context, context);
    }
    return result;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject result = KlonNil.newNil(receiver);
    if (result.equals(receiver.activate(context, context,
      ((Function) (receiver.getData())).getMessage()))) {
      result = message.getArgument(0)
        .eval(context, context);
    }
    return result;
  }

  @ExposedAs("whileTrue")
  public static KlonObject whileTrue(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject nil = KlonNil.newNil(receiver);
    KlonMessage code = message.getArgument(0);
    while (!nil.equals(receiver.activate(context, context,
      ((Function) (receiver.getData())).getMessage()))) {
      code.eval(context, context);
    }
    return nil;
  }

  @ExposedAs("whileFalse")
  public static KlonObject whileFalse(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonObject nil = KlonNil.newNil(receiver);
    KlonMessage code = message.getArgument(0);
    while (nil.equals(receiver.activate(context, context,
      ((Function) (receiver.getData())).getMessage()))) {
      code.eval(context, context);
    }
    return nil;
  }

  @SuppressWarnings("unused")
  @ExposedAs("scope")
  public static KlonObject scope(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result = ((Function) receiver.getData()).getScope();
    if (result == null) {
      result = KlonNil.newNil(receiver);
    }
    return result;
  }

  @ExposedAs("setScope")
  public static KlonObject setScope(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    ((Function) receiver.getData()).setScope(message.evalArgument(context, 0));
    return receiver;
  }

  @ExposedAs("code")
  public static KlonObject code(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return code(receiver, context, message);
  }

}
