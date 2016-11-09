package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.Automaton;
import org.kris.oilsimulation.model.BlackWhiteState;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.automatonview.AutomatonView;
import org.kris.oilsimulation.model.automatonview.AutomatonViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GridCanvasController implements Initializable {
  private static final int CELL_SIZE = 20;

  private AutomatonView currentView;

  @FXML
  public VBox parentLayout;
  @FXML
  public Canvas canvas;

  public void initGrid() {
    currentView = AutomatonViewFactory.getEmptyView();
    canvas.widthProperty().bind(parentLayout.widthProperty().subtract(20));
    canvas.heightProperty().bind(parentLayout.heightProperty().subtract(20));

    canvas.widthProperty().addListener((observable) -> redraw());
    canvas.heightProperty().addListener((observable) -> redraw());
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.setStroke(Color.BLACK);
    graphicsContext.setLineWidth(0.5);
    redraw();
  }

  private void redraw() {
    GraphicsContext graphics = canvas.getGraphicsContext2D();
    graphics.setFill(Color.WHITE);
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    drawGrid(graphics);
  }

  private void drawGrid(GraphicsContext graphics) {
    for (int i = 0; i < currentView.getWidth(); i++) {
      for (int j = 0; j < currentView.getHeight(); j++) {
        graphics.strokeRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      }
    }
  }

  private void drawCells(Automaton newAutomaton) {
    currentView = newAutomaton.getAutomatonView();
    GraphicsContext graphics = canvas.getGraphicsContext2D();


    for (int i = 0; i < currentView.getHeight(); i++) {
      for (int j = 0; j < currentView.getWidth(); j++) {
        if (currentView.getState(i, j) == BlackWhiteState.WHITE) {
          graphics.setFill(Color.WHITE);
        } else {
          graphics.setFill(Color.BLACK);
        }
        graphics.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      }
    }

    drawGrid(graphics);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initGrid();
  }

  public void initModel(Model model) {
    model.addChangeListener((observable, oldValue, newValue) -> Platform.runLater(() -> drawCells(newValue)));
  }

}
