package klon;

import java.util.Queue;

public class Actor implements Runnable {

  private Queue<Future> messages;
  private Scheduler scheduler;
  private State state;

  public Actor(Scheduler scheduler, State state) {
    this.scheduler = scheduler;
    this.state = state;
  }

  public void run() {
    while (!messages.isEmpty()) {
      Future current = messages.poll();
      try {
        KlonMessage message = current.message();
        KlonObject receiver = current.receiver();
        current.setResult(KlonMessage.eval(message, receiver, state.getRoot()));
      } catch (KlonObject e) {
        current.setError(e);
      }
      scheduler.yield();
    }
  }

}
