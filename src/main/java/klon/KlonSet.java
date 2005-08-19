package klon;

public class KlonSet extends KlonObject<KlonSet> {

  private java.util.Set storage;

  public KlonSet(java.util.Set storage) {
    this.storage = storage;
  }

  public KlonSet() {
    super();
  }

  @Override
  public String toString() {
    return storage.toString();
  }

}
