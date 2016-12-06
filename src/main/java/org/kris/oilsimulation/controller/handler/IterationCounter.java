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
    return new Handler() {
      @Override
      public void run() {
        Platform.runLater(() -> setIterationText(iterationCounter.incrementAndGet()));
      }

      @Override
      public void clear() {
        iterationCounter.set(0);
        Platform.runLater(() -> setIterationText(0));
      }
    };
  }

  private void setIterationText(int iteration) {
    iterationsLabel.setText(resources.getString("iteration") + " " + iteration);
  }

}
