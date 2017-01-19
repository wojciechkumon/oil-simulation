package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.Automaton;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.automatonview.AutomatonView;
import org.kris.oilsimulation.model.automatonview.AutomatonViewFactory;
import org.kris.oilsimulation.model.automatonview.CellView;
import org.kris.oilsimulation.model.automatonview.GridView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static org.kris.oilsimulation.controller.Colors.BACKGROUND_COLOR;
import static org.kris.oilsimulation.controller.Colors.LAND_COLOR;
import static org.kris.oilsimulation.controller.Colors.WATER_COLOR;

public class GridCanvasController implements Initializable {
  private static final int MAX_MASS = 10_000;

  private ResourceBundle bundle;
  private AutomatonView currentView;

  @FXML
  public VBox parentLayout;
  @FXML
  public Canvas canvas;

  private void redraw(GridView gridView) {
    double cellSize = calculateCellSize(gridView);

    GraphicsContext graphics = canvas.getGraphicsContext2D();
    clearCanvas(graphics);

    drawCells(gridView, graphics, cellSize);
    drawGrid(gridView, graphics, cellSize);
  }

  double calculateCellSize(GridView gridView) {
    if (gridView.getWidth() == 0) {
      return 0;
    }

    double cellMaxWidth = canvas.getWidth() / gridView.getWidth();
    double cellMaxHeight = canvas.getHeight() / gridView.getHeight();

    return cellMaxWidth <= cellMaxHeight ? cellMaxWidth : cellMaxHeight;
  }

  private void clearCanvas(GraphicsContext graphics) {
    graphics.setFill(BACKGROUND_COLOR);
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  private void drawCells(GridView gridView, GraphicsContext graphics, double cellSize) {
    for (int i = 0; i < gridView.getHeight(); i++) {
      for (int j = 0; j < gridView.getWidth(); j++) {
        drawOilCell(gridView, graphics, cellSize, i, j);
      }
    }
  }

  private void drawOilCell(GridView gridView, GraphicsContext graphics,
                           double cellSize, int i, int j) {
    graphics.setFill(calculateCellColor(gridView.getCellView(i, j)));
    graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
  }

  private Color calculateCellColor(CellView cellView) {
    Color cellColor = cellView.isWater() ? WATER_COLOR : LAND_COLOR;
    double cellMass = cellView.getMass();
    double mass = cellMass < MAX_MASS ? cellMass : MAX_MASS;
    double oilPercent = mass / MAX_MASS;
    return cellColor.interpolate(Color.BLACK, oilPercent);
  }

  private void drawGrid(GridView gridView, GraphicsContext graphics, double cellSize) {
    for (int i = 0; i < gridView.getWidth(); i++) {
      for (int j = 0; j < gridView.getHeight(); j++) {
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
    this.currentView = AutomatonViewFactory.getEmptyView();
    canvas.widthProperty().bind(parentLayout.widthProperty().subtract(20));
    canvas.heightProperty().bind(parentLayout.heightProperty().subtract(20));

    canvas.widthProperty().addListener((observable) -> redraw(this.currentView.getGridView()));
    canvas.heightProperty().addListener((observable) -> redraw(this.currentView.getGridView()));
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
    redraw(currentView.getGridView());
  }

  public void initCellTooltip(CellTooltipController cellTooltipController) {
    cellTooltipController.start(canvas, this, bundle);
  }

  public void initCellChartController(CellChartController chartController) {
    chartController.start(canvas, this, bundle);
  }
}
