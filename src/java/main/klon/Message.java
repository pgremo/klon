package klon;


public class Message {

  private String pattern;
  private KlonObject[] arguments;

  public Message(String pattern, KlonObject[] arguments) {
    this.pattern = pattern;
    this.arguments = arguments;
  }

  public KlonObject[] getArguments() {
    return arguments;
  }

  public Object[] getDownArguments() {
    Object[] result = new Object[arguments.length];
    for (int i = 0; i < arguments.length; i++) {
      result[i] = arguments[i].down();
    }
    return result;
  }

  public String getSlotName() {
    return pattern;
  }

}
