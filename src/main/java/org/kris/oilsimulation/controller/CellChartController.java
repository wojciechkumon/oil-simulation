package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.automatonview.CellView;
import org.kris.oilsimulation.model.automatonview.GridView;
import org.kris.oilsimulation.model.automatonview.History;
import org.kris.oilsimulation.util.ExecutorFactory;

import java.util.ResourceBundle;
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


  public void start(Canvas canvas, GridCanvasController canvasController, ResourceBundle bundle) {
    canvas.setOnMouseClicked(event ->
        executor.execute(() -> showCorrectChart(event, canvasController, bundle)));
  }

  private void showCorrectChart(MouseEvent event, GridCanvasController canvasController,
                                ResourceBundle bundle) {
    GridView gridView = canvasController.getCurrentView().getGridView();
    double cellSize = canvasController.calculateCellSize(gridView);
    int col = (int) Math.floor(event.getX() / cellSize);
    int row = (int) Math.floor(event.getY() / cellSize);

    if (col < gridView.getWidth() && row < gridView.getHeight()) {
      updateChart(gridView, canvasController.getCurrentView().getHistory(), col, row, bundle);
    }
  }

  private void updateChart(GridView gridView, History history, int col, int row,
                           ResourceBundle bundle) {
    XYChart.Series<Double, Double> newSeries = prepareSeries(gridView, history, col, row, bundle);
    XYChart.Series<Double, Double> constLine = prepareConstLine(history.size(), bundle);

    Platform.runLater(() -> {
      cellChart.getData().clear();
      cellChart.getData().add(newSeries);
      cellChart.getData().add(constLine);
    });
  }

  private XYChart.Series<Double, Double> prepareSeries(GridView lastGridView, History history,
                                                       int col, int row, ResourceBundle bundle) {
    XYChart.Series<Double, Double> series = new XYChart.Series<>();
    series.setName(bundle.getString("massForCell") + " [" + row + "," + col + "]");

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

  private XYChart.Series<Double, Double> prepareConstLine(double size, ResourceBundle bundle) {
    XYChart.Series<Double, Double> constLine = new XYChart.Series<>();
    constLine.setName(bundle.getString("pollutionState"));

    constLine.getData().add(new XYChart.Data<>(0.0, 500.0));
    constLine.getData().add(new XYChart.Data<>(size, 500.0));
    return constLine;
  }
}
