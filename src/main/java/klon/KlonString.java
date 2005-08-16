package klon;

public class KlonString extends KlonObject {

  private String value;

  public KlonString(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "\"" + value + "\"";
  }

}
