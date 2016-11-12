package org.kris.oilsimulation.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorFactory {

  private ExecutorFactory() {}

  public static ScheduledExecutorService createScheduledSingleThreadDaemonExecutor() {
    return Executors.newSingleThreadScheduledExecutor(runnable -> {
      Thread thread = Executors.defaultThreadFactory().newThread(runnable);
      thread.setDaemon(true);
      return thread;
    });
  }

}
