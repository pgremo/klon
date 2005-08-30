package klon;

@Prototype(name = "Block", parent = "Object")
public class KlonBlock extends KlonObject {

  private static final long serialVersionUID = 8013887125117513346L;

  public KlonBlock() {
    super();
  }

  public KlonBlock(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  public KlonBlock newBlock(Block value) throws KlonException {
    KlonBlock result = (KlonBlock) duplicate();
    result.data = value;
    return result;
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return data == null ? this : ((Block) data).activate(receiver, context,
      message);
  }

  @ExposedAs("code")
  public static KlonObject code(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return ((KlonString) receiver.getSlot("String")).newString(((Block) receiver.getData()).getCode()
      .toString());
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
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
      Message message) throws KlonException {
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
    return ((KlonString) receiver.getSlot("String")).newString(String.valueOf(receiver.getData()));
  }

}
