package klon;

import java.text.NumberFormat;

public class NumberLiteral extends Literal {

  private static NumberFormat formatter = NumberFormat.getInstance();
  static {
    formatter.setGroupingUsed(false);
    formatter.setMinimumFractionDigits(0);
    formatter.setMaximumFractionDigits(Integer.MAX_VALUE);
  }

  private double value;

  public NumberLiteral(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return formatter.format(value);
  }

}
