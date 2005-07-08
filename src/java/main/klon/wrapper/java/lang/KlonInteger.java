package klon.wrapper.java.lang;

import java.io.Serializable;

import klon.KlonException;
import klon.KlonObject;
import klon.reflection.ExposedAs;
import klon.reflection.Up;

public class KlonInteger implements Serializable {

  private static final long serialVersionUID = -3990073762495531621L;

  @ExposedAs("+")
  public static KlonObject add(KlonObject receiver, KlonObject arg)
      throws KlonException {
    Object left = receiver.down();
    Object right = arg.down();
    try {
      return Up.UP.up(((Number) left).intValue() + ((Number) right).intValue());
    } catch (ClassCastException e) {
      throw new KlonException("Illegal Argument for +");
    }
  }

  @ExposedAs("-")
  public static KlonObject subtract(KlonObject receiver, KlonObject arg)
      throws KlonException {
    Object left = receiver.down();
    Object right = arg.down();
    if (left instanceof Number && right instanceof Number) {
      return Up.UP.up(((Number) left).intValue() - ((Number) right).intValue());
    }
    throw new KlonException("Illegal Argument for -");
  }

  @ExposedAs("*")
  public static KlonObject multiply(KlonObject receiver, KlonObject arg)
      throws KlonException {
    Object left = receiver.down();
    Object right = arg.down();
    if (left instanceof Number && right instanceof Number) {
      return Up.UP.up(((Number) left).intValue() * ((Number) right).intValue());
    }
    throw new KlonException("Illegal Argument for *");
  }

  @ExposedAs("/")
  public static KlonObject divide(KlonObject receiver, KlonObject arg)
      throws KlonException {
    Object left = receiver.down();
    Object right = arg.down();
    if (left instanceof Number && right instanceof Number) {
      return Up.UP.up(((Number) left).intValue() / ((Number) right).intValue());
    }
    throw new KlonException("Illegal Argument for /");
  }

  @ExposedAs("%")
  public static Object modulus(Integer receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Integer(receiver.intValue() % ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for %");
  }

  @ExposedAs("^")
  public static Object power(Integer receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return new Integer((int) Math.pow(receiver.intValue(),
        ((Number) arg).intValue()));
    }
    throw new KlonException("Illegal Argument for ^");
  }

  @ExposedAs("==")
  public static Object equals(Integer receiver, Object arg) {
    return arg instanceof Integer && Boolean.valueOf(receiver.equals(arg));
  }

  @ExposedAs("<")
  public static Object lessThan(Integer receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.intValue() < ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for <");
  }

  @ExposedAs(">")
  public static Object greaterThan(Integer receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.intValue() > ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for >");
  }

  @ExposedAs("<=")
  public static Object lessThanEquals(Integer receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.intValue() <= ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for <=");
  }

  @ExposedAs(">=")
  public static Object greaterThanEquals(Integer receiver, Object arg)
      throws KlonException {
    if (arg instanceof Number) {
      return new Boolean(receiver.intValue() >= ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for +");
  }

  @ExposedAs("|")
  public static Object or(Integer receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return new Integer(receiver.intValue() | ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for |");
  }

  @ExposedAs("&")
  public static Object and(Integer receiver, Object arg) throws KlonException {
    if (arg instanceof Number) {
      return new Integer(receiver.intValue() & ((Number) arg).intValue());
    }
    throw new KlonException("Illegal Argument for &");
  }

  @ExposedAs("abs")
  public static Object absoluteValue(Integer receiver) {
    return new Integer(java.lang.Math.abs(receiver.intValue()));
  }

  @ExposedAs("inc")
  public static Object increment(Integer receiver) {
    return new Integer(receiver.intValue() + 1);
  }

  @ExposedAs("dec")
  public static Object decrement(Integer receiver) {
    return new Integer(receiver.intValue() - 1);
  }

  @ExposedAs("sqrt")
  public static Object sqrt(Integer receiver) {
    return new Integer((int) Math.sqrt(receiver.intValue()));
  }

  @ExposedAs("not")
  public static Object not(Integer receiver) {
    return new Integer(~receiver.intValue());
  }
}
