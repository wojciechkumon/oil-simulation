package org.kris.oilsimulation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorFactory {
  private static final Logger LOG = LoggerFactory.getLogger(ExecutorFactory.class);

  private ExecutorFactory() {}

  public static ScheduledExecutorService createScheduledSingleThreadDaemonExecutor() {
    return Executors.newSingleThreadScheduledExecutor(runnable ->
        prepareThread(runnable, "SingleThreadScheduledExecutor Thread"));
  }

  public static ExecutorService createSingleThreadDaemonExecutor() {
    return Executors.newSingleThreadExecutor(runnable ->
        prepareThread(runnable, "SingleThreadDaemonExecutor Thread"));
  }

  private static Thread prepareThread(Runnable runnable, String name) {
    Thread thread = Executors.defaultThreadFactory().newThread(runnable);
    thread.setName(name);
    thread.setUncaughtExceptionHandler((t, e) -> LOG.error("Thread uncaught exception", e));
    thread.setDaemon(true);
    return thread;
  }
}
