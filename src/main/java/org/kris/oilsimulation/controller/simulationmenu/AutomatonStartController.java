package org.kris.oilsimulation.controller.simulationmenu;

import org.kris.oilsimulation.controller.handler.IterationCounter;
import org.kris.oilsimulation.controller.handler.SimulationHandlers;
import org.kris.oilsimulation.controller.handler.SimulationTimeLogger;
import org.kris.oilsimulation.controller.handler.ViewRefresher;
import org.kris.oilsimulation.controller.mapgenerator.GeneratedMap;
import org.kris.oilsimulation.controller.mapgenerator.MapGeneratorController;
import org.kris.oilsimulation.model.Model;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
  private Button setMapButton;
  @FXML
  private Button pollutionMapButton;
  @FXML
  private Button startButton;
  @FXML
  private Button clearButton;
  @FXML
  private Slider currentSliderX;
  @FXML
  private Slider currentSliderY;
  @FXML
  private Slider windSliderX;
  @FXML
  private Slider windSliderY;
  @FXML
  private CellChartController cellChartController;
  @FXML
  private ResourceBundle resources;

  public void startOrStopClicked() {
    simulationRunner.startOrStopClicked();
  }

  public void startSettingsClicked() {
    Optional<GeneratedMap> generatedMap = MapGeneratorController
        .getGeneratedMap(iterations.getScene().getWindow());

    generatedMap.ifPresent(simulationRunner::setNewGeneratedMap);
  }

  public void pollutionMapButtonClicked() {
    simulationRunner.pollutionMapButtonClicked(iterations.getScene().getWindow());
  }

  public void initModel(Model model) {
    createSimulationRunner(model);
  }

  private void createSimulationRunner(Model model) {
    WindCurrentSliders sliders =
        new WindCurrentSliders(currentSliderX, currentSliderY, windSliderX, windSliderY);
    ViewData viewData = new ViewData(iterationDelayMillis, sliders);
    SimulationTimeLogger logger = new SimulationTimeLogger();
    ViewRefresher viewRefresher =
        new ViewRefresher(setMapButton, pollutionMapButton, startButton,
            clearButton, iterationDelayMillis, resources);
    SimulationHandlers handlers = createSimulationHandlers(resources, logger, viewRefresher, viewData);
    this.simulationRunner = new SimulationRunner(model, viewData, handlers);
  }

  private SimulationHandlers createSimulationHandlers(ResourceBundle resources,
                                                      SimulationTimeLogger logger,
                                                      ViewRefresher viewRefresher,
                                                      ViewData viewData) {
    return new SimulationHandlers(
        asList(logger.onStartHandler(), viewRefresher.onStartHandler()),
        asList(logger.onStopHandler(), viewRefresher.onStopHandler()),
        singletonList(new IterationCounter(iterations, resources).afterStepHandler()),
        singletonList(viewData.onClearHandler()));
  }

  public void clear() {
    simulationRunner.clear();
  }

  public CellChartController getCellChartController() {
    return cellChartController;
  }
}