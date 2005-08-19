package klon;

import java.util.Set;

public class KlonSet extends KlonObject<Set> {

  public KlonSet() {
    super();
  }

  public KlonSet(Set attached) {
    super(attached);
  }

  public KlonSet(KlonObject parent, Set attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonSet.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonSet(this, primitive);
  }

  @Override
  public String toString() {
    return primitive.toString();
  }

}
