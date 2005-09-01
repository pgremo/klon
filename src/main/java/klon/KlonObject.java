package klon;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Prototype(name = "Object", parent = "Klon")
public class KlonObject extends Exception {

  private static final long serialVersionUID = 5234708348712278569L;

  private List<KlonObject> bindings = new LinkedList<KlonObject>();
  private Map<String, KlonObject> slots = new HashMap<String, KlonObject>();
  private Method activator;
  private Method duplicator;
  private Method formatter;
  private Object data;

  public void configure(KlonObject root, Class<? extends Object> type)
      throws KlonObject {
    Configurator.configure(root, this, type);
  }

  public KlonObject duplicate() throws KlonObject {
    try {
      try {
        return (KlonObject) duplicator.invoke(null, this);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    } catch (KlonObject e) {
      throw e;
    } catch (Throwable e) {
      throw KlonException.newException(this, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    }
  }

  @SuppressWarnings("unused")
  public KlonObject activate(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    try {
      try {
        return (KlonObject) activator.invoke(null, this, receiver, context,
          message);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    } catch (KlonObject e) {
      throw e;
    } catch (Throwable e) {
      throw KlonException.newException(this, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    }
  }

  public void setData(Object value) {
    this.data = value;
  }

  public Object getData() {
    return data;
  }

  public void setDuplicator(Method duplicator) {
    this.duplicator = duplicator;
  }

  public Method getDuplicator() {
    return duplicator;
  }

  public void setActivator(Method activator) {
    this.activator = activator;
  }

  public Method getActivator() {
    return activator;
  }

  public void setFormatter(Method formatter) {
    this.formatter = formatter;
  }

  public Method getFormatter() {
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
        if (!searchPath.contains(current)) {
          searchPath.add(current);
          result = current.getSlot(name, searchPath);
        }
      }
    }
    return result;
  }

  public KlonObject getSlot(String name) throws KlonObject {
    LinkedList<KlonObject> searchPath = new LinkedList<KlonObject>();
    KlonObject result = getSlot(name, searchPath);
    if (result == null) {
      KlonObject self = getSlot("self", searchPath);
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
    String name = (String) message.getSelector()
      .getData();
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
      ((List<KlonObject>) e.getSlot("stackTrace")
        .getData()).add(KlonString.newString(context, message.toString()));
      throw e;
    }
    return slot.activate(this, context, message);
  }

  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (this == obj) {
      result = true;
    } else {
      KlonObject klonObject = (KlonObject) obj;
      result = obj instanceof KlonObject
          && slots.equals(klonObject.slots)
          && bindings.equals(klonObject.bindings)
          && ((data == null && klonObject.data == null) || data != null
              && data.equals(klonObject.data));
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
      result = (String) formatter.invoke(null, this);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public String getMessage() {
    StringBuilder result = new StringBuilder();
    try {
      KlonObject name = getSlot("name");
      KlonObject description = getSlot("description");
      if (name != null) {
        result.append(name.getData());
        if (description != null) {
          result.append(":")
            .append(description.getData());
        }
      }
    } catch (KlonObject e) {
      e.printStackTrace();
    }
    return result.toString();
  }

  public static KlonObject protoType() {
    KlonObject result = new KlonObject();
    Configurator.setActivator(result, KlonObject.class);
    Configurator.setDuplicator(result, KlonObject.class);
    Configurator.setFormatter(result, KlonObject.class);
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
      return result;
    } catch (Exception e) {
      throw KlonException.newException(value, e.getClass()
        .getSimpleName(), e.getMessage(), null);
    }
  }

  public static String format(KlonObject object) throws KlonObject {
    return object.getSlot("type")
      .getData() + "_0x" + Integer.toHexString(object.hashCode());
  }

  @SuppressWarnings("unused")
  public static KlonObject activate(KlonObject slot, KlonObject receiver,
      KlonObject context, Message message) throws KlonObject {
    return slot;
  }

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
    return receiver.isBound(message.eval(context, 0))
        ? receiver
        : receiver.getSlot("Nil");
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
    KlonObject subject = message.eval(context, 0);
    if ("Message".equals(subject.getSlot("type"))) {
      return receiver.perform(context, (Message) subject.getData());
    }
    throw KlonException.newException(receiver, "Invalid Argument",
      "argument must evaluate to a Message", message);
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
    receiver.removeSlot(KlonString.evalAsString(context, message, 0));
    return receiver;
  }

  @ExposedAs("forEach")
  public static KlonObject forEach(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    KlonObject scope = KlonLocals.newLocals(receiver, context);
    String name = (String) message.getArgument(0)
      .getSelector()
      .getData();
    String value = (String) message.getArgument(1)
      .getSelector()
      .getData();
    Message code = message.getArgument(2);
    for (Map.Entry<String, KlonObject> current : receiver.slots.entrySet()) {
      scope.setSlot(name, KlonString.newString(receiver, current.getKey()));
      scope.setSlot(value, current.getValue());
      result = code.eval(scope, scope);
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

  @ExposedAs("write")
  public static KlonObject write(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message printMessage = new Compiler(receiver).fromString("asString");
    for (int i = 0; i < message.getArgumentCount(); i++) {
      System.out.print(message.eval(context, i)
        .perform(context, printMessage)
        .getData());
    }
    return receiver;
  }

  @ExposedAs("writeLine")
  public static KlonObject writeLine(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    write(receiver, context, message);
    System.out.println();
    return receiver;
  }

  @ExposedAs("exit")
  public static KlonObject exit(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    int result = 0;
    if (message.getArgumentCount() == 1) {
      result = KlonNumber.evalAsNumber(context, message, 0)
        .intValue();
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
      KlonObject current = message.getArgument(i)
        .getSelector();
      if (current == null) {
        throw KlonException.newException(receiver, "Invalid Argument",
          "argument must evaluate to a Symbol", message);
      }
      parameters[i] = (String) current.getData();
    }
    return KlonBlock.newBlock(receiver, new Block(parameters,
      message.getArgument(count)));
  }

  @ExposedAs("method")
  public static KlonObject method(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    int count = message.getArgumentCount() - 1;
    String[] parameters = new String[count];
    for (int i = 0; i < count; i++) {
      KlonObject current = message.getArgument(i)
        .getSelector();
      if (current == null) {
        throw KlonException.newException(receiver, "Invalid Argument",
          "argument must evaluate to a Symbol", message);
      }
      parameters[i] = (String) current.getData();
    }
    return KlonBlock.newBlock(receiver, new Block(parameters,
      message.getArgument(count)));
  }

  @ExposedAs("for")
  public static KlonObject forLoop(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    KlonObject scope = KlonLocals.newLocals(receiver, receiver);
    String counter = (String) message.getArgument(0)
      .getSelector()
      .getData();
    double start = KlonNumber.evalAsNumber(context, message, 1)
      .intValue();
    double end = KlonNumber.evalAsNumber(context, message, 2)
      .intValue();
    double increment;
    Message code;
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
      scope.setSlot(counter, KlonNumber.newNumber(receiver, i));
      result = code.eval(scope, scope);
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
      if (!receiver.getSlot("Nil")
        .equals(result)) {
        result = message.eval(context, 1);
      } else if (message.getArgumentCount() == 3) {
        result = message.eval(context, 2);
      }
    }
    return result;
  }

  @ExposedAs("and")
  public static KlonObject and(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject nil = receiver.getSlot("Nil");
    return nil.equals(message.eval(context, 0)) ? nil : receiver;
  }

  @SuppressWarnings("unused")
  @ExposedAs("or")
  public static KlonObject or(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver;
  }

  @ExposedAs("ifFalse")
  public static KlonObject ifFalse(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return receiver.getSlot("Nil");
  }

  @ExposedAs("ifTrue")
  public static KlonObject ifTrue(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return message.eval(context, 0);
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
    return receiver.equals(message.eval(context, 0))
        ? receiver
        : receiver.getSlot("Nil");
  }

  @ExposedAs("!=")
  public static KlonObject isNotEquals(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return !receiver.equals(message.eval(context, 0))
        ? receiver
        : receiver.getSlot("Nil");
  }

  @ExposedAs("super")
  public static KlonObject superSlot(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    Message target = message.getArgument(0);
    String name = (String) target.getSelector()
      .getData();
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
    File file = new File(name);
    KlonObject string = KlonString.newString(receiver, file);
    Message target = new Compiler(receiver).fromString((String) string.getData());
    target.eval(receiver, context);
    return receiver;
  }

  @ExposedAs("parenthesis")
  public static KlonObject parenthesis(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return message.eval(context, 0);
  }

  @ExposedAs("brace")
  public static KlonObject brace(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    return message.eval(context, 0);
  }

  @ExposedAs("bracket")
  public static KlonObject bracket(KlonObject receiver, KlonObject context,
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
    System.out.println();
    for (Map.Entry<String, KlonObject> current : receiver.slots.entrySet()) {
      System.out.println(current.getKey() + " := " + current.getValue()
        .toString());
    }
    return receiver.getSlot("Nil");
  }

  @ExposedAs("?")
  public static KlonObject condition(KlonObject receiver, KlonObject context,
      Message message) throws KlonObject {
    KlonObject result = receiver.getSlot("Nil");
    Message target = message.getArgument(0);
    if (receiver.getSlot((String) target.getSelector()
      .getData()) != null) {
      result = receiver.perform(context, target);
    }
    return result;
  }
}
