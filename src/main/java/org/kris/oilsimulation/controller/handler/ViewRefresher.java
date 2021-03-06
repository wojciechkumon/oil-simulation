package org.kris.oilsimulation.controller.handler;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ViewRefresher {
  private final Button setMapButton;
  private final Button pollutionMapButton;
  private final Button startButton;
  private final Button clearButton;
  private final ToggleGroup iterationDelayMillis;
  private final ResourceBundle resources;

  public ViewRefresher(Button setMapButton, Button pollutionMapButton, Button startButton,
                       Button clearButton, ToggleGroup iterationDelayMillis,
                       ResourceBundle resources) {
    this.setMapButton = setMapButton;
    this.pollutionMapButton = pollutionMapButton;
    this.startButton = startButton;
    this.clearButton = clearButton;
    this.iterationDelayMillis = iterationDelayMillis;
    this.resources = resources;
  }

  public Handler onStartHandler() {
    return () ->
        Platform.runLater(() -> {
          startButton.setText(resources.getString("stop"));
          clearButton.setDisable(true);
          setMapButton.setDisable(true);
          pollutionMapButton.setDisable(true);
          iterationDelayMillis.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
        });
  }

  public Handler onStopHandler() {
    return () -> Platform.runLater(() -> {
      startButton.setText(resources.getString("start"));
      clearButton.setDisable(false);
      setMapButton.setDisable(false);
      pollutionMapButton.setDisable(false);
      iterationDelayMillis.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(false));
    });
  }
}
