package klon;

@Prototype(name = "Block", parent = "Object")
public class KlonBlock extends KlonObject {

  public KlonBlock() {
    super();
  }

  public KlonBlock(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return data == null ? this : ((Block) data).activate(receiver,
      context, message);
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    if (!nil.equals(receiver.activate(context, context,
      ((Block) (receiver.getData())).getCode()))) {
      return message.getArgument(0)
        .eval(context, context);
    }
    return nil;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    if (nil.equals(receiver.activate(context, context,
      ((Block) (receiver.getData())).getCode()))) {
      return message.getArgument(0)
        .eval(context, context);
    }
    return nil;
  }

  @ExposedAs("whileTrue")
  public static KlonObject whileTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
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
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    while (nil.equals(receiver.activate(context, context,
      ((Block) (receiver.getData())).getCode()))) {
      message.getArgument(0)
        .eval(context, context);
    }
    return nil;
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return receiver.getSlot("String")
      .duplicate(String.valueOf(receiver.getData()));
  }

}
