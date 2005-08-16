package klon;

public class KlonString extends KlonObject {

  public KlonString(String value) {
    // TODO: Need Object form Lobby
    super(null, value);
  }

  @Override
  public String toString() {
    return "\"" + attached + "\"";
  }

}
