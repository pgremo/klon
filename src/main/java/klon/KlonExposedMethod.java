package klon;

import java.lang.reflect.Method;

@Prototype(name = "ExposedMethod", parent = "Object")
public class KlonExposedMethod extends KlonObject<Method> {

  public KlonExposedMethod() {
    super();
  }

  public KlonExposedMethod(Method attached) throws KlonException {
    super(Klon.ROOT.getSlot("Object"), attached);
  }

  public KlonExposedMethod(KlonObject parent, Method attached) {
    super(parent, attached);
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = null;
    if (primitive == null) {
      result = getSlot("Nil");
    } else {
      try {
        result = (KlonObject) primitive
            .invoke(null, receiver, context, message);
      } catch (Exception e) {
        throw new KlonException(e);
      }
    }
    return result;
  }

  @Override
  public void configure(KlonObject root) throws KlonException {
    Configurator.configure(root, this, KlonExposedMethod.class);
  }

  @Override
  public KlonObject clone() {
    return new KlonExposedMethod(this, primitive);
  }

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    System.out.println(receiver.getPrimitive());
    return receiver.getSlot("Nil");
  }

}
