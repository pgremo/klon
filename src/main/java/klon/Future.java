package klon;

public class Future {

  private Scheduler scheduler;
  private KlonObject receiver;
  private KlonObject message;
  private KlonObject result;
  private KlonObject error;

  public Future(Scheduler scheduler, KlonObject receiver, KlonObject message) {
    this.scheduler = scheduler;
    this.receiver = receiver;
    this.message = message;
  }

  public KlonObject receiver() {
    return receiver;
  }

  public KlonObject message() {
    return message;
  }

  public KlonObject result() {
    return result;
  }

  public KlonObject error() {
    return error;
  }

  public void setResult(KlonObject result) {
    this.result = result;
  }

  public void setError(KlonObject error) {
    this.error = error;
  }

  public void waitOnResult() {
    while (!hasResult()) {
      scheduler.yield();
    }
  }

  public boolean hasResult() {
    return result != null || error != null;
  }

}
