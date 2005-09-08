package klon;

import java.util.Random;

public class MersenneTwister extends Random implements Cloneable {

  private static final long serialVersionUID = 3257853164429259320L;
  private static final int MASK = 0xffff0000;
  private static final int MULTIPLIER = 69069;
  private static final int MAX = 624;
  private static final int MIN = 397;
  private static int[] MAG = new int[]{0x0, 0x9908b0df};
  private int[] values;
  private int index;
  private long seed;

  /**
   * Constructor for MersenneTwister.
   */
  public MersenneTwister() {
    super();
  }

  /**
   * Constructor for MersenneTwister.
   * 
   * @param seed
   */
  public MersenneTwister(long seed) {
    super(seed);
  }

  /**
   * Initalize the pseudo random number generator. Don't pass in a long that's
   * bigger than an int (Mersenne Twister only uses the first 32 bits for its
   * seed).
   * 
   * @param seed
   *          used to initialize.
   */
  synchronized public void setSeed(long value) {
    super.setSeed(value);
    this.seed = value;

    int current = (int) value;
    values = new int[MAX];

    for (index = 0; index < MAX; index++) {
      values[index] = current & MASK;
      current = (MULTIPLIER * current) + 1;
      values[index] |= (current & MASK) >>> 16;
      current = (MULTIPLIER * current) + 1;
    }
  }

  /**
   * Returns an integer with <i>bits </i> bits filled with a random number.
   * 
   * @param bits
   *          required
   * 
   * @return integer with bits of random bites
   */
  protected int next(int bits) {
    int y;

    if (index >= MAX) {
      int i;

      for (i = 0; i < (MAX - MIN); i++) {
        y = (values[i] & Integer.MIN_VALUE)
            | (values[i + 1] & Integer.MAX_VALUE);
        values[i] = values[i + MIN] ^ (y >>> 1) ^ MAG[y & 0x1];
      }

      for (; i < (MAX - 1); i++) {
        y = (values[i] & Integer.MIN_VALUE)
            | (values[i + 1] & Integer.MAX_VALUE);
        values[i] = values[i + (MIN - MAX)] ^ (y >>> 1) ^ MAG[y & 0x1];
      }

      y = (values[i] & Integer.MIN_VALUE) | (values[0] & Integer.MAX_VALUE);
      values[i] = values[MIN - 1] ^ (y >>> 1) ^ MAG[y & 0x1];

      index = 0;
    }

    y = values[index++];
    y ^= y >>> 11;
    y ^= (y << 7) & 0x9d2c5680;
    y ^= (y << 15) & 0xefc60000;
    y ^= y >>> 18;

    return y >>> (32 - bits);
  }

  @Override
  public Object clone() {
    return new MersenneTwister(seed);
  }
}