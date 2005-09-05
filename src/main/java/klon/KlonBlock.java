package klon;

import java.util.List;

@Prototype(name = "Block", parent = "Object")
public class KlonBlock extends Identity {

  private static final long serialVersionUID = -5339972783724473883L;

  public static KlonObject newBlock(KlonObject root, Block value)
      throws KlonObject {
    KlonObject result = root.getSlot("Block").duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    result.setIdentity(new KlonBlock());
    return result;
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
      KlonObject scope = KlonLocals.newLocals(receiver, receiver);
      String[] parameters = value.getParameters();
      int limit = Math.min(message.getArgumentCount(), parameters.length);
      int i = 0;
      for (; i < limit; i++) {
        scope.setSlot(parameters[i], message.eval(context, i));
      }
      KlonObject nil = receiver.getSlot("Nil");
      for (; i < parameters.length; i++) {
        scope.setSlot(parameters[i], nil);
      }
      result = nil;
      try {
        result = value.getCode().eval(scope, scope);
      } catch (KlonObject e) {
        ((List<KlonObject>) e.getSlot("stackTrace").getData()).add(KlonString
            .newString(receiver, message.toString()));
        throw e;
      }

    }
    return result;
  }

  @ExposedAs("code")
  public static KlonObject code(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonMessage.newMessage(receiver, ((Block) receiver.getData())
        .getCode());
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    if (!result.equals(receiver.activate(context, context, ((Block) (receiver
        .getData())).getCode()))) {
      result = message.getArgument(0).eval(context, context);
    }
    return result;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    if (result.equals(receiver.activate(context, context, ((Block) (receiver
        .getData())).getCode()))) {
      result = message.getArgument(0).eval(context, context);
    }
    return result;
  }

  @ExposedAs("whileTrue")
  public static KlonObject whileTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject nil = receiver.getSlot("Nil");
    while (!nil.equals(receiver.activate(context, context, ((Block) (receiver
        .getData())).getCode()))) {
      message.getArgument(0).eval(context, context);
    }
    return nil;
  }

  @ExposedAs("whileFalse")
  public static KlonObject whileFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject nil = receiver.getSlot("Nil");
    while (nil.equals(receiver.activate(context, context, ((Block) (receiver
        .getData())).getCode()))) {
      message.getArgument(0).eval(context, context);
    }
    return nil;
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, String.valueOf(receiver.getData()));
  }

}
