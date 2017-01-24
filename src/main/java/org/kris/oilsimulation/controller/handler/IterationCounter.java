package org.kris.oilsimulation.controller.handler;

import org.kris.oilsimulation.model.Model;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class IterationCounter {
  private final AtomicInteger iterationCounter = new AtomicInteger(0);
  private final Label iterationsLabel;
  private final Label timeElapsed;
  private final Model model;
  private final ResourceBundle resources;

  public IterationCounter(Label iterationsLabel, Label timeElapsed,
                          Model model, ResourceBundle resources) {
    this.iterationsLabel = iterationsLabel;
    this.timeElapsed = timeElapsed;
    this.model = model;
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
    String iterationsText = resources.getString("iteration") + " " + iteration;

    double timeStep = model.getAutomaton().getOilSimulationConstants().getTimeStep();
    int hours = (int) (timeStep * iteration / 3600);
    String timeElapsedText = resources.getString("timeElapsed") + " " + hours + "h";

    iterationsLabel.setText(iterationsText);
    timeElapsed.setText(timeElapsedText);
  }

}
