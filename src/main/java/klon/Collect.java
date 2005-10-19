package klon;

import java.util.List;

public class Collect extends Closure {

  private List<KlonObject> list;

  @SuppressWarnings("unchecked")
  public Collect(KlonObject result) {
    super(result);
    list = (List<KlonObject>) result.getData();
  }

  @Override
  public boolean invoke(KlonObject current, KlonObject context)
      throws KlonObject {
    list.add(KlonMessage.eval(code, context, context));
    return false;
  }

}
