package klon.reflection;

import java.lang.reflect.Field;
import java.util.List;

import klon.KlonException;
import klon.KlonObject;
import klon.KlonMessage;

public class PrimitiveValue extends KlonObject {

  private Field field;

  public PrimitiveValue(Field field) {
    this.field = field;
  }

  public KlonObject activate(KlonObject receiver, KlonMessage message)
      throws KlonException {
    KlonObject result;
    List<KlonMessage> arguments = message.getArguments();
    if (arguments.size() > 0) {
      KlonObject parameter = arguments.get(0);
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
