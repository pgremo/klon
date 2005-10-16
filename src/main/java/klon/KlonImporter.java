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
  }

  @Override
  public KlonObject clone() {
    return this;
  }

  @Override
  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("load", KlonNativeMethod.newNativeMethod(root, KlonImporter.class
        .getMethod("load", KlonNativeMethod.PARAMETER_TYPES)));
  }

  public static KlonObject load(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    try {
      KlonMessage.assertArgumentCount(message, 2);
      String typeName = KlonString.evalAsString(receiver, message, 1);
      Class type = Class.forName(typeName);
      Constructor constructor = type
          .getDeclaredConstructor(new Class[] { State.class });
      KlonObject prototype = (KlonObject) constructor
          .newInstance(new Object[] { receiver.getState() });
      prototype.prototype();
      return prototype;
    } catch (KlonObject e) {
      throw e;
    } catch (Exception e) {
      throw KlonException.newException(receiver, e.getClass().getSimpleName(),
          e.getMessage(), message);
    }
  }
}
