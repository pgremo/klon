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
    StringBuilder result = new StringBuilder();
    for (Object current : primitive) {
      if (result.length() > 0) {
        result.append(", ");
      }
      result.append(current.toString());
    }
    return result.toString();
  }

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    StringBuilder result = new StringBuilder();
    for (Object current : (Iterable) receiver.getPrimitive()) {
      if (result.length() > 0) {
        result.append(", ");
      }
      result.append(current.toString());
    }
    System.out.print(result.toString());
    return receiver.getSlot("Nil");
  }

}
