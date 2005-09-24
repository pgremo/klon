package klon;

public class ShellListener implements ExitListener, WriteListener {

  private static final long serialVersionUID = -142383084375472828L;
  private boolean hasPrint;
  private boolean exit;

  public void onExit(State state, int result) {
    exit = true;
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

  public boolean isExit() {
    return exit;
  }

}
