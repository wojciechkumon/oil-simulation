package org.kris.oilsimulation.controller.handler;

import java.util.Collection;

public class SimulationHandlers {
  private final Collection<Handler> onStartHandlers;
  private final Collection<Handler> onStopHandlers;
  private final Collection<Handler> afterStepHandlers;

  public SimulationHandlers(Collection<Handler> onStartHandlers,
                            Collection<Handler> onStopHandlers,
                            Collection<Handler> afterStepHandlers) {
    this.onStartHandlers = onStartHandlers;
    this.onStopHandlers = onStopHandlers;
    this.afterStepHandlers = afterStepHandlers;
  }

  public void fireOnStartHandlers() {
    onStartHandlers.forEach(Handler::run);
  }

  public void fireOnStopHandlers() {
    onStopHandlers.forEach(Handler::run);
  }

  public void fireAfterStepHandlers() {
    afterStepHandlers.forEach(Handler::run);
  }

}
