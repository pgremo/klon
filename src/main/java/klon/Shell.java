package klon;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

public class Shell {
  private static final String PROMPT = "klon> ";
  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";

  public void process(Reader in, PrintWriter out, PrintWriter error) {
    Compiler compiler = new Compiler();
    StringBuilder buffer = new StringBuilder();
    while (true) {
      out.print(PROMPT);
      out.flush();
      int depth = 0;
      char current = (char) 0;
      buffer.setLength(0);
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
        }
        Message message = compiler.fromString(buffer.toString());
        KlonObject value = message.eval(Klon.ROOT, Klon.ROOT);
        Message reportMessage = compiler.fromString("writeLine");
        Message argument = new Message();
        argument.setLiteral(value);
        reportMessage.setArguments(argument);
        reportMessage.eval(value, value);
      } catch (Exception e) {
        e.printStackTrace(error);
        error.flush();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Klon.init(args);
    new Shell().process(new InputStreamReader(System.in), new PrintWriter(
        new OutputStreamWriter(System.out)), new PrintWriter(
        new OutputStreamWriter(System.err)));
  }

}
