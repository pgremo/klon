package klon;

import klon.reflection.ExposedAs;

public class Nil extends KlonObject {

  @Override
  public KlonObject activate(KlonObject receiver, Message message)
      throws MessageNotUnderstood {
    return Klon.ROOT.getSlot("Nil");
  }

  @ExposedAs("==")
  public KlonObject equals(KlonObject locals, Message message)
      throws MessageNotUnderstood {
    return equals(message.getArguments()
      .get(0)) ? Klon.ROOT : Klon.ROOT.getSlot("Nil");
  }

  @Override
  public String toString() {
    return "Nil";
  }

}
