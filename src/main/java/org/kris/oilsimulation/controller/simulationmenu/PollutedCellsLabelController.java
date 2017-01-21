package org.kris.oilsimulation.controller.simulationmenu;

import org.kris.oilsimulation.controller.pollutionmap.PollutionMeter;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.automatonview.CellView;
import org.kris.oilsimulation.model.automatonview.GridView;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class PollutedCellsLabelController {
  private final Model model;
  private final Label pollutedCellsLabel;
  private final ResourceBundle bundle;
  private final PollutionMeter pollutionMeter;

  public PollutedCellsLabelController(Model model, Label pollutedCellsLabel, ResourceBundle bundle) {
    this.model = model;
    this.pollutedCellsLabel = pollutedCellsLabel;
    this.bundle = bundle;
    this.pollutionMeter = new PollutionMeter();
  }

  public void updateOnModelChange() {
    model.addChangeListener(
        (observable, oldValue, newValue) -> update(newValue.getAutomatonView().getGridView()));
  }

  private void update(GridView gridView) {
    String text = bundle.getString("pollutedCells") + ": " + getPollutedCellsNumber(gridView);
    Platform.runLater(() -> pollutedCellsLabel.setText(text));
  }

  private int getPollutedCellsNumber(GridView gridView) {
    int pollutedCells = 0;

    for (int i = 0; i < gridView.getHeight(); i++) {
      for (int j = 0; j < gridView.getWidth(); j++) {
        CellView cellView = gridView.getCellView(i, j);
        if (pollutionMeter.isPolluted(cellView)) {
          pollutedCells++;
        }
      }
    }
    return pollutedCells;
  }
}
