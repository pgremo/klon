package klon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Shell {

  private Reader in;
  private State state;

  public Shell(Reader in, State state) {
    this.in = in;
    this.state = state;
  }

  public void process() throws IOException, KlonObject {
    state
        .doString("writeLine(\"klon \", Properties klon.version, \".\", Properties klon.build)");
    while (!((ShellListener) state.getRoot().getState().getExitListener())
        .isExit()) {
      try {
        state.doString("write(\"\\n\", Properties ?klon.shell.prompt)");
        evalMessage(readMessage(in));
      } catch (KlonObject e) {
        state.doString(e, "asString print");
      }
    }
  }

  private void evalMessage(String message) throws KlonObject {
    ((ShellListener) state.getRoot().getState().getWriteListener())
        .setHasPrint(false);
    KlonObject value = state.doString(message);
    if (!((ShellListener) state.getRoot().getState().getWriteListener())
        .getHasPrint()) {
      state.doString(value, "print");
    }
  }

  private String readMessage(Reader in) throws IOException {
    StringBuilder buffer = new StringBuilder();
    int depth = 0;
    boolean quotting = false;
    char previous = 0;
    char current = 0;
    while ('\n' != current || depth > 0) {
      current = (char) in.read();
      buffer.append(current);
      if ("({[".indexOf(current) > -1 && !quotting) {
        depth++;
      }
      if (")}]".indexOf(current) > -1 && !quotting) {
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
