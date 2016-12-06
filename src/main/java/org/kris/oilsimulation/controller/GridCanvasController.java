package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.Automaton;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.OilCellState;
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
  private static final int BLUE_R = 67;
  private static final int BLUE_G = 183;
  private static final int BLUE_B = 222;
  private static final Color BACKGROUND_COLOR = Color.rgb(240, 240, 240);

  private AutomatonView currentView;

  @FXML
  public VBox parentLayout;
  @FXML
  public Canvas canvas;

  private void redraw() {
    double cellSize = calculateCellSize();

    GraphicsContext graphics = canvas.getGraphicsContext2D();
    clearCanvas(graphics);

    drawCells(graphics, cellSize);
    drawGrid(graphics, cellSize);
  }

  private double calculateCellSize() {
    if (currentView.getWidth() == 0) {
      return 0;
    }

    double cellMaxWidth = canvas.getWidth() / currentView.getWidth();
    double cellMaxHeight = canvas.getHeight() / currentView.getHeight();

    return cellMaxWidth <= cellMaxHeight ? cellMaxWidth : cellMaxHeight;
  }

  private void clearCanvas(GraphicsContext graphics) {
    graphics.setFill(BACKGROUND_COLOR);
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  private void drawCells(GraphicsContext graphics, double cellSize) {
    for (int i = 0; i < currentView.getHeight(); i++) {
      for (int j = 0; j < currentView.getWidth(); j++) {
        drawOilCell(graphics, cellSize, i, j);
      }
    }
  }

  private void drawOilCell(GraphicsContext graphics, double cellSize, int i, int j) {
    double max = 7_000;
    graphics.setFill(calculateCellColor(max, (OilCellState) currentView.getState(i, j)));
    graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
  }

  private Color calculateCellColor(double max, OilCellState oilCellState) {
    double cellMass = oilCellState.getMass();
    double mass = cellMass < max ? cellMass : max;
    double cleanPercent = 1 - (mass / max);
    return Color.rgb((int) (BLUE_R * cleanPercent), (int) (BLUE_G * cleanPercent), (int) (BLUE_B * cleanPercent));
  }

  private void drawGrid(GraphicsContext graphics, double cellSize) {
    for (int i = 0; i < currentView.getWidth(); i++) {
      for (int j = 0; j < currentView.getHeight(); j++) {
        graphics.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
      }
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initGrid();
  }

  private void initGrid() {
    currentView = AutomatonViewFactory.getEmptyView();
    canvas.widthProperty().bind(parentLayout.widthProperty().subtract(20));
    canvas.heightProperty().bind(parentLayout.heightProperty().subtract(20));

    canvas.widthProperty().addListener((observable) -> redraw());
    canvas.heightProperty().addListener((observable) -> redraw());
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.setStroke(Color.BLACK);
    graphicsContext.setLineWidth(0.5);
  }

  public void initModel(Model model) {
    model.addChangeListener((observable, oldValue, newValue) ->
        Platform.runLater(() -> setNewAutomaton(newValue)));
  }

  private void setNewAutomaton(Automaton automaton) {
    this.currentView = automaton.getAutomatonView();
    redraw();
  }

}
