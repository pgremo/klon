package klon.wrapper.java.lang;

import java.io.Serializable;

import klon.KlonException;
import klon.reflection.ExposedAs;

public class KlonFloat implements Serializable {

  private static final long serialVersionUID = -763318978728958575L;

  @ExposedAs("+")
  public static Object add(Float receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return new Float(receiver.floatValue() + ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("-")
  public static Object subtract(Float receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Float(receiver.floatValue() - ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for -");
  }

  @ExposedAs("*")
  public static Object multiply(Float receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Float(receiver.floatValue() * ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for *");
  }

  @ExposedAs("/")
  public static Object divide(Float receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return new Float(receiver.floatValue() / ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for /");
  }

  @ExposedAs("^")
  public static Object power(Float receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return new Float((int) Math.pow(receiver.floatValue(),
        ((Number) arg).intValue()));
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("==")
  public static Object equals(Float receiver, Object arg) {
    return arg instanceof Float && Boolean.valueOf(receiver.equals(arg));
  }

  @ExposedAs("<")
  public static Object lessThan(Float receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.floatValue() < ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for <");
  }

  @ExposedAs(">")
  public static Object greaterThan(Float receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.floatValue() > ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for >");
  }

  @ExposedAs("<=")
  public static Object lessThanEquals(Float receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.floatValue() <= ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for <=");
  }

  @ExposedAs(">=")
  public static Object greaterThanEquals(Float receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.floatValue() >= ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("abs")
  public static Object absoluteValue(Float receiver) {
    return new Float(Math.abs(receiver.floatValue()));
  }

  @ExposedAs("sqrt")
  public static Object squareRoot(Float receiver) {
    return new Float(Math.sqrt(receiver.floatValue()));
  }

}
