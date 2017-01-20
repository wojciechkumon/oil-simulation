package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.automatonview.CellView;
import org.kris.oilsimulation.model.automatonview.GridView;

import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class CellTooltipController {
  private final Tooltip tooltip;
  private final PauseTransition tooltipHider;
  private int lastCol = -1;
  private int lastRow = -1;

  public CellTooltipController() {
    this.tooltip = new Tooltip();
    this.tooltip.setAutoHide(true);
    String tooltipStyle = "-fx-background-color: rgba(238,238,238,0.75);" +
        " -fx-text-fill: #222222;" +
        "-fx-border-color: #222222;" +
        "-fx-font-size: 14";
    this.tooltip.setStyle(tooltipStyle);
    tooltipHider = initDelayedTooltipHider();
  }

  private PauseTransition initDelayedTooltipHider() {
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    pause.setOnFinished(e -> tooltip.hide());
    return pause;
  }

  public void start(Canvas canvas, GridCanvasController canvasController, ResourceBundle bundle) {
    canvas.setOnMouseMoved(event -> refreshTooltip(canvas, canvasController, event, bundle));
    canvas.setOnMouseExited(event -> hideTooltipIfPossible(canvasController, event));
  }

  private void refreshTooltip(Canvas canvas, GridCanvasController canvasController,
                              MouseEvent event, ResourceBundle bundle) {
    GridView gridView = canvasController.getCurrentView().getGridView();
    double cellSize = canvasController.calculateCellSize(gridView);
    int col = (int) Math.floor(event.getX() / cellSize);
    int row = (int) Math.floor(event.getY() / cellSize);

    if (col < gridView.getWidth() && row < gridView.getHeight()) {
      if (lastCol != col || lastRow != row) {
        tooltip.setText(getTooltipText(gridView, col, row, bundle));
        tooltip.show(canvas, event.getScreenX() + 6, event.getScreenY() - tooltip.getHeight() + 6);
        tooltipHider.playFromStart();
      }
      lastCol = col;
      lastRow = row;
    } else {
      tooltip.hide();
    }
  }

  private String getTooltipText(GridView gridView, int col, int row, ResourceBundle bundle) {
    CellView cellView = gridView.getCellView(row, col);
    String landType;
    if (cellView.isWater()) {
      landType = cellView.getNumberOfParticles() > 0 ? bundle.getString("contaminated_water") : bundle.getString("clean_water");
    } else {
      landType = bundle.getString("land");
    }
    return bundle.getString("type") + ": " + landType + "\n"
        + bundle.getString("xCellPos") + ": " + (col + 1) + "\n"
        + bundle.getString("yCellPos") + ": " + (row + 1) + "\n"
        + bundle.getString("Mass") + ": " + roundToThirdPlace(cellView.getMass()) + "kg\n"
        + bundle.getString("Volume") + ": " + roundToThirdPlace(cellView.getVolume()) + "m^3\n"
        + bundle.getString("OilParticles") + ": " + cellView.getNumberOfParticles() + "\n";
  }

  private double roundToThirdPlace(double value) {
    return Math.round(value * 1_000) / 1_000.0;
  }

  private void hideTooltipIfPossible(GridCanvasController canvasController, MouseEvent event) {
    GridView gridView = canvasController.getCurrentView().getGridView();
    double cellSize = canvasController.calculateCellSize(gridView);
    if (event.getX() <= 0 || event.getX() >= cellSize * gridView.getWidth()
        || event.getY() <= 0 || event.getY() >= cellSize * gridView.getHeight()) {
      tooltip.hide();
    }
  }
}
