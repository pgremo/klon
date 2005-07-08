package klon.reflection;

import java.lang.reflect.Field;

import klon.KlonException;
import klon.KlonObject;
import klon.Message;

public class PrimitiveValue extends KlonObject {

  private Field field;

  public PrimitiveValue(Field field) {
    this.field = field;
  }

  public KlonObject activate(KlonObject receiver, Message message)
      throws KlonException {
    KlonObject result;
    KlonObject[] arguments = message.getArguments();
    if (arguments.length > 0) {
      KlonObject parameter = arguments[0];
      try {
        field.set(receiver, parameter.down());
        result = parameter;
      } catch (Exception e) {
        throw new KlonException(e);
      }
    } else {
      try {
        result = Up.UP.up(field.get(receiver));
      } catch (Exception e) {
        throw new KlonException(e);
      }
    }
    return result;
  }

  public String toString() {
    return "Primitive Value";
  }

}
