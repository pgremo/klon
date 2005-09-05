package klon;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Prototype(name = "Object", parent = "Klon")
public class KlonObject extends Exception implements Cloneable {

  private static final long serialVersionUID = 5234708348712278569L;

  private List<KlonObject> bindings = new LinkedList<KlonObject>();
  private Map<String, KlonObject> slots = new HashMap<String, KlonObject>();
  private NativeMethod comparator;
  private NativeMethod activator;
  private NativeMethod duplicator;
  private NativeMethod formatter;
  private Object data;

  public void configure(KlonObject root, Class<? extends Object> type)
      throws Exception {
    Configurator.setSlots(root, this, type);
  }

  public int compare(KlonObject other) throws KlonObject {
    try {
      try {
        return (Integer) comparator.invoke(this, other);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    } catch (KlonObject e) {
      throw e;
    } catch (Throwable e) {
      throw KlonException.newException(this, e.getClass().getSimpleName(), e
          .getMessage(), null);
    }
  }

  public KlonObject duplicate() throws KlonObject {
    try {
      try {
        return (KlonObject) duplicator.invoke(this);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    } catch (KlonObject e) {
      throw e;
    } catch (Throwable e) {
      throw KlonException.newException(this, e.getClass().getSimpleName(), e
          .getMessage(), null);
    }
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    try {
      try {
        return (KlonObject) activator.invoke(this, receiver, context, message);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    } catch (KlonObject e) {
      throw e;
    } catch (Throwable e) {
      throw KlonException.newException(this, e.getClass().getSimpleName(), e
          .getMessage(), null);
    }
  }

  public void setData(Object value) {
    this.data = value;
  }

  public Object getData() {
    return data;
  }

  public NativeMethod getComparator() {
    return comparator;
  }

  public void setComparator(NativeMethod comparator) {
    this.comparator = comparator;
  }

  public void setDuplicator(NativeMethod duplicator) {
    this.duplicator = duplicator;
  }

  public NativeMethod getDuplicator() {
    return duplicator;
  }

  public void setActivator(NativeMethod activator) {
    this.activator = activator;
  }

  public NativeMethod getActivator() {
    return activator;
  }

  public void setFormatter(NativeMethod formatter) {
    this.formatter = formatter;
  }

  public NativeMethod getFormatter() {
    return formatter;
  }

  public void setSlot(String name, KlonObject value) {
    slots.put(name, value);
  }

  private KlonObject updateSlot(String name, KlonObject value)
      throws KlonObject {
    KlonObject result = getSlot(name);
    if (result != null) {
      setSlot(name, value);
    }
    return result;
  }

  private KlonObject getSlot(String name, Collection<KlonObject> searchPath)
      throws KlonObject {
    KlonObject result = null;
    if (slots.containsKey(name)) {
      result = slots.get(name);
    } else {
      Iterator<KlonObject> iterator = bindings.iterator();
      while (iterator.hasNext() && result == null) {
        KlonObject current = iterator.next();
        if (!contains(searchPath, current)) {
          searchPath.add(current);
          result = current.getSlot(name, searchPath);
        }
      }
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

  private void removeSlot(String name) {
    slots.remove(name);
  }

  public void bind(KlonObject object) {
    boolean found = false;
    Iterator<KlonObject> iterator = bindings.iterator();
    while (iterator.hasNext() && !found) {
      found = iterator.next() == object;
    }
    if (!found) {
      bindings.add(object);
    }
  }

  public boolean isBound(KlonObject object) {
    boolean found = false;
    Iterator<KlonObject> iterator = bindings.iterator();
    while (iterator.hasNext() && !found) {
      found = iterator.next() == object;
    }
    return found;
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
  // java.lang.Object
  // ================

  @Override
  public Object clone() throws CloneNotSupportedException {
    try {
      return duplicate();
    } catch (KlonObject e) {
      throw new CloneNotSupportedException(e.getMessage());
    }
  }

  @Override
  public boolean equals(Object obj) {
    boolean result;
    try {
      result = obj instanceof KlonObject && compare((KlonObject) obj) == 0;
    } catch (KlonObject e) {
      result = false;
    }
    return result;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    String result = null;
    try {
      result = (String) formatter.invoke(this);
    } catch (Exception e) {
      e.printStackTrace();
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
      KlonObject description = getSlot("description");
      if (name != null) {
        result.append(name.getData());
        if (description != null) {
          result.append(":").append(description.getData());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result.toString();
  }

  // ================
  // Klon prototype methods
  // ================

  public static KlonObject prototype() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonObject.class);
    Configurator.setDuplicator(result, KlonObject.class);
    Configurator.setFormatter(result, KlonObject.class);
    Configurator.setComparator(result, KlonObject.class);
    return result;
  }

  public static KlonObject duplicate(KlonObject value) throws KlonObject {
    try {
      KlonObject result = new KlonObject();
      result.bind(value);
      result.setData(value.getData());
      result.setActivator(value.getActivator());
      result.setFormatter(value.getFormatter());
      result.setDuplicator(value.getDuplicator());
      result.setComparator(value.getComparator());
      return result;
    } catch (Exception e) {
      throw KlonException.newException(value, e.getClass().getSimpleName(), e
          .getMessage(), null);
    }
  }

  @SuppressWarnings("unused")
  public static int compare(KlonObject o1, KlonObject o2) throws KlonObject {
    return o1.hashCode() - o2.hashCode();
  }

  public static String format(KlonObject object) throws KlonObject {
    return object.getSlot("type").getData() + "_0x"
        + Integer.toHexString(object.hashCode());
  }

  @SuppressWarnings("unused")
  public static KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    return slot;
  }

  // ================
  // Klon Exposed Methods
  // ================

  @ExposedAs("bind")
  public static KlonObject bind(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    receiver.bind(message.eval(context, 0));
    return receiver;
  }

  @ExposedAs("unbind")
  public static KlonObject unbind(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    receiver.unbind(message.eval(context, 0));
    return receiver;
  }

  @ExposedAs("isBound")
  public static KlonObject isBound(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.isBound(message.eval(context, 0)) ? receiver : receiver
        .getSlot("Nil");
  }

  @ExposedAs("clone")
  public static KlonObject clone(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.duplicate();
    KlonObject slot = result.getSlot("init");
    if (slot != null) {
      slot.activate(result, context, message);
    }
    return result;
  }

  @ExposedAs("send")
  public static KlonObject send(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message target;
    KlonObject subject = message.eval(context, 0);
    KlonObject type = subject.getSlot("type");
    if ("Message".equals(type.getData())) {
      if (message.getArgumentCount() > 1) {
        throw KlonException.newException(receiver, "Invalid Argument",
            "argument must evaluate to a Message", message);
      }
      target = (Message) subject.getData();
    } else {
      if (!"String".equals(type.getData())) {
        throw KlonException.newException(receiver, "Invalid Argument",
            "argument must evaluate to a String", message);
      }
      target = new Message();
      target.setSelector(KlonSymbol.newSymbol(receiver, (String) subject
          .getData()));
      for (int i = 1; i < message.getArgumentCount(); i++) {
        target.addArgument(message.getArgument(i));
      }
    }
    return target.eval(receiver, context);
  }

  @ExposedAs("getSlot")
  public static KlonObject getSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject result = receiver.getSlot(name);
    if (result == null) {
      throw KlonException.newException(receiver, "Invalid Slot", name
          + " does not exist", message);
    }
    return result;
  }

  @ExposedAs("hasSlot")
  public static KlonObject hasSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    return receiver.getSlot(name) == null ? receiver.getSlot("Nil") : receiver;
  }

  @ExposedAs("setSlot")
  public static KlonObject setSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject value = message.eval(context, 1);
    receiver.setSlot(name, value);
    return value;
  }

  @ExposedAs("updateSlot")
  public static KlonObject updateSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject value = message.eval(context, 1);
    KlonObject result = receiver.updateSlot(name, value);
    if (result == null) {
      throw KlonException.newException(receiver, "Invalid Slot", name
          + " does not exist", null);
    }
    return value;
  }

  @ExposedAs("removeSlot")
  public static KlonObject removeSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    for (int i = 0; i < message.getArgumentCount(); i++) {
      receiver.removeSlot(KlonString.evalAsString(context, message, i));
    }
    return receiver;
  }

  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    String name = (String) message.getArgument(0).getSelector().getData();
    String value = (String) message.getArgument(1).getSelector().getData();
    Message code = message.getArgument(2);
    for (Map.Entry<String, KlonObject> current : receiver.slots.entrySet()) {
      context.setSlot(name, KlonString.newString(receiver, current.getKey()));
      context.setSlot(value, current.getValue());
      result = code.eval(context, context);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @ExposedAs("list")
  public static KlonObject list(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    List list = new ArrayList();
    for (int i = 0; i < message.getArgumentCount(); i++) {
      list.add(message.eval(context, i));
    }
    return KlonList.newList(receiver, list);
  }

  @ExposedAs("asString")
  public static KlonObject asString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonString.newString(receiver, receiver.toString());
  }

  @ExposedAs("uniqueId")
  public static KlonObject uniqueId(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNumber.newNumber(receiver, (double) System
        .identityHashCode(receiver));
  }

  @ExposedAs("write")
  public static KlonObject write(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message printMessage = new Compiler(receiver).fromString("asString");
    for (int i = 0; i < message.getArgumentCount(); i++) {
      System.out.print(message
          .eval(context, i)
            .perform(context, printMessage)
            .getData());
    }
    return receiver;
  }

  @ExposedAs("writeLine")
  public static KlonObject writeLine(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    write(receiver, context, message);
    System.out.print("\n");
    return receiver;
  }

  @ExposedAs("exit")
  public static KlonObject exit(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    int result = 0;
    if (message.getArgumentCount() == 1) {
      result = KlonNumber.evalAsNumber(context, message, 0).intValue();
    }
    System.exit(result);
    return receiver.getSlot("Nil");
  }

  @ExposedAs("block")
  public static KlonObject block(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    int count = message.getArgumentCount() - 1;
    String[] parameters = new String[count];
    for (int i = 0; i < count; i++) {
      KlonObject current = message.getArgument(i).getSelector();
      if (current == null) {
        throw KlonException.newException(receiver, "Invalid Argument",
            "argument must evaluate to a Symbol", message);
      }
      parameters[i] = (String) current.getData();
    }
    return KlonBlock.newBlock(receiver, new Block(parameters, message
        .getArgument(count)));
  }

  @ExposedAs("method")
  public static KlonObject method(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    int count = message.getArgumentCount() - 1;
    String[] parameters = new String[count];
    for (int i = 0; i < count; i++) {
      KlonObject current = message.getArgument(i).getSelector();
      if (current == null) {
        throw KlonException.newException(receiver, "Invalid Argument",
            "argument must evaluate to a Symbol", message);
      }
      parameters[i] = (String) current.getData();
    }
    return KlonBlock.newBlock(receiver, new Block(parameters, message
        .getArgument(count)));
  }

  @ExposedAs("for")
  public static KlonObject forLoop(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    String counter = (String) message.getArgument(0).getSelector().getData();
    double start = KlonNumber.evalAsNumber(context, message, 1).intValue();
    double end = KlonNumber.evalAsNumber(context, message, 2).intValue();
    double increment;
    Message code;
    if (message.getArgumentCount() == 5) {
      increment = KlonNumber.evalAsNumber(context, message, 3).intValue();
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
      Message message) throws KlonObject {
    KlonObject nil = receiver.getSlot("Nil");
    KlonObject result = nil;
    Message condition = message.getArgument(0);
    Message code = message.getArgument(1);
    while (!nil.equals(condition.eval(context, context))) {
      result = code.eval(context, context);
    }
    return result;
  }

  @ExposedAs("if")
  public static KlonObject ifBranch(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = message.eval(context, 0);
    if (message.getArgumentCount() > 1) {
      if (!receiver.getSlot("Nil").equals(result)) {
        result = message.eval(context, 1);
      } else if (message.getArgumentCount() == 3) {
        result = message.eval(context, 2);
      }
    }
    return result;
  }

  @ExposedAs("then")
  public static KlonObject then(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return KlonNoOp.newNoOp(receiver, message.eval(context, 0));
  }

  @ExposedAs( { "and", "&&" })
  public static KlonObject and(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject nil = receiver.getSlot("Nil");
    return nil.equals(message.eval(context, 0)) ? nil : receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs( { "or", "||", "else", "elseIf" })
  public static KlonObject noop(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.getSlot("Nil");
  }

  @SuppressWarnings("unused")
  @ExposedAs("ifNil")
  public static KlonObject ifNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }

  @ExposedAs("isNil")
  public static KlonObject isNil(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.getSlot("Nil");
  }

  @ExposedAs("==")
  public static KlonObject isEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.equals(message.eval(context, 0)) ? receiver : receiver
        .getSlot("Nil");
  }

  @ExposedAs("!=")
  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return !receiver.equals(message.eval(context, 0)) ? receiver : receiver
        .getSlot("Nil");
  }

