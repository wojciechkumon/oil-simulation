package org.kris.oilsimulation.controller.simulationmenu;

import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.automatonview.GridView;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class MapSizeLabelUpdater {
  private final Model model;
  private final Label mapSizeLabel;
  private final ResourceBundle bundle;

  public MapSizeLabelUpdater(Model model, Label mapSizeLabel, ResourceBundle bundle) {
    this.model = model;
    this.mapSizeLabel = mapSizeLabel;
    this.bundle = bundle;
  }

  public void updateOnModelChange() {
    model.addChangeListener(
        (observable, oldValue, newValue) -> update(newValue.getAutomatonView().getGridView()));
  }

  private void update(GridView gridView) {
    String text = bundle.getString("mapSize") + ": "
        + gridView.getWidth() + "x" + gridView.getHeight();

    Platform.runLater(() -> mapSizeLabel.setText(text));
  }
}
