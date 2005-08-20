package klon;

import java.util.List;

public class KlonList extends KlonObject<List> {

  public KlonList() {
    super();
  }

  public KlonList(List value) throws KlonException {
    super(Klon.ROOT.getSlot("List"), value);
  }

  public KlonList(KlonObject parent, List attached) {
    super(parent, attached);
  }

  @Override
  public void configure() throws KlonException {
    slots.put("parent", Klon.ROOT.getSlot("Object"));
    Configurator.configure(KlonList.class, this);
  }

  @Override
  public KlonObject clone() {
    return new KlonList(this, primitive);
  }

  @Override
  public String toString() {
    return primitive.toString();
  }

}
