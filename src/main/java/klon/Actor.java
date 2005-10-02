package klon;

import java.util.Queue;

public class Actor implements Runnable {
  private Queue<KlonMessage> messages;
  private Scheduler scheduler;
  private KlonObject receiver;
  private KlonObject root;

  public Actor(Scheduler scheduler, KlonObject receiver) {
    this.scheduler = scheduler;
    this.receiver = receiver;
  }

  public void run() {
    while (!messages.isEmpty()) {
      KlonMessage current = messages.poll();
      try {
        current.eval(receiver, root);
      } catch (KlonObject e) {
        // TODO:  this has to go somewhere.  Future?
      }
      scheduler.yield();
    }
  }

}
