package klon;

import java.util.List;

@ExposedAs("Block")
@Bindings("Object")
public class KlonBlock extends KlonObject {

  private static final long serialVersionUID = -5339972783724473883L;

  public static KlonObject newBlock(KlonObject root, Block value)
      throws KlonObject {
    KlonObject result = root.getSlot("Block")
      .clone();
    result.setData(value);
    return result;
  }

  public KlonBlock(State state) {
    super(state);
  }

  @Override
  public KlonObject clone() {
    KlonObject result = new KlonBlock(state);
    result.bind(this);
    Block source = (Block) data;
    if (source != null) {
      result.setData(source.clone());
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
    Block value = (Block) slot.getData();
    KlonObject result;
    if (value == null) {
      result = slot;
    } else {
      KlonObject scope = ((Block) slot.getData()).getBlockLocals();
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

      List<KlonObject> parameters = value.getParameters();
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
        result = value.getCode()
          .eval(locals, locals);
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
    return KlonList.newList(receiver,
      ((Block) receiver.getData()).getParameters());
  }

  @ExposedAs("code")
  public static KlonObject code(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonMessage.newMessage(receiver,
      ((Block) receiver.getData()).getCode());
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = KlonNil.newNil(receiver);
    if (!result.equals(receiver.activate(context, context,
      ((Block) (receiver.getData())).getCode()))) {
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
      ((Block) (receiver.getData())).getCode()))) {
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
      ((Block) (receiver.getData())).getCode()))) {
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
      ((Block) (receiver.getData())).getCode()))) {
      message.getArgument(0)
        .eval(context, context);
    }
    return nil;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
