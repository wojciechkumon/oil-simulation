package org.kris.oilsimulation.controller.handler;

import org.kris.oilsimulation.controller.AutomatonStartController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationTimeLogger {
  private static final Logger LOG = LoggerFactory.getLogger(AutomatonStartController.class);

  private volatile long totalTime = 0;
  private volatile long startMillis;

  public Handler onStartHandler() {
    return () -> startMillis = System.currentTimeMillis();
  }

  public Handler onStopHandler() {
    return () -> {
      long lastSimulationTime = System.currentTimeMillis() - startMillis;
      totalTime += lastSimulationTime;
      LOG.info("Last simulation time: {}", lastSimulationTime);
      LOG.info("Total simulation time: {}", totalTime);
    };
  }

}
