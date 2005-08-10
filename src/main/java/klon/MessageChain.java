package klon;

public class MessageChain extends Expression {

  private Message[] messages;

  public MessageChain(Message[] messages) {
    this.messages = messages;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Message current : messages) {
      if (result.length() > 0) {
        result.append(" ");
      }
      result.append(current);
    }
    return result.toString();
  }

}
