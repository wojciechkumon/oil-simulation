package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.Model;

import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AutomatonStartController {
  private final ScheduledExecutorService scheduler;

  private Model model;
  private ScheduledFuture<?> currentTask;

  @FXML
  private Button startButton;
  @FXML
  private ResourceBundle resources;

  public AutomatonStartController() {
    // setting threads as daemons to be able to close application process when closing window
    this.scheduler = Executors.newScheduledThreadPool(1, runnable -> {
      Thread thread = Executors.defaultThreadFactory().newThread(runnable);
      thread.setDaemon(true);
      return thread;
    });
  }

  public synchronized void startAutomaton() {
    if (isRunning()) {
      stop();
    } else {
      runModel(model, 300);
    }
  }

  private boolean isRunning() {
    return currentTask != null && !currentTask.isCancelled();
  }

  private void stop() {
    currentTask.cancel(false);
    startButton.setText(resources.getString("start"));
  }

  private void runModel(Model model, int periodMillis) {
    currentTask = scheduler.scheduleAtFixedRate(
        () -> model.setAutomaton(model.getAutomaton().nextState()),
        0, periodMillis, TimeUnit.MILLISECONDS);
    startButton.setText(resources.getString("stop"));
  }

  public void initModel(Model model) {
    this.model = model;
  }


}
