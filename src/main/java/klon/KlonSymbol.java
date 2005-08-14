package klon;

public class KlonSymbol extends KlonObject {

  private String selector;

  public KlonSymbol(String selector) {
    this.selector = selector;
  }

  @Override
  public String toString() {
    return selector;
  }
}
