package klon;

import java.util.ArrayList;
import java.util.List;

@ExposedAs("Block")
@Bindings("Object")
public class KlonBlock extends KlonObject {

  private static final long serialVersionUID = -5339972783724473883L;
  private List<KlonObject> parameters;
  private Message code;
  private KlonObject blockLocals;

  public static KlonObject newBlock(KlonObject root,
      List<KlonObject> parameters, Message code) throws KlonObject {
    KlonBlock result = (KlonBlock) root.getSlot("Block")
      .clone();
    result.setParameters(parameters);
    result.setCode(code);
    return result;
  }

  public KlonBlock(State state) {
    super(state);
  }

  public List<KlonObject> getParameters() {
    return parameters;
  }

  public void setParameters(List<KlonObject> parameters) {
    this.parameters = parameters;
  }

  public Message getCode() {
    return code;
  }

  public void setCode(Message message) {
    this.code = message;
  }

  public KlonObject getBlockLocals() {
    return blockLocals;
  }

  public void setBlockLocals(KlonObject blockLocals) {
    this.blockLocals = blockLocals;
  }

  @Override
  public KlonObject clone() {
    KlonBlock result = new KlonBlock(state);
    result.bind(this);
    if (code != null) {
      result.setParameters(new ArrayList<KlonObject>(parameters));
      result.setCode((Message) code.clone());
    }
    return result;
  }

  @Override
  public String toString() {
    String result;
    if (code == null) {
      result = super.toString();
    } else {
      StringBuilder buffer = new StringBuilder();
      if (blockLocals == null) {
        buffer.append("method");
      } else {
        buffer.append("block");
      }
      buffer.append("(");
      for (int i = 0; i < parameters.size(); i++) {
        if (i > 0) {
          buffer.append(", ");
        }
        buffer.append(parameters.get(i)
          .getData());
      }
      if (parameters.size() > 0) {
        buffer.append(", ");
      }
      buffer.append(code)
        .append(")");
      result = buffer.toString();
    }
    return result;
  }

  @Override
  public String getType() {
    return "Block";
  }

  @SuppressWarnings("unchecked")
  @Override
  public KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    KlonObject result;
    if (code == null) {
      result = slot;
    } else {
      KlonObject scope = blockLocals;
      if (scope == null) {
        scope = receiver;
      }

      KlonObject locals = receiver.getSlot("Locals")
        .clone();

      locals.setSlot("self", receiver);
      locals.setSlot("receiver", scope);
      locals.setSlot("sender", context);
      locals.setSlot("block", slot);
      locals.setSlot("message", KlonMessage.newMessage(receiver, message));

      locals.setData(receiver.getData());

      int limit = Math.min(message.getArgumentCount(), parameters.size());
      int i = 0;
      for (; i < limit; i++) {
        locals.setSlot((String) parameters.get(i)
          .getData(), message.eval(context, i));
      }
      KlonObject nil = KlonNil.newNil(receiver);
      for (; i < parameters.size(); i++) {
        locals.setSlot((String) parameters.get(i)
          .getData(), nil);
      }
      result = nil;
      try {
        result = code.eval(locals, locals);
      } catch (KlonObject e) {
        ((List<KlonObject>) e.getSlot("stackTrace")
          .getData()).add(KlonString.newString(receiver, message.toString()));
        throw e;
      }

    }
    return result;
  }

  @ExposedAs("parameters")
  public static KlonObject parameters(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonList.newList(receiver, ((KlonBlock) receiver).getParameters());
  }

  @ExposedAs("code")
  public static KlonObject code(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonMessage.newMessage(receiver, ((KlonBlock) receiver).getCode());
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = KlonNil.newNil(receiver);
    if (!result.equals(receiver.activate(context, context,
      ((KlonBlock) receiver).getCode()))) {
      result = message.getArgument(0)
        .eval(context, context);
    }
    return result;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = KlonNil.newNil(receiver);
    if (result.equals(receiver.activate(context, context,
      ((KlonBlock) receiver).getCode()))) {
      result = message.getArgument(0)
        .eval(context, context);
    }
    return result;
  }

  @ExposedAs("whileTrue")
  public static KlonObject whileTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject nil = KlonNil.newNil(receiver);
    while (!nil.equals(receiver.activate(context, context,
      ((KlonBlock) receiver).getCode()))) {
      message.getArgument(0)
        .eval(context, context);
    }
    return nil;
  }

  @ExposedAs("whileFalse")
  public static KlonObject whileFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject nil = KlonNil.newNil(receiver);
    while (nil.equals(receiver.activate(context, context,
      ((KlonBlock) receiver).getCode()))) {
      message.getArgument(0)
        .eval(context, context);
    }
    return nil;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, receiver.toString());
  }

}
