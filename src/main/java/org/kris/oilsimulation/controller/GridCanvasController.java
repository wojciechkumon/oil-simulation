package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.Automaton;
import org.kris.oilsimulation.model.CellState;
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
  private static final Color WATER_COLOR = Color.rgb(67, 183, 222);
  private static final Color LAND_COLOR = Color.rgb(245, 189, 81);
  private static final Color BACKGROUND_COLOR = Color.rgb(240, 240, 240);
  private static final int MAX_MASS = 10_000;

  private ResourceBundle bundle;
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

  double calculateCellSize() {
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
    graphics.setFill(calculateCellColor(currentView.getState(i, j)));
    graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
  }

  private Color calculateCellColor(CellState cellState) {
    Color cellColor = cellState.isWater() ? WATER_COLOR : LAND_COLOR;
    double cellMass = cellState.getMass();
    double mass = cellMass < MAX_MASS ? cellMass : MAX_MASS;
    double oilPercent = mass / MAX_MASS;
    return cellColor.interpolate(Color.BLACK, oilPercent);
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
    bundle = resources;
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

  AutomatonView getCurrentView() {
    return currentView;
  }

  public void initModel(Model model) {
    model.addChangeListener((observable, oldValue, newValue) ->
        Platform.runLater(() -> setNewAutomaton(newValue)));
  }

  private void setNewAutomaton(Automaton automaton) {
    this.currentView = automaton.getAutomatonView();
    redraw();
  }

  public void initCellTooltip(CellTooltipController cellTooltipController) {
    cellTooltipController.start(canvas, this, bundle);
  }
}
