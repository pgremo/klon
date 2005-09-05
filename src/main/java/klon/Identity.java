package klon;

import java.io.Serializable;

public class Identity implements Serializable {

  private static final long serialVersionUID = -8518903977702842129L;

  @SuppressWarnings("unused")
  public KlonObject duplicate(KlonObject value) throws KlonObject {
    KlonObject result = new KlonObject();
    result.bind(value);
    result.setData(value.getData());
    result.setIdentity(value.getIdentity());
    return result;
  }

  @SuppressWarnings("unused")
  public int compare(KlonObject o1, KlonObject o2) throws KlonObject {
    return o1.hashCode() - o2.hashCode();
  }

  public String format(KlonObject object) throws KlonObject {
    return object.getSlot("type").getData() + "_0x"
        + Integer.toHexString(object.hashCode());
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    return slot;
  }

}
