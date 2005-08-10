package klon.reflection;

import java.util.HashMap;
import java.util.Map;

import klon.KlonObject;

public class TableBuilder {

  private boolean onlyExposed;
  private Map<String, KlonObject> exposed = new HashMap<String, KlonObject>();
  private Map<String, KlonObject> unExposed = new HashMap<String, KlonObject>();

  public void addSlot(String pattern, KlonObject slot, boolean isExposed) {
    onlyExposed = onlyExposed | isExposed;
    if (isExposed) {
      exposed.put(pattern, slot);
    } else {
      unExposed.put(pattern, slot);
    }
  }

  public Map<String, KlonObject> buildTable() {
    Map<String, KlonObject> result = new HashMap<String, KlonObject>(
      exposed.size() + unExposed.size());
    result.putAll(exposed);
    if (!onlyExposed) {
      result.putAll(unExposed);
    }
    return result;
  }

}
