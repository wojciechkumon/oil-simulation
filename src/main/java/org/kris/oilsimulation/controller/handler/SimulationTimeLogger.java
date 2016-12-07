package org.kris.oilsimulation.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationTimeLogger {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationTimeLogger.class);

  private volatile long totalTime = 0;
  private volatile long startMillis;

  public Handler onStartHandler() {
    return () -> startMillis = System.currentTimeMillis();
  }

  public Handler onStopHandler() {
    return new Handler() {
      @Override
      public void run() {
        long lastSimulationTime = System.currentTimeMillis() - startMillis;
        totalTime += lastSimulationTime;
        LOG.info("Last simulation time: {}ms", lastSimulationTime);
        LOG.info("Total simulation time: {}ms", totalTime);
      }

      @Override
      public void clear() {
        startMillis = 0;
        totalTime = 0;
      }
    };
  }

}
