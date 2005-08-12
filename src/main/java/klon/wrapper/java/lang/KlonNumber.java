package klon.wrapper.java.lang;

import java.io.Serializable;

import klon.KlonException;
import klon.reflection.ExposedAs;

public final class KlonNumber implements Serializable {

  private static final long serialVersionUID = -763318978728958575L;

  @ExposedAs("+")
  public static Object add(Double receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() + ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("-")
  public static Object subtract(Double receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() - ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for -");
  }

  @ExposedAs("*")
  public static Object multiply(Double receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() * ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for *");
  }

  @ExposedAs("/")
  public static Object divide(Double receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() / ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for /");
  }

  @ExposedAs("^")
  public static Object power(Double receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return Math.pow(receiver.doubleValue(), ((Number) arg).doubleValue());
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("==")
  public static Object equals(Double receiver, Object arg) {
    return receiver.equals(arg);
  }

  @ExposedAs("<")
  public static Object lessThan(Double receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() < ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for <");
  }

  @ExposedAs(">")
  public static Object greaterThan(Double receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() > ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for >");
  }

  @ExposedAs("<=")
  public static Object lessThanEquals(Double receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() <= ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for <=");
  }

  @ExposedAs(">=")
  public static Object greaterThanEquals(Double receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return receiver.doubleValue() >= ((Number) arg).doubleValue();
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("abs")
  public static Object absoluteValue(Double receiver) {
    return Math.abs(receiver);
  }

  @ExposedAs("sqrt")
  public static Object squareRoot(Double receiver) {
    return Math.sqrt(receiver);
  }

  private KlonNumber() {
  }

}
