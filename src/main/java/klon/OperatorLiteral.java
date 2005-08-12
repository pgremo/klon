package klon;

public class OperatorLiteral extends KlonObject {

  private String selector;

  public OperatorLiteral(String selector) {
    this.selector = selector;
  }

  @Override
  public String toString() {
    return selector;
  }
}
