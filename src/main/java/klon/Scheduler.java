package klon;

public class Scheduler {

  private Object createLock = new Object();
  private int activeCount;

  public int getActiveCount() {
    return activeCount;
  }

  public synchronized void create(Coroutine coroutine) {
    coroutine.start();
    try {
      createLock.wait();
    } catch (InterruptedException e) {
    }
  }

  public synchronized void begin() {
    createLock.notify();
    try {
      wait();
    } catch (InterruptedException e) {
    }
    activeCount++;
  }

  public synchronized void yield() {
    notify();
    try {
      wait();
    } catch (InterruptedException e) {
    }
  }

  public synchronized void end() {
    activeCount--;
    notify();
  }
}
