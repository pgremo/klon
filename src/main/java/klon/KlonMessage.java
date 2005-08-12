package klon;

import java.util.Iterator;
import java.util.List;

public class KlonMessage extends KlonObject {

  private KlonObject selector;
  private List<KlonMessage> arguments;
  private KlonMessage attached;
  private KlonMessage next;

  public KlonMessage(KlonObject selector, KlonMessage attached, List<KlonMessage> arguments) {
    this.selector = selector;
    this.attached = attached;
    this.arguments = arguments;
  }

  public List<KlonMessage> getArguments() {
    return arguments;
  }

  public KlonObject getSelector() {
    return selector;
  }

  public KlonMessage getAttached() {
    return attached;
  }

  public void setAttached(KlonMessage value) {
    attached = value;
  }

  public KlonMessage getNext() {
    return next;
  }

  public void setNext(KlonMessage next) {
    this.next = next;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(selector.toString());
    if (arguments.size() > 0) {
      result.append("(");
      Iterator<KlonMessage> iterator = arguments.iterator();
      while (iterator.hasNext()) {
        result.append(iterator.next());
        if (iterator.hasNext()) {
          result.append(", ");
        }
      }
      result.append(")");
    }
    if (attached != null) {
      result.append(" ")
        .append(attached);
    }
    if (next != null) {
      result.append(";\n")
        .append(next);
    }
    return result.toString();
  }

}
