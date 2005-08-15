package klon.reflection;

import klon.KlonException;
import klon.KlonMessage;
import klon.KlonObject;

public interface Slot {

  KlonObject activate(Identity receiver, KlonMessage message)
      throws KlonException;

}