package klon;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.LinkedList;

public class Shell {
  private static final String PROMPT = "klon> ";
  private static final String OPEN_GROUP = "({[";
  private static final String CLOSE_GROUP = ")}]";

  public void process(Reader in, PrintWriter out, PrintWriter error) {
    Compiler compiler = new Compiler();
    StringBuilder buffer = new StringBuilder();
    LinkedList<Character> depth = new LinkedList<Character>();
    out.print(PROMPT);
    out.flush();
    while (true) {
      try {
        char current = (char) in.read();
        buffer.append(current);
        if (OPEN_GROUP.indexOf(current) > -1) {
          depth.addFirst(current);
        }
        if (CLOSE_GROUP.indexOf(current) > -1) {
          depth.poll();
        }
        if ("\n".indexOf(current) > -1 && depth.isEmpty()) {
          Message message = (Message) compiler.fromString(buffer.toString());
          KlonObject value = message.eval(Klon.ROOT, Klon.ROOT);
          out.println(value.toString());
          out.flush();
          out.print(PROMPT);
          out.flush();
          buffer.setLength(0);
        }
      } catch (Exception e) {
        e.printStackTrace(error);
        error.flush();
        buffer.setLength(0);
      }
    }
  }

  public static void main(String... args) {
    new Shell().process(new InputStreamReader(System.in), new PrintWriter(
        new OutputStreamWriter(System.out)), new PrintWriter(
        new OutputStreamWriter(System.err)));
  }

}
