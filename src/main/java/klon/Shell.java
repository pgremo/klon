package klon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class Shell implements ExceptionListener, ExitListener, WriteListener {

  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";
  private static final String[] PRINTABLES = new String[]{
      "Exception",
      "Nil",
      "Number",
      "String"};
  private Reader in;
  private KlonState state;
  private boolean hasPrint;

  public Shell(Reader in, KlonState state) {
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
    hasPrint = false;
    KlonObject value = state.doString(message);
    if (!hasPrint) {
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

  public void onException(KlonState state, KlonObject exception) {
    try {
      Message reportMessage = new Compiler(state.getRoot()).fromString("writeLine");
      reportMessage.addArgument(exception);
      reportMessage.eval(exception, exception);
    } catch (KlonObject e) {
      e.printStackTrace();
    }
  }

  public void onExit(KlonState state, int result) {
    System.exit(result);
  }

  public void onWrite(KlonState state, String value) {
    System.out.print(value);
    hasPrint = true;
  }

  public static void main(String[] args) {
    try {
      KlonState state = new KlonState(args);
      Shell shell = new Shell(new InputStreamReader(System.in), state);
      state.setExceptionListener(shell);
      state.setExitListener(shell);
      state.setWriteListener(shell);
      shell.process();
    } catch (Exception e) {
      System.err.print(e.getMessage() + "\n");
      e.printStackTrace(System.err);
    }
  }

}
