package klon;

public class KlonString extends KlonObject {

  public KlonString(String value) {
    super(Lobby.Object, value);
  }

  @Override
  public String toString() {
    return "\"" + attached + "\"";
  }

}
