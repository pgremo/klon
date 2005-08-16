package klon;

public class Set extends KlonObject {

  private java.util.Set storage;

  public Set(java.util.Set storage) {
    this.storage = storage;
  }

  public Set() {
    super();
  }

  public Set(KlonObject parent) {
    super(parent);
  }

  @Override
  public String toString() {
    return storage.toString();
  }

}
