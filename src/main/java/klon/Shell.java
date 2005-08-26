package klon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

public class Shell {

  private static final String PROMPT = "\nklon> ";
  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";

  public void process(KlonObject root, Reader in, MonitoredPrintStream out,
      PrintWriter error) throws KlonException, IOException {
    Compiler compiler = new Compiler(root);
    StringBuilder buffer = new StringBuilder();
    while (true) {
      out.print(PROMPT);
      out.flush();
      int depth = 0;
      char current = (char) 0;
      buffer.setLength(0);
      boolean quotting = false;
      out.setHasOutput(false);
      try {
        while ("\n".indexOf(current) == -1 || depth > 0) {
          current = (char) in.read();
          buffer.append(current);
          if (OPEN_GROUP.indexOf(current) > -1) {
            depth++;
          }
          if (CLOSE_GROUP.indexOf(current) > -1) {
            depth--;
          }
          if ('"' == current) {
            quotting = !quotting;
            if (quotting) {
              depth++;
            } else {
              depth--;
            }
          }
        }
        Message message = compiler.fromString(buffer.toString());
        if (message != null) {
          KlonObject value = message.eval(root, root);
          if (!out.hasOutput()) {
            if ("String".equals(value.getType())
                || "Number".equals(value.getType())) {
              Message reportMessage = compiler.fromString("write;writeLine");
              Message argument = new Message();
              argument.setLiteral(value);
              reportMessage.addArgument(argument);
              reportMessage.eval(value, value);
            } else {
              Message reportMessage = compiler.fromString("inspect");
              reportMessage.eval(value, value);
            }
          }
        }
      } catch (KlonException e) {
        Message reportMessage = compiler.fromString("inspect");
        reportMessage.eval(e, e);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    KlonObject root = new KlonRoot(args);
    root.configure(root);
    MonitoredPrintStream newOut = new MonitoredPrintStream(System.out);
    System.setOut(newOut);
    new Shell().process(root, new InputStreamReader(System.in), newOut,
      new PrintWriter(new OutputStreamWriter(System.err)));
  }

}
