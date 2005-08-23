package klon;

import java.util.Set;

@Prototype(name = "Set", parent = "Object")
public class KlonSet extends KlonObject<Set> {

  public KlonSet() throws KlonException {
    this(null);
  }

  public KlonSet(Set value) throws KlonException {
    this(Klon.ROOT.getSlot("List"), value);
  }

  public KlonSet(KlonObject parent, Set attached) {
    super(parent, attached);
    this.prototype = KlonSet.class.getAnnotation(Prototype.class);
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonSet.class);
  }

  @Override
  public KlonObject clone() {
    return new KlonSet(this, primitive);
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
