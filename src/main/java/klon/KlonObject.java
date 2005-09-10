package klon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KlonObject extends Exception implements Cloneable,
    Comparable<KlonObject> {

  private static final long serialVersionUID = 5234708348712278569L;

  private List<KlonObject> bindings = new LinkedList<KlonObject>();
  private Map<String, KlonObject> slots = new HashMap<String, KlonObject>();
  private Identity identity;

  public void configure(KlonObject root, Class<? extends Object> type)
      throws Exception {
    Configurator.setSlots(root, this, type);
  }

  public KlonObject duplicate() throws KlonObject {
    return identity.duplicate(this);
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return identity.activate(this, receiver, context, message);
  }

  public void setData(Object value) {
    identity.setData(value);
  }

  public Object getData() {
    return identity.getData();
  }

  public Identity getIdentity() {
    return identity;
  }

  public void setIdentity(Identity identity) {
    this.identity = identity;
  }

  public List<KlonObject> getBindings() {
    return bindings;
  }

  public Map<String, KlonObject> getSlots() {
    return slots;
  }

  public void setSlot(String name, KlonObject value) {
    slots.put(name, value);
  }

  public KlonObject updateSlot(String name, KlonObject value) throws KlonObject {
    KlonObject result = getSlot(name);
    if (result != null) {
      setSlot(name, value);
    }
    return result;
  }

  private boolean contains(Collection<KlonObject> collection, KlonObject object) {
    boolean found = false;
    int target = System.identityHashCode(object);
    Iterator<KlonObject> iterator = collection.iterator();
    while (!found && iterator.hasNext()) {
      found = target == System.identityHashCode(iterator.next());
    }
    return found;
  }

  private KlonObject getSlot(String name, Collection<KlonObject> searchPath)
      throws KlonObject {
    KlonObject result = null;
    if (slots.containsKey(name)) {
      result = slots.get(name);
    } else {
      Iterator<KlonObject> iterator = bindings.iterator();
      while (result == null && iterator.hasNext()) {
        KlonObject current = iterator.next();
        if (!contains(searchPath, current)) {
          searchPath.add(current);
          result = current.getSlot(name, searchPath);
        }
      }
    }
    return result;
  }

  public KlonObject getSlot(String name) throws KlonObject {
    KlonObject result = getSlot(name, new LinkedList<KlonObject>());
    if (result == null) {
      KlonObject self = getSlot("self", new LinkedList<KlonObject>());
      if (self != null) {
        result = self.getSlot(name);
      }
    }
    return result;
  }

  public void removeSlot(String name) {
    slots.remove(name);
  }

  public void bind(KlonObject object) {
    if (!isBound(object)) {
      bindings.add(object);
    }
  }

  private boolean isBound(KlonObject object, Collection<KlonObject> searchPath) {
    boolean found = false;
    Iterator<KlonObject> iterator = bindings.iterator();
    while (!found && iterator.hasNext()) {
      KlonObject current = iterator.next();
      found = object == current;
      if (!found && !contains(searchPath, current)) {
        searchPath.add(current);
        found = current.isBound(object, searchPath);
      }
    }
    return found;
  }

  public boolean isBound(KlonObject object) {
    return isBound(object, new LinkedList<KlonObject>());
  }

  public void unbind(KlonObject object) {
    bindings.remove(object);
  }

  @SuppressWarnings("unchecked")
  public KlonObject perform(KlonObject context, Message message)
      throws KlonObject {
    String name = (String) message.getSelector().getData();
    KlonObject slot = getSlot(name);
    if (slot == null && "Locals".equals(context.getSlot("type"))) {
      slot = context.getSlot(name);
    }
    if (slot == null) {
      slot = getSlot("forward");
    }
    if (slot == null) {
      KlonObject e = KlonException.newException(this, "Invalid Slot", name
          + " does not exist", message);
      ((List<KlonObject>) e.getSlot("stackTrace").getData()).add(KlonString
          .newString(context, message.toString()));
      throw e;
    }
    return slot.activate(this, context, message);
  }

  // ================
  // java.lang.Comparable
  // ================

  public int compareTo(KlonObject o) {
    return identity.compareTo(o.getIdentity());
  }

  // ================
  // java.lang.Object
  // ================

  @Override
  public Object clone() {
    try {
      return duplicate();
    } catch (KlonObject e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean equals(Object obj) {
    boolean result;
    try {
      result = obj instanceof KlonObject
          && identity.compareTo(((KlonObject) obj).getIdentity()) == 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  @Override
  public int hashCode() {
    return identity.hashCode();
  }

  @Override
  public String toString() {
    String result = null;
    try {
      result = identity.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  // ================
  // java.lang.Exception
  // ================

  public String getMessage() {
    StringBuilder result = new StringBuilder();
    try {
      KlonObject name = getSlot("name");
      if (name != null) {
        result.append(name.getData());
        KlonObject description = getSlot("description");
        if (description != null) {
          result.append(":").append(description.getData());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result.toString();
  }

}
