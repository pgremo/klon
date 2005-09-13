package klon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class Shell {

  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";
  private static final String[] PRINTABLES = new String[]{
      "Exception",
      "Nil",
      "Number",
      "String"};
  private Reader in;
  private MonitoredPrintStream out;
  private KlonState state;

  public Shell(Reader in, MonitoredPrintStream out, KlonState state) {
    this.in = in;
    this.out = out;
    this.state = state;
  }

  public void process() throws KlonObject, IOException {
    KlonObject properties = state.getRoot()
      .getSlot("Properties");
    KlonObject version = properties.getSlot("klon.version");
    KlonObject build = properties.getSlot("klon.build");
    out.println("klon " + version.getData() + "." + build.getData());
    out.flush();
    while (true) {
      String prompt = "";
      KlonObject promptSlot = state.getRoot()
        .getSlot("Properties")
        .getSlot("klon.shell.prompt");
      if (promptSlot != null) {
        prompt = promptSlot.getData()
          .toString();
      }
      out.print('\n');
      out.print(prompt);
      out.flush();
      String message = readMessage(in);
      evalMessage(message);
    }
  }

  private void evalMessage(String message) throws KlonObject {
    out.setHasOutput(false);
    KlonObject value;
    try {
      value = state.doString(message);
    } catch (KlonObject e) {
      value = e;
      out.setHasOutput(false);
    }
    if (!out.hasOutput()) {
      if (Arrays.binarySearch(PRINTABLES, value.getType()) > -1) {
        Message reportMessage = new Compiler(state.getRoot()).fromString("writeLine");
        reportMessage.addArgument(value);
        reportMessage.eval(value, value);
      } else {
        Message reportMessage = new Compiler(state.getRoot()).fromString("inspect");
        reportMessage.eval(value, value);
      }
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
      MonitoredPrintStream newOut = new MonitoredPrintStream(System.out);
      System.setOut(newOut);
      new Shell(new InputStreamReader(System.in), newOut, new KlonState(args)).process();
    } catch (Exception e) {
      System.err.print(e.getMessage() + "\n");
      e.printStackTrace(System.err);
    }
  }
}
