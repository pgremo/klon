package klon;

import java.lang.reflect.Method;

@Prototype(name = "ExposedMethod", parent = "Object")
public class KlonExposedMethod extends KlonObject {

  public KlonExposedMethod() {
    super();
  }

  public KlonExposedMethod(KlonObject parent, Object attached) {
    super(parent, attached);
    this.prototype = KlonExposedMethod.class.getAnnotation(Prototype.class);
  }

  @Override
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonException {
    KlonObject result = null;
    if (primitive == null) {
      result = getSlot("Nil");
    } else {
      try {
        result = (KlonObject) ((Method) primitive).invoke(null, receiver,
            context, message);
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
  public KlonObject clone(Object subject) {
    return new KlonExposedMethod(this, subject);
  }

}
