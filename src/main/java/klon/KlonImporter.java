package klon;

import java.lang.reflect.Constructor;

@ExposedAs("Importer")
@Bindings("Object")
public class KlonImporter extends KlonObject {

  private static final long serialVersionUID = 1409329651434043476L;

  public KlonImporter() {

  }

  public KlonImporter(State state) {
    super(state);
    type = "Importer";
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @ExposedAs("load")
  public static KlonObject load(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    try {
      message.assertArgumentCount(2);
      String typeName = KlonString.evalAsString(receiver, message, 1);
      Class type = Class.forName(typeName);
      Constructor constructor = type.getDeclaredConstructor(new Class[]{State.class});
      KlonObject prototype = (KlonObject) constructor.newInstance(new Object[]{receiver.getState()});
      prototype.configure(context);
      return prototype;
    } catch (KlonObject e) {
      throw e;
    } catch (Exception e) {
      throw KlonException.newException(receiver, e.getClass()
        .getSimpleName(), e.getMessage(), message);
    }
  }
}
