package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.controller.handler.IterationCounter;
import org.kris.oilsimulation.controller.handler.SimulationHandlers;
import org.kris.oilsimulation.controller.handler.SimulationTimeLogger;
import org.kris.oilsimulation.controller.handler.ViewRefresher;
import org.kris.oilsimulation.model.Model;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class AutomatonStartController {

  private SimulationRunner simulationRunner;

  @FXML
  private Label iterations;
  @FXML
  private ToggleGroup iterationDelayMillis;
  @FXML
  private Button startSettingsButton;
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

  public void startSettingsClicked() throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
        .getResource("view/fxml/mapGenerator.fxml"));
    Parent root = fxmlLoader.load();
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initStyle(StageStyle.UNDECORATED);
    stage.setTitle("ABC");
    stage.setScene(new Scene(root));
    stage.show();
//    MapGeneratorController mapController = new MapGeneratorController();
//    mapController.loadGenerationMenu();
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
        new ViewRefresher(startSettingsButton, startButton, clearButton, iterationDelayMillis, resources);
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
        asList(viewData.onClearHandler(), viewRefresher.onClearHandler()));
  }

  public void clear() {
    simulationRunner.clear();
  }

  public CellChartController getCellChartController() {
    return cellChartController;
  }
}
