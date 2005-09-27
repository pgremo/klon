package klon;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

@ExposedAs("Object")
public class KlonObject extends Exception
    implements
      Cloneable,
      Externalizable,
      Comparable<KlonObject> {

  private static final long serialVersionUID = 5234708348712278569L;

  protected State state;
  private List<KlonObject> bindings = new ArrayList<KlonObject>();
  private Map<String, KlonObject> slots = new HashMap<String, KlonObject>(50);
  private boolean activatable;
  protected Object data;

  public KlonObject() {

  }

  public KlonObject(State state) {
    this.state = state;
  }

  public void configure(KlonObject root) throws Exception {
    Configurator.configure(root, this);
  }

  public String getName() {
    ExposedAs exposedAs = getClass().getAnnotation(ExposedAs.class);
    if (exposedAs == null) {
      throw new RuntimeException(getClass() + " is not exposed.");
    }
    return exposedAs.value()[0];
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return activate(this, receiver, context, message);
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
    return getSlot(name, new ArrayList<KlonObject>(20));
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
    return isBound(object, new ArrayList<KlonObject>(20));
  }

  public void unbind(KlonObject object) {
    bindings.remove(object);
  }

  @SuppressWarnings("unchecked")
  public KlonObject perform(KlonObject context, KlonMessage message)
      throws KlonObject {
    String name = (String) message.getSelector()
      .getData();
    KlonObject slot = getSlot(name);
    if (slot == null) {
      slot = getSlot("forward");
    }
    if (slot == null) {
      forward(this, context, message);
    }
    return slot.activate(this, context, message);
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object value) {
    this.data = value;
  }

  public boolean isActivatable() {
    return activatable;
  }

  public void setActivatable(boolean activatable) {
    this.activatable = activatable;
  }

  public String getType() {
    return "Object";
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    KlonObject result = slot;
    if (activatable) {
      KlonObject activate = slot.getSlot("activate");
      if (activate != null) {
        result = activate.activate(slot, receiver, message);
      }
    }
    return result;
  }

  // ================
  // java.io.Externalizable
  // ================

  @SuppressWarnings("unchecked")
  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    state = (State) in.readObject();
    bindings = (List<KlonObject>) in.readObject();
    slots = (Map<String, KlonObject>) in.readObject();
    activatable = (Boolean) in.readObject();
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(state);
    out.writeObject(bindings);
    out.writeObject(slots);
    out.writeObject(activatable);
  }

  // ================
  // java.lang.Comparable
  // ================

  public int compareTo(KlonObject o) {
    return hashCode() - o.hashCode();
  }

  // ================
  // java.lang.Object
  // ================

  @Override
  public String toString() {
    return getType() + "_0x"
        + Integer.toHexString(System.identityHashCode(this));
  }

  @Override
  public int hashCode() {
    return data == null ? super.hashCode() : data.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof KlonObject && compareTo((KlonObject) obj) == 0;
  }

  @SuppressWarnings("unused")
  @Override
  public KlonObject clone() {
    KlonObject result = new KlonObject(state);
    result.bind(this);
    result.setData(data);
    return result;
  }

  // ================
  // java.lang.Exception
  // ================

  @Override
  public String getMessage() {
    StringBuilder result = new StringBuilder();
    try {
      KlonObject name = getSlot("name");
      if (name != null) {
        result.append(name.getData());
        KlonObject description = getSlot("description");
        if (description != null) {
          result.append(":")
            .append(description.getData());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result.toString();
  }

  // ================
  // Exposed Methods
  // ================

  @ExposedAs("type")
  public static KlonObject type(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, receiver.getType());
  }

  @ExposedAs("bind")
  public static KlonObject bind(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    receiver.bind(message.evalArgument(context, 0));
    return receiver;
  }

  @ExposedAs("unbind")
  public static KlonObject unbind(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    receiver.unbind(message.evalArgument(context, 0));
    return receiver;
  }

  @ExposedAs("isBound")
  public static KlonObject isBound(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver.isBound(message.evalArgument(context, 0))
        ? receiver
        : KlonNil.newNil(receiver);
  }

  @ExposedAs("clone")
  public static KlonObject clone(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result = receiver.clone();
    KlonObject slot = result.getSlot("init");
    if (slot != null) {
      slot.activate(result, context, receiver.getState()
        .getInit());
    }
    return result;
  }

  @ExposedAs("setIsActivatable")
  public static KlonObject setIsActivatable(KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    KlonObject value = message.evalArgument(context, 0);
    receiver.setActivatable(!KlonNil.newNil(receiver)
      .equals(value));
    return receiver;
  }

  @ExposedAs("send")
  public static KlonObject send(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonMessage target;
    KlonObject subject = message.evalArgument(context, 0);
    if ("Message".equals(subject.getType())) {
      if (message.getArgumentCount() > 1) {
        throw KlonException.newException(receiver, "Object.invalidArgument",
          "argument must evaluate to a Message", message);
      }
      target = (KlonMessage) subject;
    } else {
      if (!"String".equals(subject.getType())) {
        throw KlonException.newException(receiver, "Object.invalidArgument",
          "argument must evaluate to a String", message);
      }
      target = KlonMessage.newMessage(receiver);
      target.setSelector(KlonString.newString(receiver,
        (String) subject.getData()));
      for (int i = 1; i < message.getArgumentCount(); i++) {
        target.addArgument(message.getArgument(i));
      }
    }
    return target.eval(receiver, context);
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject result = receiver.getSlot(name);
    if (result == null) {
      throw KlonException.newException(receiver, "Object.doesNotExist", name
          + " does not exist", message);
    }
    return result;
  }

  @ExposedAs("hasSlot")
  public static KlonObject hasSlot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    return receiver.getSlot(name) == null ? KlonNil.newNil(receiver) : receiver;
  }

  @ExposedAs("setSlot")
  public static KlonObject setSlot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject value = message.evalArgument(context, 1);
    receiver.setSlot(name, value);
    return value;
  }

  @ExposedAs("updateSlot")
  public static KlonObject updateSlot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject value = message.evalArgument(context, 1);
    KlonObject result = receiver.updateSlot(name, value);
    if (result == null) {
      throw KlonException.newException(receiver, "Object.doesNotExist", name
          + " does not exist", null);
    }
    return value;
  }

  @ExposedAs("removeSlot")
  public static KlonObject removeSlot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    for (int i = 0; i < message.getArgumentCount(); i++) {
      receiver.removeSlot(KlonString.evalAsString(context, message, i));
    }
    return receiver;
  }

  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result = KlonNil.newNil(receiver);
    String name = (String) message.getArgument(0)
      .getSelector()
      .getData();
    String value = (String) message.getArgument(1)
      .getSelector()
      .getData();
    KlonMessage code = message.getArgument(2);
    // this is to protectect against concurrent modification exceptions
    Set<Entry<String, KlonObject>> entries = new HashSet<Entry<String, KlonObject>>(
      receiver.getSlots()
        .entrySet());
    for (Map.Entry<String, KlonObject> current : entries) {
      context.setSlot(name, KlonString.newString(receiver, current.getKey()));
      context.setSlot(value, current.getValue());
      result = code.eval(context, context);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("list")
  public static KlonObject list(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    List list = new ArrayList();
    for (int i = 0; i < message.getArgumentCount(); i++) {
      list.add(message.evalArgument(context, i));
    }
    return KlonList.newList(receiver, list);
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonString.newString(receiver, receiver.toString());
  }

  @ExposedAs("uniqueId")
  public static KlonObject uniqueId(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) System.identityHashCode(receiver));
  }

  @ExposedAs("print")
  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    inspect(receiver, context, message);
    return KlonNil.newNil(receiver);
  }

  @ExposedAs("write")
  public static KlonObject write(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonMessage printMessage = receiver.getState()
      .getAsString();
    for (int i = 0; i < message.getArgumentCount(); i++) {
      receiver.getState()
        .write((String) message.evalArgument(context, i)
          .perform(context, printMessage)
          .getData());
    }
    return receiver;
  }

  @ExposedAs("writeLine")
  public static KlonObject writeLine(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    write(receiver, context, message);
    receiver.getState()
      .write("\n");
    return receiver;
  }

  @ExposedAs("exit")
  public static KlonObject exit(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    int result = 0;
    if (message.getArgumentCount() == 1) {
      result = KlonNumber.evalAsNumber(context, message, 0)
        .intValue();
    }
    receiver.getState()
      .exit(result);
    return KlonNil.newNil(receiver);
  }

  @ExposedAs("function")
  public static KlonObject function(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result = method(receiver, context, message);
    ((Function) result.getData()).setScope(context);
    return result;
  }

  @ExposedAs("method")
  public static KlonObject method(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    int count = message.getArgumentCount() - 1;
    List<KlonObject> parameters = new ArrayList<KlonObject>(count);
    for (int i = 0; i < count; i++) {
      KlonObject current = message.getArgument(i)
        .getSelector();
      if (current == null) {
        throw KlonException.newException(receiver, "Object.invalidArgument",
          "argument must evaluate to a Symbol", message);
      }
      parameters.add(current);
    }
    return KlonFunction.newFunction(receiver, parameters,
      message.getArgument(count));
  }

  @ExposedAs("for")
  public static KlonObject forLoop(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result = KlonNil.newNil(receiver);
    String counter = (String) message.getArgument(0)
      .getSelector()
      .getData();
    double start = KlonNumber.evalAsNumber(context, message, 1)
      .intValue();
    double end = KlonNumber.evalAsNumber(context, message, 2)
      .intValue();
    double increment;
    KlonMessage code;
    if (message.getArgumentCount() == 5) {
      increment = KlonNumber.evalAsNumber(context, message, 3)
        .intValue();
      code = message.getArgument(4);
    } else {
      increment = end - start < 0 ? -1 : 1;
      code = message.getArgument(3);
    }
    double i = start;
    while (!(increment > 0 ? i > end : i < end)) {
      context.setSlot(counter, KlonNumber.newNumber(receiver, i));
      result = code.eval(receiver, context);
      i += increment;
    }
    return result;
  }

  @ExposedAs("while")
  public static KlonObject whileLoop(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject nil = KlonNil.newNil(receiver);
    KlonObject result = nil;
    KlonMessage condition = message.getArgument(0);
    KlonMessage code = message.getArgument(1);
    while (!nil.equals(condition.eval(context, context))) {
      result = code.eval(context, context);
    }
    return result;
  }

  @ExposedAs("if")
  public static KlonObject ifBranch(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result = message.evalArgument(context, 0);
    if (message.getArgumentCount() > 1) {
      if (!KlonNil.newNil(receiver)
        .equals(result)) {
        result = message.evalArgument(context, 1);
      } else if (message.getArgumentCount() == 3) {
        result = message.evalArgument(context, 2);
      }
    }
    return result;
  }

  @ExposedAs("then")
  public static KlonObject then(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonMirror.newMirror(receiver, message.evalArgument(context, 0));
  }

  @ExposedAs({"and", "&&"})
  public static KlonObject and(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject nil = KlonNil.newNil(receiver);
    return nil.equals(message.evalArgument(context, 0)) ? nil : receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs({"or", "||", "else", "elseIf"})
  public static KlonObject mirror(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNil.newNil(receiver);
  }

  @SuppressWarnings("unused")
  @ExposedAs("ifNil")
  public static KlonObject ifNil(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("isNil")
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return KlonNil.newNil(receiver);
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return receiver.equals(message.evalArgument(context, 0))
        ? receiver
        : KlonNil.newNil(receiver);
  }

  @ExposedAs("!=")
  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return !receiver.equals(message.evalArgument(context, 0))
        ? receiver
        : KlonNil.newNil(receiver);
  }

  @ExposedAs("<")
  public static KlonObject lessThan(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject argument = message.evalArgument(context, 0);
    return receiver.compareTo(argument) < 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  @ExposedAs(">")
  public static KlonObject greaterThan(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject argument = message.evalArgument(context, 0);
    return receiver.compareTo(argument) > 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  @ExposedAs("<=")
  public static KlonObject lessThanEquals(KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    KlonObject argument = message.evalArgument(context, 0);
    return receiver.compareTo(argument) <= 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  @ExposedAs(">=")
  public static KlonObject greaterThanEquals(KlonObject receiver,
      KlonObject context, KlonMessage message) throws KlonObject {
    KlonObject argument = message.evalArgument(context, 0);
    return receiver.compareTo(argument) >= 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  @ExposedAs("super")
  public static KlonObject superSlot(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonMessage target = message.getArgument(0);
    String name = (String) target.getSelector()
      .getData();
    KlonObject parent = null;
    Iterator<KlonObject> iterator = receiver.getBindings()
      .iterator();
    while (iterator.hasNext() && parent == null) {
      KlonObject current = iterator.next();
      if (current.getSlot(name) != null) {
        parent = current;
      }
    }
    if (parent == null) {
      throw KlonException.newException(receiver, "Object.doesNotExist", name
          + " does not exist", message);
    }
    return target.eval(receiver, context);
  }

  @ExposedAs("do")
  public static KlonObject doArgument(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    if (message.getArgumentCount() > 0) {
      message.evalArgument(receiver, 0);
    }
    return receiver;
  }

  @ExposedAs("doString")
  public static KlonObject doString(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    KlonMessage target = KlonMessage.newMessageFromString(receiver,
      KlonString.evalAsString(context, message, 0));
    target.eval(receiver, context);
    return receiver;
  }

  @ExposedAs("doFile")
  public static KlonObject doFile(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    message.assertArgumentCount(1);
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject string = KlonString.newString(receiver, new File(name));
    KlonMessage target = KlonMessage.newMessageFromString(receiver,
      (String) string.getData());
    target.eval(receiver, context);
    return receiver;
  }

  @ExposedAs({"ifTrue", "", "brace", "bracket"})
  public static KlonObject eval(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    return message.evalArgument(context, 0);
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("forward")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    String name = (String) message.getSelector()
      .getData();
    throw KlonException.newException(receiver, "Object.doesNotExist", name
        + " does not exist", message);
  }

  @SuppressWarnings("unused")
  @ExposedAs("try")
  public static KlonObject tryMessage(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result;
    try {
      result = KlonMirror.newMirror(receiver, message.evalArgument(context, 0));
    } catch (KlonObject e) {
      result = e;
    }
    return result;
  }

  @SuppressWarnings("unused")
  @ExposedAs("inspect")
  public static KlonObject inspect(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    receiver.getState()
      .write(receiver.toString());
    List<KlonObject> bindings = receiver.getBindings();
    if (!bindings.isEmpty()) {
      receiver.getState()
        .write(" (");
      Iterator<KlonObject> iterator = bindings.iterator();
      while (iterator.hasNext()) {
        receiver.getState()
          .write(iterator.next()
            .toString());
        if (iterator.hasNext()) {
          receiver.getState()
            .write(", ");
        }
      }
      receiver.getState()
        .write(")");
    }
    receiver.getState()
      .write("\n");
    TreeMap<String, KlonObject> sortedSlots = new TreeMap<String, KlonObject>(
      receiver.getSlots());
    for (Map.Entry<String, KlonObject> current : sortedSlots.entrySet()) {
      receiver.getState()
        .write(current.getKey() + " := " + current.getValue()
          .toString() + "\n");
    }
    return KlonNil.newNil(receiver);
  }

  @ExposedAs("?")
  public static KlonObject condition(KlonObject receiver, KlonObject context,
      KlonMessage message) throws KlonObject {
    KlonObject result = KlonNil.newNil(receiver);
    KlonMessage target = message.getArgument(0);
    KlonObject selector = target.getSelector();
    if (selector != null
        && receiver.getSlot((String) selector.getData()) != null) {
      result = receiver.perform(context, target);
    }
    return result;
  }

}
