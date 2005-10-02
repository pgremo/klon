package klon;

public class Coroutine implements Runnable {

  private Scheduler scheduler;
  private Runnable worker;

  public Coroutine(Scheduler scheduler, Runnable worker) {
    this.scheduler = scheduler;
    this.worker = worker;
  }

  public void run() {
    scheduler.start();
    worker.run();
    scheduler.stop();
  }

}
