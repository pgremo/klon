package klon;

public class IdentifierLiteral extends Literal {

  private String selector;

  public IdentifierLiteral(String selector) {
    this.selector = selector;
  }

  @Override
  public String toString() {
    return selector;
  }
}
