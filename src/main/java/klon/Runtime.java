package klon;

public class Runtime {

  public KlonObject eval(KlonObject receiver, KlonObject locals, KlonMessage message)
      throws KlonException {
    KlonMessage outer = message;
    while (outer != null) {
      KlonMessage inner = message;
      while (inner != null) {
        receiver = receiver.send(receiver, locals, inner);
        inner = inner.getAttached();
      }
      outer = outer.getNext();
      receiver = locals;
    }
    return receiver;
  }

}
