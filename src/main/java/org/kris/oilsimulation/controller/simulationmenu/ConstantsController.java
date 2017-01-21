package org.kris.oilsimulation.controller.simulationmenu;

import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.OilSimulationConstants;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class ConstantsController {
  private final Model model;
  private final Label cellSizeLabel;
  private final Label iterationTimeLabel;
  private final ResourceBundle bundle;

  public ConstantsController(Model model, Label cellSizeLabel, Label iterationTimeLabel,
                             ResourceBundle bundle) {
    this.model = model;
    this.cellSizeLabel = cellSizeLabel;
    this.iterationTimeLabel = iterationTimeLabel;
    this.bundle = bundle;
  }

  public void updateOnModelChange() {
    model.addChangeListener(
        (observable, oldValue, newValue) -> update(newValue.getOilSimulationConstants()));
  }

  private void update(OilSimulationConstants constants) {
    String cellSizeText = bundle.getString("cellSize") + ": " + (int) constants.getCellSize() + "m";
    String iterationTimeText = bundle.getString("iterationTime") + ": " + (int) constants.getTimeStep() + "s";

    Platform.runLater(() -> {
      cellSizeLabel.setText(cellSizeText);
      iterationTimeLabel.setText(iterationTimeText);
    });
  }
}
