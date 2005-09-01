package klon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

public class Shell {

  private static final String PROMPT = "\nklon> ";
  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";
  private static final String[] PRINTABLES = new String[]{
      "Exception",
      "Number",
      "String"};
  private Compiler compiler;
  private KlonObject root;
  private Reader in;
  private MonitoredPrintStream out;

  public Shell(KlonObject root, Reader in, MonitoredPrintStream out) {
    compiler = new Compiler(root);
    this.root = root;
    this.in = in;
    this.out = out;
  }

  public void process() throws KlonObject, IOException {
    while (true) {
      out.print(PROMPT);
      out.flush();
      String buffer = readMessage(in);
      Message message = compiler.fromString(buffer);
      if (message != null) {
        evalMessage(message);
      }
    }
  }

  private void evalMessage(Message message) throws KlonObject {
    out.setHasOutput(false);
    KlonObject value = null;
    try {
      value = message.eval(root, root);
    } catch (KlonObject e) {
      value = e;
      out.setHasOutput(false);
    }
    if (!out.hasOutput()) {
      Object type = value.getSlot("type")
        .getData();
      if (Arrays.binarySearch(PRINTABLES, type) > -1) {
        Message reportMessage = compiler.fromString("writeLine");
        reportMessage.addArgument(value);
        reportMessage.eval(value, value);
      } else {
        Message reportMessage = compiler.fromString("inspect");
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

  public static void main(String[] args) throws Exception {
    KlonObject root = KlonRoot.protoType(args);
    MonitoredPrintStream newOut = new MonitoredPrintStream(System.out);
    System.setOut(newOut);
    new Shell(root, new InputStreamReader(System.in), newOut).process();
  }
}
