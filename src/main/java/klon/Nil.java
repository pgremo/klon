package klon;

import klon.reflection.ExposedAs;

public class Nil extends KlonObject {

  public Nil() {
    super();
  }

  public Nil(Object attached) throws KlonException {
    super(Klon.ROOT.getSlot("Nil"), attached);
  }

  public Nil(KlonObject parent, Object attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    parent = Klon.ROOT.getSlot("Object");
    Configurator.configure(Nil.class, slots);
  }

  @Override
  public KlonObject clone() {
    return new Nil(this, attached);
  }

  @Override
  public String toString() {
    return "Nil";
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, Message message)
      throws KlonException {
    return receiver.equals(message.eval(receiver, 0))
        ? Klon.ROOT.getSlot("Klon")
        : Klon.ROOT.getSlot("Nil");
  }

}
