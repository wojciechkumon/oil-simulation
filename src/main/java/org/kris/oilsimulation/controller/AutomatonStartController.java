package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.controller.handler.IterationCounter;
import org.kris.oilsimulation.controller.handler.SimulationHandlers;
import org.kris.oilsimulation.controller.handler.SimulationTimeLogger;
import org.kris.oilsimulation.controller.handler.ViewRefresher;
import org.kris.oilsimulation.model.Model;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class AutomatonStartController {

  private SimulationRunner simulationRunner;

  @FXML
  private Label iterations;
  @FXML
  private ToggleGroup iterationDelayMillis;
  @FXML
  private Button startButton;
  @FXML
  private Button clearButton;
  @FXML
  private ResourceBundle resources;

  public void startOrStopClicked() {
    simulationRunner.startOrStopClicked();
  }

  public void initModel(Model model) {
    createSimulationRunner(model);
  }

  private void createSimulationRunner(Model model) {
    SimulationTimeLogger logger = new SimulationTimeLogger();
    ViewRefresher viewRefresher =
        new ViewRefresher(startButton, clearButton, iterationDelayMillis, resources);
    SimulationHandlers handlers = createSimulationHandlers(resources, logger, viewRefresher);
    this.simulationRunner = new SimulationRunner(model, iterationDelayMillis, handlers);
  }

  private SimulationHandlers createSimulationHandlers(ResourceBundle resources,
                                                      SimulationTimeLogger logger,
                                                      ViewRefresher viewRefresher) {
    return new SimulationHandlers(
        asList(logger.onStartHandler(), viewRefresher.onStartHandler()),
        asList(logger.onStopHandler(), viewRefresher.onStopHandler()),
        singletonList(new IterationCounter(iterations, resources).afterStepHandler()));
  }

  public void clear() {
    simulationRunner.clear();
  }
}
