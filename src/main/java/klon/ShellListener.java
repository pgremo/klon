package klon;

public class ShellListener
    implements
      ExceptionListener,
      ExitListener,
      WriteListener {

  private static final long serialVersionUID = -142383084375472828L;
  private boolean hasPrint;

  public void onException(State state, KlonObject exception) {
    try {
      KlonMessage reportMessage = new Compiler(state.getRoot()).fromString("writeLine");
      reportMessage.addArgument(KlonMessage.newMessage(exception, new Message(
        exception)));
      reportMessage.eval(exception, exception);
    } catch (KlonObject e) {
      e.printStackTrace();
    }
  }

  public void onExit(State state, int result) {
    System.exit(result);
  }

  public void onWrite(State state, String value) {
    System.out.print(value);
    hasPrint = true;
  }

  public boolean getHasPrint() {
    return hasPrint;
  }

  public void setHasPrint(boolean hasPrint) {
    this.hasPrint = hasPrint;
  }

}
