package klon;

public class Coroutine extends Thread {

  private Scheduler scheduler;
  private Runnable worker;

  public Coroutine(Scheduler scheduler, Runnable worker) {
    this.scheduler = scheduler;
    this.worker = worker;
  }

  public void run() {
    scheduler.begin();
    worker.run();
    scheduler.end();
  }

}
