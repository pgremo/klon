package klon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MonitoredPrintStream extends PrintStream {

  private boolean hasOutput;

  public MonitoredPrintStream(OutputStream out) {
    super(out);
  }

  public MonitoredPrintStream(OutputStream out, boolean autoFlush) {
    super(out, autoFlush);
  }

  public MonitoredPrintStream(OutputStream out, boolean autoFlush,
      String encoding) throws UnsupportedEncodingException {
    super(out, autoFlush, encoding);
  }

  public MonitoredPrintStream(String fileName) throws FileNotFoundException {
    super(fileName);
  }

  public MonitoredPrintStream(String fileName, String csn)
      throws FileNotFoundException, UnsupportedEncodingException {
    super(fileName, csn);
  }

  public MonitoredPrintStream(File file) throws FileNotFoundException {
    super(file);
  }

  public MonitoredPrintStream(File file, String csn)
      throws FileNotFoundException, UnsupportedEncodingException {
    super(file, csn);
  }

  public boolean hasOutput() {
    return hasOutput;
  }

  public void setHasOutput(boolean hasOutput) {
    this.hasOutput = hasOutput;
  }

  @Override
  public void print(boolean b) {
    hasOutput = true;
    super.print(b);
  }

  @Override
  public void print(char c) {
    hasOutput = true;
    super.print(c);
  }

  @Override
  public void print(char[] s) {
    hasOutput = true;
    super.print(s);
  }

  @Override
  public void print(double d) {
    hasOutput = true;
    super.print(d);
  }

  @Override
  public void print(float f) {
    hasOutput = true;
    super.print(f);
  }

  @Override
  public void print(int i) {
    hasOutput = true;
    super.print(i);
  }

  @Override
  public void print(long l) {
    hasOutput = true;
    super.print(l);
  }

  @Override
  public void print(Object obj) {
    hasOutput = true;
    super.print(obj);
  }

  @Override
  public void print(String s) {
    hasOutput = true;
    super.print(s);
  }

  @Override
  public PrintStream printf(Locale l, String format, Object... args) {
    hasOutput = true;
    return super.printf(l, format, args);
  }

  @Override
  public PrintStream printf(String format, Object... args) {
    hasOutput = true;
    return super.printf(format, args);
  }

  @Override
  public void println() {
    hasOutput = true;
    super.println();
  }

  @Override
  public void println(boolean x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(char x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(char[] x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(double x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(float x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(int x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(long x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(Object x) {
    hasOutput = true;
    super.println(x);
  }

  @Override
  public void println(String x) {
    hasOutput = true;
    super.println(x);
  }

}
