package klon;

import java.util.Set;

public class KlonSet extends KlonObject<Set> {

  public KlonSet() {
    super();
  }

  public KlonSet(Set value) throws KlonException {
    super(Klon.ROOT.getSlot("List"), value);
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
    System.out.println(result.toString());
    return receiver.getSlot("Nil");
  }

}
