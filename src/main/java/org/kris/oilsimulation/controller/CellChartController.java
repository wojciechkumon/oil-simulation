package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.automatonview.CellView;
import org.kris.oilsimulation.model.automatonview.GridView;
import org.kris.oilsimulation.model.automatonview.History;
import org.kris.oilsimulation.util.ExecutorFactory;

import java.util.concurrent.Executor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;

public class CellChartController {

  private final Executor executor = ExecutorFactory.createSingleThreadDaemonExecutor();

  @FXML
  private LineChart<Double, Double> cellChart;


  public void start(Canvas canvas, GridCanvasController canvasController) {
    canvas.setOnMouseClicked(event ->
        executor.execute(() -> showCorrectChart(event, canvasController)));
  }

  private void showCorrectChart(MouseEvent event, GridCanvasController canvasController) {
    GridView gridView = canvasController.getCurrentView().getGridView();
    double cellSize = canvasController.calculateCellSize(gridView);
    int col = (int) Math.floor(event.getX() / cellSize);
    int row = (int) Math.floor(event.getY() / cellSize);

    if (col < gridView.getWidth() && row < gridView.getHeight()) {
      updateChart(gridView, canvasController.getCurrentView().getHistory(), col, row);
    }
  }

  private void updateChart(GridView gridView, History history, int col, int row) {
    System.out.println(col + " " + row + " " + gridView.getCellView(row, col).getMass());
    XYChart.Series<Double, Double> newSeries = prepareSeries(gridView, history, col, row);
    Platform.runLater(() -> {
      cellChart.getData().clear();
      cellChart.getData().add(newSeries);
    });
  }

  private XYChart.Series<Double, Double> prepareSeries(GridView lastGridView, History history,
                                                       int col, int row) {
    XYChart.Series<Double, Double> series = new XYChart.Series<>();
    series.setName("Mass for cell [" + row + "," + col + "]");

    for (int i = 0; i < history.size(); i++) {
      GridView gridView = history.get(i);
      addGridViewData(col, row, series, i, gridView);
    }
    addGridViewData(col, row, series, history.size(), lastGridView);

    return series;
  }

  private void addGridViewData(int col, int row, XYChart.Series<Double, Double> series,
                               double index, GridView gridView) {
    CellView cell = gridView.getCellView(row, col);
    series.getData().add(new XYChart.Data<>(index, cell.getMass()));
  }
}
