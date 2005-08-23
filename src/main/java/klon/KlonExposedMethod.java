package klon;

import java.lang.reflect.Method;

@Prototype(name = "ExposedMethod", parent = "Object")
public class KlonExposedMethod extends KlonObject<Method> {

  public KlonExposedMethod() {
    super();
  }

  public KlonExposedMethod(Method attached) throws KlonException {
    super(Klon.ROOT.getSlot("ExposedMethod"), attached);
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
        Throwable cause = e.getCause();
        if (cause instanceof KlonException) {
          throw (KlonException) cause;
        }
        throw new KlonException(cause);
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

}
