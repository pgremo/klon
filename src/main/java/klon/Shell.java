package klon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class Shell {

  private static final long serialVersionUID = -7997503355735054042L;
  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";
  private static final String[] PRINTABLES = new String[]{
      "Exception",
      "Nil",
      "Number",
      "String"};
  private Reader in;
  private State state;

  public Shell(Reader in, State state) {
    this.in = in;
    this.state = state;
  }

  public void process() throws KlonObject, IOException {
    KlonObject properties = state.getRoot()
      .getSlot("Properties");
    KlonObject version = properties.getSlot("klon.version");
    KlonObject build = properties.getSlot("klon.build");
    System.out.print("klon " + version.getData() + "." + build.getData() + "\n");
    System.out.flush();
    while (true) {
      String prompt = "";
      KlonObject promptSlot = state.getRoot()
        .getSlot("Properties")
        .getSlot("klon.shell.prompt");
      if (promptSlot != null) {
        prompt = promptSlot.getData()
          .toString();
      }
      System.out.print('\n');
      System.out.print(prompt);
      System.out.flush();
      evalMessage(readMessage(in));
    }
  }

  private void evalMessage(String message) throws KlonObject {
    ((ShellListener) state.getRoot()
      .getState()
      .getWriteListener()).setHasPrint(false);
    KlonObject value = state.doString(message);
    if (!((ShellListener) state.getRoot()
      .getState()
      .getWriteListener()).getHasPrint()) {
      KlonMessage reportMessage;
      if (Arrays.binarySearch(PRINTABLES, value.getType()) > -1) {
        reportMessage = KlonMessage.newMessageFromString(state.getRoot(),
          "writeLine");
        reportMessage.addArgument(KlonMessage.newMessageWithLiteral(value,
          value));
      } else {
        reportMessage = KlonMessage.newMessageFromString(state.getRoot(),
          "inspect");
      }
      reportMessage.eval(value, value);
    }
  }

  private String readMessage(Reader in) throws IOException {
    StringBuilder buffer = new StringBuilder();
    int depth = 0;
    boolean quotting = false;
    char previous = 0;
    char current = 0;
    while ("\n".indexOf(current) == -1 || depth > 0) {
      current = (char) in.read();
      buffer.append(current);
      if (OPEN_GROUP.indexOf(current) > -1 && !quotting) {
        depth++;
      }
      if (CLOSE_GROUP.indexOf(current) > -1 && !quotting) {
        depth--;
      }
      if ('\\' != previous && '"' == current) {
        quotting = !quotting;
        if (quotting) {
          depth++;
        } else {
          depth--;
        }
      }
      previous = current;
    }
    return buffer.toString();
  }

  public static void main(String[] args) {
    try {
      State state = new State(args);
      ShellListener listener = new ShellListener();
      state.setExceptionListener(listener);
      state.setExitListener(listener);
      state.setWriteListener(listener);
      Shell shell = new Shell(new InputStreamReader(System.in), state);
      shell.process();
    } catch (Exception e) {
      System.err.print(e.getMessage() + "\n");
      e.printStackTrace(System.err);
    }
  }

}
