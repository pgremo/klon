package klon;

public class IdentifierLiteral extends KlonObject {

  private String selector;

  public IdentifierLiteral(String selector) {
    this.selector = selector;
  }

  @Override
  public String toString() {
    return selector;
  }
}
