package org.kris.oilsimulation.controller.pollutionmap;

import org.kris.oilsimulation.controller.util.WindowUtil;
import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Window;

import static org.kris.oilsimulation.controller.Colors.LAND_COLOR;
import static org.kris.oilsimulation.controller.Colors.WATER_COLOR;

public class PollutionMapController {
  private static final String ICON_PATH = "view/img/icon.png";

  @FXML
  private Canvas canvas;

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
  }

  private PollutionMap getPollutionMap(AutomatonView automatonView) {
    return new PollutionMapCalculator(new PollutionMeter()).getPollutionMap(automatonView);
  }

  private void drawPollutionMap(PollutionMap pollutionMap) {
    int mapSize = pollutionMap.getCols();
    double cellSize = calculateCellSize(mapSize);
    GraphicsContext graphics = canvas.getGraphicsContext2D();
    drawCells(graphics, pollutionMap, mapSize, cellSize);
    drawGrid(graphics, mapSize, cellSize);
  }

  private double calculateCellSize(int mapSize) {
    double cellMaxWidth = canvas.getWidth() / mapSize;
    double cellMaxHeight = canvas.getHeight() / mapSize;

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
    graphics.setFill(getCellColor(pollutionMap.get(i, j), pollutionMap.getMaxIterations()));
    graphics.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
  }

  private Color getCellColor(PollutionCell cell, double maxIterations) {
    Color cleanColor = cell.isWater() ? WATER_COLOR : LAND_COLOR;
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
}
