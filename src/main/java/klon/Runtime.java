package klon;

public class Runtime extends KlonObject {

  public static KlonObject eval(KlonObject receiver, KlonObject locals,
      KlonMessage message) throws KlonException {
    KlonObject result = null;
    for (KlonMessage outer = message; outer != null; outer = outer.getNext()) {
      for (KlonMessage inner = outer; inner != null; inner = inner
          .getAttached()) {
        result = inner.getLiteral();
        if (result == null) {
          result = KlonObject.send(receiver, locals, inner);
        }
        receiver = result;
      }
      receiver = locals;
    }
    return receiver;
  }

}
