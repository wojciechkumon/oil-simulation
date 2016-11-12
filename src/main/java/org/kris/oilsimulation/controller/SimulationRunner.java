package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.controller.handler.SimulationHandlers;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.util.ExecutorFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.scene.control.ToggleGroup;

public class SimulationRunner {
  private final ScheduledExecutorService scheduler;
  private final Model model;
  private final ToggleGroup iterationDelayMillis;
  private final SimulationHandlers handlers;

  private ScheduledFuture<?> currentTask;

  public SimulationRunner(Model model, ToggleGroup iterationDelayMillis,
                          SimulationHandlers handlers) {
    this.scheduler = ExecutorFactory.createScheduledSingleThreadDaemonExecutor();
    this.model = model;
    this.iterationDelayMillis = iterationDelayMillis;
    this.handlers = handlers;
    this.currentTask = new DummyScheduledFuture<>();
  }

  public synchronized void startOrStopClicked() {
    if (isRunning()) {
      stop();
    } else {
      start(calculateDelayMillis());
    }
  }

  private int calculateDelayMillis() {
    SimulationSpeed simulationSpeed =
        (SimulationSpeed) iterationDelayMillis.getSelectedToggle().getUserData();
    return simulationSpeed.getGetIterationDelayMillis();
  }

  private boolean isRunning() {
    return !currentTask.isCancelled();
  }

  private void stop() {
    currentTask.cancel(false);
    handlers.fireOnStopHandlers();
  }

  private void start(int delayMillis) {
    if (delayMillis < 0) {
      runSingleStep();
      return;
    }

    currentTask = scheduler.scheduleAtFixedRate(
        this::runSingleStep,
        0, delayMillis, TimeUnit.MILLISECONDS);

    handlers.fireOnStartHandlers();
  }

  private void runSingleStep() {
    model.setAutomaton(model.getAutomaton().nextState());
    handlers.fireAfterStepHandlers();
  }

}
