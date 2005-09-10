package klon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class Shell {

  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";
  private static final String[] PRINTABLES = new String[] { "Exception", "Nil",
      "Number", "String" };
  private Reader in;
  private MonitoredPrintStream out;

  public Shell(Reader in, MonitoredPrintStream out) {
    this.in = in;
    this.out = out;
  }

  public void process() throws KlonObject, IOException {
    KlonObject properties = KlonRoot.getROOT().getSlot("Properties");
    KlonObject version = properties.getSlot("klon.version");
    KlonObject build = properties.getSlot("klon.build");
    out.println("klon " + version.getData() + "." + build.getData());
    out.flush();
    while (true) {
      String prompt = "";
      KlonObject promptSlot = KlonRoot.getROOT().getSlot("Properties").getSlot(
          "klon.shell.prompt");
      if (promptSlot != null) {
        prompt = promptSlot.getData().toString();
      }
      out.print('\n');
      out.print(prompt);
      out.flush();
      String buffer = readMessage(in);
      Message message = new Compiler(KlonRoot.getROOT()).fromString(buffer);
      if (message != null) {
        evalMessage(message);
      }
    }
  }

  private void evalMessage(Message message) throws KlonObject {
    out.setHasOutput(false);
    KlonObject value = null;
    try {
      KlonObject root = KlonRoot.getROOT();
      value = message.eval(root, root);
    } catch (KlonObject e) {
      value = e;
      out.setHasOutput(false);
    }
    if (!out.hasOutput()) {
      Object type = value.getType();
      if (Arrays.binarySearch(PRINTABLES, type) > -1) {
        Message reportMessage = new Compiler(KlonRoot.getROOT())
            .fromString("writeLine");
        reportMessage.addArgument(value);
        reportMessage.eval(value, value);
      } else {
        Message reportMessage = new Compiler(KlonRoot.getROOT())
            .fromString("inspect");
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
      KlonRoot.setup(args);
      MonitoredPrintStream newOut = new MonitoredPrintStream(System.out);
      System.setOut(newOut);
      new Shell(new InputStreamReader(System.in), newOut).process();
    } catch (Exception e) {
      System.err.print(e.getMessage() + "\n");
      e.printStackTrace(System.err);
    }
  }
}
