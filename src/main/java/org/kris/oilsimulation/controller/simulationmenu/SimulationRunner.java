package org.kris.oilsimulation.controller.simulationmenu;

import org.kris.oilsimulation.controller.handler.SimulationHandlers;
import org.kris.oilsimulation.controller.mapgenerator.GeneratedMap;
import org.kris.oilsimulation.controller.pollutionmap.PollutionMapController;
import org.kris.oilsimulation.controller.util.ExecutorFactory;
import org.kris.oilsimulation.model.ExternalConditions;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.OilAutomaton;
import org.kris.oilsimulation.model.OilSimulationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.stage.Window;

public class SimulationRunner {
  private static final Logger LOG = LoggerFactory.getLogger(SimulationRunner.class);

  private final Model model;
  private final ViewData viewData;
  private final SimulationHandlers handlers;
  private final ScheduledExecutorService scheduler;

  private ScheduledFuture<?> currentTask;

  public SimulationRunner(Model model, ViewData viewData,
                          SimulationHandlers handlers) {
    this.model = model;
    this.viewData = viewData;
    this.handlers = handlers;
    this.scheduler = ExecutorFactory.createScheduledSingleThreadDaemonExecutor();
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

  public synchronized void clear() {
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

  public void setNewGeneratedMap(GeneratedMap generatedMap) {
    handlers.clear();
    OilSimulationConstants constants = model.getAutomaton().getOilSimulationConstants();
    model.setAutomaton(
        OilAutomaton.newAutomaton(
            generatedMap.getSize(),
            ExternalConditions.getNoInfluenceConditions(),
            constants,
            generatedMap.getInitialStates()));
  }
}
