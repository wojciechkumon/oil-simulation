package org.kris.oilsimulation.controller.handler;

import java.util.Collection;

public class SimulationHandlers {
  private final Collection<Handler> onStartHandlers;
  private final Collection<Handler> onStopHandlers;
  private final Collection<Handler> afterStepHandlers;
  private final Collection<Handler> onClearHandlers;

  public SimulationHandlers(Collection<Handler> onStartHandlers,
                            Collection<Handler> onStopHandlers,
                            Collection<Handler> afterStepHandlers,
                            Collection<Handler> onClearHandlers) {
    this.onStartHandlers = onStartHandlers;
    this.onStopHandlers = onStopHandlers;
    this.afterStepHandlers = afterStepHandlers;
    this.onClearHandlers = onClearHandlers;
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

  public void clear() {
    onStartHandlers.forEach(Handler::clear);
    onStopHandlers.forEach(Handler::clear);
    afterStepHandlers.forEach(Handler::clear);
    onClearHandlers.forEach(Handler::run);
  }
}
