package klon;

public class Set extends KlonObject<Set> {

  private java.util.Set storage;

  public Set(java.util.Set storage) {
    this.storage = storage;
  }

  public Set() {
    super();
  }

  @Override
  public String toString() {
    return storage.toString();
  }

}
