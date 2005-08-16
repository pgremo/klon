package klon;

public class KlonSymbol extends KlonObject {

  private String selector;

  public KlonSymbol(String selector) {
    this.selector = selector;
  }

  public String getSelector() {
    return selector;
  }

  @Override
  public String toString() {
    return selector;
  }

}
