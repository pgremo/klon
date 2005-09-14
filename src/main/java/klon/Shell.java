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
  private ShellListener listener;

  public Shell(Reader in, State state, ShellListener listener) {
    this.in = in;
    this.state = state;
    this.listener = listener;
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
    listener.setHasPrint(false);
    KlonObject value = state.doString(message);
    if (!listener.getHasPrint()) {
      Message reportMessage;
      if (Arrays.binarySearch(PRINTABLES, value.getType()) > -1) {
        reportMessage = new Compiler(state.getRoot()).fromString("writeLine");
        reportMessage.addArgument(value);
      } else {
        reportMessage = new Compiler(state.getRoot()).fromString("inspect");
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
      Shell shell = new Shell(new InputStreamReader(System.in), state, listener);
      shell.process();
    } catch (Exception e) {
      System.err.print(e.getMessage() + "\n");
      e.printStackTrace(System.err);
    }
  }

}
