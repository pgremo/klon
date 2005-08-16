package klon;

public class Symbol extends KlonObject {

  private String selector;

  public Symbol(String selector) {
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
