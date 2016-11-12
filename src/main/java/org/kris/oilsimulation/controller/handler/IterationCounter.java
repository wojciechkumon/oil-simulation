package org.kris.oilsimulation.controller.handler;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class IterationCounter {
  private final AtomicInteger iterationCounter = new AtomicInteger(0);
  private final Label iterationsLabel;
  private final ResourceBundle resources;

  public IterationCounter(Label iterationsLabel, ResourceBundle resources) {
    this.iterationsLabel = iterationsLabel;
    this.resources = resources;
  }

  public Handler afterStepHandler() {
    return () ->
        Platform.runLater(() ->
            iterationsLabel.setText(
                resources.getString("iteration") + " " + iterationCounter.incrementAndGet()));
  }

}
