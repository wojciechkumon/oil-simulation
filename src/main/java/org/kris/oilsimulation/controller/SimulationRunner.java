package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.controller.handler.SimulationHandlers;
import org.kris.oilsimulation.controller.pollutionmap.PollutionMapController;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.OilAutomaton;
import org.kris.oilsimulation.util.ExecutorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.stage.Window;

public class SimulationRunner {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationRunner.class);

  private final ScheduledExecutorService scheduler;
  private final Model model;
  private final SimulationHandlers handlers;
  private final ViewData viewData;

  private ScheduledFuture<?> currentTask;

  public SimulationRunner(Model model, ViewData viewData,
                          SimulationHandlers handlers) {
    this.viewData = viewData;
    this.scheduler = ExecutorFactory.createScheduledSingleThreadDaemonExecutor();
    this.model = model;
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
    return viewData.getIterationDelayMillis();
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
    model.setAutomaton(model.getAutomaton().nextState(viewData.getOilAutomatonNextSettings()));
    handlers.fireAfterStepHandlers();
  }

  public void clear() {
    LOG.info("Clear button pressed");
    if (isRunning()) {
      stop();
    }
    handlers.clear();
    model.setAutomaton(model.getAutomaton().clearState());
  }

  public void pollutionMapButtonClicked(Window window) {
    PollutionMapController.showPollutionMap(window, model.getAutomaton().getAutomatonView());
  }
}
