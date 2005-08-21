package klon;

public class KlonBlock extends KlonObject<Block> {

  public KlonBlock() {
    super();
  }

  public KlonBlock(Block value) throws KlonException {
    super(Klon.ROOT.getSlot("Block"), value);
  }

  public KlonBlock(KlonObject parent, Block attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonBlock.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonBlock(this, primitive);
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    return primitive == null ? getSlot("Nil") : primitive.activate(receiver,
        context, message);
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    if (!nil.equals(receiver.activate(context, context, ((Block) (receiver
        .getPrimitive())).getCode()))) {
      return message.getArgument(0).eval(context, context);
    }
    return nil;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    if (nil.equals(receiver.activate(context, context, ((Block) (receiver
        .getPrimitive())).getCode()))) {
      return message.getArgument(0).eval(context, context);
    }
    return nil;
  }

  @ExposedAs("whileTrue")
  public static KlonObject whileTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    while (!nil.equals(receiver.activate(context, context, ((Block) (receiver
        .getPrimitive())).getCode()))) {
      message.getArgument(0).eval(context, context);
    }
    return nil;
  }

  @ExposedAs("whileFalse")
  public static KlonObject whileFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject nil = receiver.getSlot("Nil");
    while (nil.equals(receiver.activate(context, context, ((Block) (receiver
        .getPrimitive())).getCode()))) {
      message.getArgument(0).eval(context, context);
    }
    return nil;
  }

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    System.out.println(receiver.getPrimitive());
    return receiver.getSlot("Nil");
  }

}
