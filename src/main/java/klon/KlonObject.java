package klon;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class KlonObject extends Exception
    implements
      Cloneable,
      Externalizable,
      Comparable<KlonObject> {

  private static final long serialVersionUID = 5234708348712278569L;

  private State state;
  private List<KlonObject> bindings;
  private Map<String, KlonObject> slots;
  private Object data;

  public KlonObject() {

  }

  public KlonObject(State state) {
    this.bindings = new ArrayList<KlonObject>();
    this.state = state;
  }

  public void prototype() throws Exception {
    KlonObject root = getState().getRoot();

    bind(root.getSlot("Object"));

    setSlot("and", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("and", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("&&", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("and", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("asString", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("asString", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("bind", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("bind", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("clone", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("clone", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("?",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "condition", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("do",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "doArgument", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("doFile", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("doFile", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("doString", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("doString", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifTrue", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("evaluate", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("evaluate", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("brace", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("evaluate", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("exit", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("exit", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("forEach", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("forEach", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("for", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("forLoop", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("forward", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("forward", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("function", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("function", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("getSlot", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("getSlot", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot(">", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("greaterThan",
        KlonNativeMethod.PARAMETER_TYPES)));
    setSlot(">=", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("greaterThanEquals",
        KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("hasSlot", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("hasSlot", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("if", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("ifBranch", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("inspect", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("inspect", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("isBound", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("isBound", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("==", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("isEquals", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("!=", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("isNotEquals",
        KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("<", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("lessThan", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("<=", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("lessThanEquals",
        KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("list", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("list", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("bracket", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("list", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("method", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("method", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("or", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("||", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("else", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("elseIf", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifNil", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("mirror", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("ifFalse", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("nil", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("isNil", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("nil", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("print", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("print", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("removeSlot",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "removeSlot", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("send", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("send", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("sendMessage", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("sendMessage",
        KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("setSlot", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("setSlot", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("super",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "superSlot", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("then", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("then", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("try",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "tryMessage", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("unbind", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("unbind", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("uniqueId", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("uniqueId", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("updateSlot",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "updateSlot", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("while",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "whileLoop", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("write", KlonNativeMethod.newNativeMethod(root,
      KlonObject.class.getMethod("write", KlonNativeMethod.PARAMETER_TYPES)));
    setSlot("writeLine",
      KlonNativeMethod.newNativeMethod(root, KlonObject.class.getMethod(
        "writeLine", KlonNativeMethod.PARAMETER_TYPES)));
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject activate = getSlot("activate");
    return activate == null
        ? mirror(this, context, message)
        : activate.activate(this, context, message);
  }

  public List<KlonObject> getBindings() {
    return bindings;
  }

  public Map<String, KlonObject> getSlots() {
    return slots;
  }

  public void setSlot(String name, KlonObject value) {
    if (slots == null) {
      slots = new HashMap<String, KlonObject>(50);
    }
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
    Iterator<KlonObject> iterator = collection.iterator();
    while (!found && iterator.hasNext()) {
      found = object == iterator.next();
    }
    return found;
  }

  private KlonObject getSlot(String name, Collection<KlonObject> searchPath)
      throws KlonObject {
    KlonObject result = null;
    if (slots != null) {
      result = slots.get(name);
    }
    if (result == null) {
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
    if (slots != null) {
      slots.remove(name);
    }
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
  public KlonObject perform(KlonObject context, KlonObject message)
      throws KlonObject {
    String name = (String) KlonMessage.getSelector(message)
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

  @SuppressWarnings("unchecked")
  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    state = (State) in.readObject();
    bindings = (List<KlonObject>) in.readObject();
    slots = (Map<String, KlonObject>) in.readObject();
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(state);
    out.writeObject(bindings);
    out.writeObject(slots);
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
    return "Object_0x" + Integer.toHexString(System.identityHashCode(this));
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

  public static KlonObject bind(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    receiver.bind(KlonMessage.evalArgument(message, context, 0));
    return receiver;
  }

  public static KlonObject unbind(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    receiver.unbind(KlonMessage.evalArgument(message, context, 0));
    return receiver;
  }

  public static KlonObject isBound(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return receiver.isBound(KlonMessage.evalArgument(message, context, 0))
        ? receiver
        : KlonNil.newNil(receiver);
  }

  public static KlonObject clone(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result = receiver.clone();
    KlonMessage.eval(result.getState()
      .getInit(), result, context);
    return result;
  }

  public static KlonObject send(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject subject = KlonMessage.evalArgument(message, context, 0);
    subject = KlonMessage.eval(subject.getState()
      .getAsString(), subject, context);
    KlonObject target = KlonMessage.newMessage(receiver);
    KlonMessage.setSelector(target, subject);
    for (int i = 1; i < KlonMessage.getArgumentCount(message); i++) {
      KlonMessage.addArgument(target, KlonMessage.getArgument(message, i));
    }
    return KlonMessage.eval(target, receiver, context);
  }

  public static KlonObject sendMessage(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject target = KlonMessage.evalArgument(message, context, 0);
    return KlonMessage.eval(target, receiver, context);
  }

  public static KlonObject getSlot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject result = receiver.getSlot(name);
    if (result == null) {
      throw KlonException.newException(receiver, "Object.doesNotExist", name
          + " does not exist", message);
    }
    return result;
  }

  public static KlonObject hasSlot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return receiver.getSlot(KlonString.evalAsString(context, message, 0)) == null
        ? KlonNil.newNil(receiver)
        : receiver;
  }

  public static KlonObject setSlot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    receiver.setSlot(KlonString.evalAsString(context, message, 0),
      KlonMessage.evalArgument(message, context, 1));
    return KlonMessage.evalArgument(message, context, 1);
  }

  public static KlonObject updateSlot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject value = KlonMessage.evalArgument(message, context, 1);
    KlonObject result = receiver.updateSlot(name, value);
    if (result == null) {
      throw KlonException.newException(receiver, "Object.doesNotExist", name
          + " does not exist", null);
    }
    return value;
  }

  public static KlonObject removeSlot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    for (int i = 0; i < KlonMessage.getArgumentCount(message); i++) {
      receiver.removeSlot(KlonString.evalAsString(context, message, i));
    }
    return receiver;
  }

  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 3);
    KlonObject result = KlonNil.newNil(receiver);
    String name = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, 0))
      .getData();
    String value = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, 1))
      .getData();
    KlonObject code = KlonMessage.getArgument(message, 2);
    Map<String, KlonObject> slots = receiver.getSlots();
    if (slots != null) {
      // this is to protect against concurrent modification exceptions
      List<Entry<String, KlonObject>> entries = new ArrayList<Entry<String, KlonObject>>(
        slots.entrySet());
      for (Map.Entry<String, KlonObject> current : entries) {
        context.setSlot(name, KlonString.newString(receiver, current.getKey()));
        context.setSlot(value, current.getValue());
        result = KlonMessage.eval(code, context, context);
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static KlonObject list(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    int count = KlonMessage.getArgumentCount(message);
    List list = new ArrayList(count);
    for (int i = 0; i < count; i++) {
      list.add(KlonMessage.evalArgument(message, context, i));
    }
    return KlonList.newList(receiver, list);
  }

  public static KlonObject asString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonString.newString(receiver, receiver.toString());
  }

  public static KlonObject uniqueId(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNumber.newNumber(receiver,
      (double) System.identityHashCode(receiver));
  }

  public static KlonObject print(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    inspect(receiver, context, message);
    return KlonNil.newNil(receiver);
  }

  public static KlonObject write(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject asString = receiver.getState()
      .getAsString();
    for (int i = 0; i < KlonMessage.getArgumentCount(message); i++) {
      receiver.getState()
        .write(
          KlonMessage.eval(asString,
            KlonMessage.evalArgument(message, context, i), context)
            .getData());
    }
    return receiver;
  }

  public static KlonObject writeLine(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    write(receiver, context, message);
    receiver.getState()
      .write("\n");
    return receiver;
  }

  public static KlonObject exit(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    int result = 0;
    if (KlonMessage.getArgumentCount(message) > 0) {
      result = KlonNumber.evalAsNumber(context, message, 0)
        .intValue();
    }
    receiver.getState()
      .exit(result);
    return KlonNil.newNil(receiver);
  }

  public static KlonObject function(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonObject result = method(receiver, context, message);
    ((Function) result.getData()).setScope(context);
    return result;
  }

  public static KlonObject method(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    int count = KlonMessage.getArgumentCount(message) - 1;
    List<KlonObject> parameters = new ArrayList<KlonObject>(count);
    for (int i = 0; i < count; i++) {
      KlonObject current = KlonMessage.getSelector(KlonMessage.getArgument(
        message, i));
      if (current == null) {
        throw KlonException.newException(receiver, "Object.invalidArgument",
          "argument must evaluate to a Symbol", message);
      }
      parameters.add(current);
    }
    return KlonFunction.newFunction(receiver, parameters,
      KlonMessage.getArgument(message, count));
  }

  public static KlonObject forLoop(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 3);
    KlonObject result = KlonNil.newNil(receiver);
    String counter = (String) KlonMessage.getSelector(
      KlonMessage.getArgument(message, 0))
      .getData();
    double start = KlonNumber.evalAsNumber(context, message, 1)
      .intValue();
    double end = KlonNumber.evalAsNumber(context, message, 2)
      .intValue();
    double increment;
    KlonObject code;
    if (KlonMessage.getArgumentCount(message) == 5) {
      increment = KlonNumber.evalAsNumber(context, message, 3)
        .intValue();
      code = KlonMessage.getArgument(message, 4);
    } else {
      increment = end - start < 0 ? -1 : 1;
      code = KlonMessage.getArgument(message, 3);
    }
    double i = start;
    while (!(increment > 0 ? i > end : i < end)) {
      context.setSlot(counter, KlonNumber.newNumber(receiver, i));
      result = KlonMessage.eval(code, receiver, context);
      i += increment;
    }
    return result;
  }

  public static KlonObject whileLoop(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 2);
    KlonObject nil = KlonNil.newNil(receiver);
    KlonObject result = nil;
    KlonObject condition = KlonMessage.getArgument(message, 0);
    KlonObject code = KlonMessage.getArgument(message, 1);
    while (!nil.equals(KlonMessage.eval(condition, context, context))) {
      result = KlonMessage.eval(code, context, context);
    }
    return result;
  }

  public static KlonObject ifBranch(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject result = KlonMessage.evalArgument(message, context, 0);
    int count = KlonMessage.getArgumentCount(message);
    if (count > 1) {
      if (!KlonNil.newNil(receiver)
        .equals(result)) {
        result = KlonMessage.evalArgument(message, context, 1);
      } else if (count == 3) {
        result = KlonMessage.evalArgument(message, context, 2);
      }
    }
    return result;
  }

  public static KlonObject then(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return KlonVoid.newVoid(receiver, KlonMessage.evalArgument(message,
      context, 0));
  }

  public static KlonObject and(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject nil = KlonNil.newNil(receiver);
    return nil.equals(KlonMessage.evalArgument(message, context, 0))
        ? nil
        : receiver;
  }

  @SuppressWarnings("unused")
  public static KlonObject mirror(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return receiver;
  }

  @SuppressWarnings("unused")
  public static KlonObject nil(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    return KlonNil.newNil(receiver);
  }

  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return receiver.equals(KlonMessage.evalArgument(message, context, 0))
        ? receiver
        : KlonNil.newNil(receiver);
  }

  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return !receiver.equals(KlonMessage.evalArgument(message, context, 0))
        ? receiver
        : KlonNil.newNil(receiver);
  }

  public static KlonObject lessThan(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject argument = KlonMessage.evalArgument(message, context, 0);
    return receiver.compareTo(argument) < 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  public static KlonObject greaterThan(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject argument = KlonMessage.evalArgument(message, context, 0);
    return receiver.compareTo(argument) > 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  public static KlonObject lessThanEquals(KlonObject receiver,
      KlonObject context, KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject argument = KlonMessage.evalArgument(message, context, 0);
    return receiver.compareTo(argument) <= 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  public static KlonObject greaterThanEquals(KlonObject receiver,
      KlonObject context, KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject argument = KlonMessage.evalArgument(message, context, 0);
    return receiver.compareTo(argument) >= 0
        ? argument
        : KlonNil.newNil(receiver);
  }

  public static KlonObject superSlot(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject target = KlonMessage.getArgument(message, 0);
    String name = (String) KlonMessage.getSelector(target)
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
    return KlonMessage.eval(target, receiver, context);
  }

  public static KlonObject doArgument(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    if (KlonMessage.getArgumentCount(message) > 0) {
      KlonMessage.evalArgument(message, receiver, 0);
    }
    return receiver;
  }

  public static KlonObject doString(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject target = KlonMessage.newMessageFromString(receiver,
      KlonString.evalAsString(context, message, 0));
    KlonMessage.eval(target, receiver, context);
    return receiver;
  }

  public static KlonObject doFile(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject file = KlonFile.newFile(receiver, new File(name));
    KlonObject string = KlonFile.asString(file, context, message);
    KlonObject target = KlonMessage.newMessageFromString(receiver,
      (String) string.getData());
    KlonMessage.eval(target, receiver, context);
    return receiver;
  }

  public static KlonObject evaluate(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    return KlonMessage.evalArgument(message, context, 0);
  }

  @SuppressWarnings("unchecked")
  public static KlonObject forward(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    throw KlonException.newException(receiver, "Object.doesNotRespond",
      "Receiver does not respond to '" + KlonMessage.getSelector(message)
        .getData() + "'", message);
  }

  @SuppressWarnings("unused")
  public static KlonObject tryMessage(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject result;
    try {
      result = KlonVoid.newVoid(receiver, KlonMessage.evalArgument(message,
        context, 0));
    } catch (KlonObject e) {
      result = e;
    }
    return result;
  }

  @SuppressWarnings("unused")
  public static KlonObject inspect(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    State state = receiver.getState();
    state.write(receiver.toString());
    List<KlonObject> bindings = receiver.getBindings();
    if (!bindings.isEmpty()) {
      state.write(" (");
      Iterator<KlonObject> iterator = bindings.iterator();
      while (iterator.hasNext()) {
        state.write(iterator.next()
          .toString());
        if (iterator.hasNext()) {
          state.write(", ");
        }
      }
      state.write(")");
    }
    state.write("\n");
    Map<String, KlonObject> slots = receiver.getSlots();
    if (slots != null) {
      TreeMap<String, KlonObject> sortedSlots = new TreeMap<String, KlonObject>(
        slots);
      for (Map.Entry<String, KlonObject> current : sortedSlots.entrySet()) {
        state.write(current.getKey());
        state.write(" := ");
        state.write(current.getValue());
        state.write("\n");
      }
    }
    return KlonNil.newNil(receiver);
  }

  public static KlonObject condition(KlonObject receiver, KlonObject context,
      KlonObject message) throws KlonObject {
    KlonMessage.assertArgumentCount(message, 1);
    KlonObject result = KlonNil.newNil(receiver);
    KlonObject target = KlonMessage.getArgument(message, 0);
    KlonObject selector = KlonMessage.getSelector(target);
    if (selector != null
        && receiver.getSlot((String) selector.getData()) != null) {
      result = KlonMessage.eval(target, receiver, context);
    }
    return result;
  }
}
