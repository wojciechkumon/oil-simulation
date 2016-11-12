package org.kris.oilsimulation.controller.handler;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ViewRefresher {
  private final Button startButton;
  private final ToggleGroup iterationDelayMillis;
  private final ResourceBundle resources;

  public ViewRefresher(Button startButton, ToggleGroup iterationDelayMillis,
                       ResourceBundle resources) {
    this.startButton = startButton;
    this.iterationDelayMillis = iterationDelayMillis;
    this.resources = resources;
  }

  public Handler onStartHandler() {
    return () ->
        Platform.runLater(() -> {
          startButton.setText(resources.getString("stop"));
          iterationDelayMillis.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(true));
        });
  }

  public Handler onStopHandler() {
    return () -> Platform.runLater(() -> {
      startButton.setText(resources.getString("start"));
      iterationDelayMillis.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(false));
    });
  }

}