  @ExposedAs("super")
  public static KlonObject superSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message target = message.getArgument(0);
    String name = (String) target.getSelector().getData();
    KlonObject parent = null;
    Iterator<KlonObject> iterator = receiver.bindings.iterator();
    while (iterator.hasNext() && parent == null) {
      KlonObject current = iterator.next();
      if (current.getSlot(name) != null) {
        parent = current;
      }
    }
    if (parent == null) {
      throw KlonException.newException(receiver, "Invalid Slot", name
          + " does not exist", message);
    }
    return target.eval(receiver, context);
  }

  @ExposedAs("do")
  public static KlonObject doArgument(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    message.eval(receiver, 0);
    return receiver;
  }

  @ExposedAs("doString")
  public static KlonObject doString(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message target = new Compiler(receiver).fromString(KlonString.evalAsString(
        context, message, 0));
    target.eval(receiver, context);
    return receiver;
  }

  @ExposedAs("doFile")
  public static KlonObject doFile(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    String name = KlonString.evalAsString(context, message, 0);
    KlonObject string = KlonString.newString(receiver, new File(name));
    Message target = new Compiler(receiver).fromString((String) string
        .getData());
    target.eval(receiver, context);
    return receiver;
  }

  @ExposedAs( { "ifTrue", "", "brace", "bracket" })
  public static KlonObject eval(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return message.eval(context, 0);
  }

  @SuppressWarnings("unused")
  @ExposedAs("try")
  public static KlonObject tryMessage(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result;
    try {
      result = KlonNoOp.newNoOp(receiver, message.eval(context, 0));
    } catch (KlonObject e) {
      result = e;
    }
    return result;
  }

  @ExposedAs("inspect")
  public static KlonObject inspect(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    System.out.print(receiver.toString());
    if (!receiver.bindings.isEmpty()) {
      System.out.print(" (");
      Iterator<KlonObject> iterator = receiver.bindings.iterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
        if (iterator.hasNext()) {
          System.out.print(", ");
        }
      }
      System.out.print(")");
    }
    System.out.print("\n");
    TreeMap<String, KlonObject> sortedSlots = new TreeMap<String, KlonObject>(
        receiver.slots);
    for (Map.Entry<String, KlonObject> current : sortedSlots.entrySet()) {
      System.out.print(current.getKey() + " := "
          + current.getValue().toString() + "\n");
    }
    return receiver.getSlot("Nil");
  }

  @ExposedAs("?")
  public static KlonObject condition(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    Message target = message.getArgument(0);
    KlonObject selector = target.getSelector();
    if (selector != null
        && receiver.getSlot((String) selector.getData()) != null) {
      result = receiver.perform(context, target);
    }
    return result;
  }
}
