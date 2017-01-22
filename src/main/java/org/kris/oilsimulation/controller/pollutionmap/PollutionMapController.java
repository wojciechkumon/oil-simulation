package org.kris.oilsimulation.controller.pollutionmap;

import org.kris.oilsimulation.controller.Colors;
import org.kris.oilsimulation.controller.util.WindowUtil;
import org.kris.oilsimulation.model.automatonview.AutomatonView;
import org.kris.oilsimulation.model.automatonview.GridView;

import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Window;

import static org.kris.oilsimulation.controller.Colors.LAND_COLOR;
import static org.kris.oilsimulation.controller.Colors.WATER_COLOR;

public class PollutionMapController {
  private static final String ICON_PATH = "view/img/icon.png";

  @FXML
  private Canvas mapCanvas;
  @FXML
  private Canvas landPollutionGradient;
  @FXML
  private Canvas waterPollutionGradient;
  @FXML
  private Label maxIterationsLabel1;
  @FXML
  private Label maxIterationsLabel2;
  @FXML
  private Label xCellLabel;
  @FXML
  private Label yCellLabel;
  @FXML
  private Label pollutedIterationsLabel;
  @FXML
  private ResourceBundle resources;

  public static void showPollutionMap(Window mainWindow, AutomatonView automatonView) {
    Consumer<FXMLLoader> adjustFxml = fxmlLoader -> {
      PollutionMapController controller = fxmlLoader.getController();
      controller.setAutomatonView(automatonView);
    };

    WindowUtil.showWindowAndGetController(mainWindow, "view/fxml/pollutionMap.fxml",
        "pollutionMap", ICON_PATH, adjustFxml);
  }

  private void setAutomatonView(AutomatonView automatonView) {
    PollutionMap pollutionMap = getPollutionMap(automatonView);
    drawPollutionMap(pollutionMap);
    drawGradients();
    setLabels(pollutionMap.getMaxIterations());
    mapCanvas.setOnMouseClicked(
        event -> refreshClickedCellStats(automatonView, pollutionMap, event));
  }

  private PollutionMap getPollutionMap(AutomatonView automatonView) {
    return new PollutionMapCalculator(new PollutionMeter()).getPollutionMap(automatonView);
  }

  private void drawPollutionMap(PollutionMap pollutionMap) {
    int mapSize = pollutionMap.getCols();
    double cellSize = calculateCellSize(mapSize);
    GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
    drawCells(graphics, pollutionMap, mapSize, cellSize);
    drawGrid(graphics, mapSize, cellSize);
  }

  private double calculateCellSize(int mapSize) {
    double cellMaxWidth = mapCanvas.getWidth() / mapSize;
    double cellMaxHeight = mapCanvas.getHeight() / mapSize;

    return cellMaxWidth <= cellMaxHeight ? cellMaxWidth : cellMaxHeight;
  }

  private void drawCells(GraphicsContext graphics, PollutionMap pollutionMap, int mapSize,
                         double cellSize) {
    for (int i = 0; i < mapSize; i++) {
      for (int j = 0; j < mapSize; j++) {
        drawCell(graphics, pollutionMap, cellSize, i, j);
      }
    }
  }

  private void drawCell(GraphicsContext graphics, PollutionMap pollutionMap,
                        double cellSize, int i, int j) {
    Color cellColor = getCellColor(pollutionMap.get(j, i), pollutionMap.getMaxIterations());
    graphics.setFill(cellColor);
    graphics.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
  }

  private Color getCellColor(PollutionCell cell, double maxIterations) {
    Color cleanColor = cell.isWater() ? WATER_COLOR : LAND_COLOR;
    if (maxIterations == 0) {
      return cleanColor;
    }
    double oilPercent = cell.getPollutedIterations() / maxIterations;
    return cleanColor.interpolate(Color.BLACK, oilPercent);
  }

  private void drawGrid(GraphicsContext graphics, int mapSize, double cellSize) {
    for (int i = 0; i < mapSize; i++) {
      for (int j = 0; j < mapSize; j++) {
        graphics.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
      }
    }
  }

  private void drawGradients() {
    drawRadialGradient(landPollutionGradient, Colors.LAND_COLOR);
    drawRadialGradient(waterPollutionGradient, Colors.WATER_COLOR);
  }

  private void drawRadialGradient(Canvas canvas, Color color) {
    GraphicsContext graphics = canvas.getGraphicsContext2D();
    graphics.setFill(new LinearGradient(0, 0, 1, 1, true,
        CycleMethod.REFLECT,
        new Stop(0.0, Color.BLACK),
        new Stop(1.0, color)));
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  private void setLabels(int maxIterations) {
    if (maxIterations > 0) {
      String stringIterations = Integer.toString(maxIterations);
      maxIterationsLabel1.setText(stringIterations);
      maxIterationsLabel2.setText(stringIterations);
    }
  }

  private void refreshClickedCellStats(AutomatonView automatonView, PollutionMap pollutionMap, MouseEvent event) {
    double xPixel = event.getX();
    double yPixel = event.getY();
    GridView gridView = automatonView.getGridView();
    int col = (int) ((xPixel / mapCanvas.getWidth()) * gridView.getWidth());
    int row = (int) ((yPixel / mapCanvas.getHeight()) * gridView.getHeight());

    PollutionCell pollutionCell = pollutionMap.get(row, col);
    updateCellLabels(row, col, pollutionCell);
  }

  private void updateCellLabels(int row, int col, PollutionCell pollutionCell) {
    String xText = resources.getString("x") + " " + col;
    String yText = resources.getString("y") + " " + row;
    String pollutedIterationsText = resources.getString("pollutedIterations") + " "
        + pollutionCell.getPollutedIterations();

    Platform.runLater(() -> {
      xCellLabel.setText(xText);
      yCellLabel.setText(yText);
      pollutedIterationsLabel.setText(pollutedIterationsText);
    });
  }
}
