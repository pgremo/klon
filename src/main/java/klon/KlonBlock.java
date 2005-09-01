package klon;

@Prototype(name = "Block", parent = "Object")
public class KlonBlock {

  public static KlonObject newBlock(KlonObject root, Block value)
      throws KlonObject {
    KlonObject result = root.getSlot("Block")
      .duplicate();
    result.setData(value);
    return result;
  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonBlock.class);
    Configurator.setDuplicator(result, KlonBlock.class);
    Configurator.setFormatter(result, KlonBlock.class);
    return result;
  }

  public static KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    Object value = slot.getData();
    return value == null ? slot : ((Block) value).activate(receiver, context,
      message);
  }

  @ExposedAs("code")
  public static KlonObject code(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver,
      ((Block) receiver.getData()).getCode()
        .toString());
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
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
    KlonObject result = receiver.getSlot("Nil");
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
    KlonObject nil = receiver.getSlot("Nil");
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
    KlonObject nil = receiver.getSlot("Nil");
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
