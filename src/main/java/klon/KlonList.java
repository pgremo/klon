package klon;

import java.util.List;

@Prototype(name = "List", parent = "Object")
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
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonList.class);
  }

  @Override
  public KlonObject clone() {
    return new KlonList(this, primitive);
  }

  @Override
  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    StringBuilder result = new StringBuilder();
    for (Object current : (Iterable) receiver.getPrimitive()) {
      if (result.length() > 0) {
        result.append(", ");
      }
      result.append(current.toString());
    }
    return new KlonString(result.toString());
  }

}
