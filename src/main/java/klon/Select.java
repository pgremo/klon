package klon;

import java.util.List;

public class Select extends Closure {

  private List<KlonObject> list;
  private KlonObject nil;

  @SuppressWarnings("unchecked")
  public Select(KlonObject result) {
    super(result);
    list = (List<KlonObject>) result.getData();
    nil = KlonNil.newNil(result);
  }

  @Override
  public boolean invoke(KlonObject current, KlonObject context)
      throws KlonObject {
    if (!nil.equals(KlonMessage.eval(code, context, context))) {
      list.add(current);
    }
    return false;
  }

}
