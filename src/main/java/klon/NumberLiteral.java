package klon;

public class NumberLiteral extends Expression {

  private double value;

  public NumberLiteral(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return Double.toString(value);
  }

}
