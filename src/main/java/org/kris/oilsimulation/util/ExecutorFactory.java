package org.kris.oilsimulation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorFactory {
  private static final Logger LOG = LoggerFactory.getLogger(ExecutorFactory.class);

  private ExecutorFactory() {}

  public static ScheduledExecutorService createScheduledSingleThreadDaemonExecutor() {
    return Executors.newSingleThreadScheduledExecutor(runnable -> {
      Thread thread = Executors.defaultThreadFactory().newThread(runnable);
      thread.setName("SingleThreadScheduledExecutor Thread");
      thread.setUncaughtExceptionHandler((t, e) -> LOG.error("Thread uncaught exception", e));
      thread.setDaemon(true);
      return thread;
    });
  }

}
