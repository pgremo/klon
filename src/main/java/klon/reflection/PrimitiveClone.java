package klon.reflection;

import java.lang.reflect.Constructor;

import klon.KlonException;
import klon.KlonObject;
import klon.KlonMessage;

public class PrimitiveClone extends KlonObject {

  private Constructor constructor;

  public PrimitiveClone(Constructor constructor) {
    this.constructor = constructor;
  }

  public KlonObject activate(KlonObject receiver, KlonMessage message)
      throws KlonException {
    Object result = null;
    try {
      result = constructor.newInstance(message.getDownArguments());
    } catch (Exception e) {
      throw new KlonException(e);
    }
    return Up.UP.up(result);
  }

  public String toString() {
    return "Primitive Clone";
  }
}
