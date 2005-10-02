package klon;

public class Scheduler {

  private Object createLock = new Object();
  private int activeCount;

  public int getActiveCount() {
    return activeCount;
  }

  public synchronized Coroutine create(Runnable worker) {
    Coroutine result = new Coroutine(this, worker);
    new Thread(result).start();
    try {
      createLock.wait();
    } catch (InterruptedException e) {
    }
    return result;
  }

  public synchronized void start() {
    createLock.notify();
    activeCount++;
    try {
      wait();
    } catch (InterruptedException e) {
    }
  }

  public synchronized void yield() {
    notify();
    try {
      wait();
    } catch (InterruptedException e) {
    }
  }

  public synchronized void stop() {
    activeCount--;
    notify();
  }
}
