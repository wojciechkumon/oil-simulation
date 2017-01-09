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
    tooltipHider = initDelayedTooltipHider();
  }

  private PauseTransition initDelayedTooltipHider() {
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
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
        tooltip.show(canvas, event.getScreenX(), event.getScreenY());
        tooltipHider.playFromStart();
      }
      lastCol = col;
      lastRow = row;
    }
  }

  private String getTooltipText(GridView gridView, int col, int row, ResourceBundle bundle) {
    CellView cellView = gridView.getState(row, col);

    return bundle.getString("Mass") + ": " + cellView.getMass() + "kg\n"
        + bundle.getString("Volume") + ": " + roundToThirdPlace(cellView.getVolume()) + "m^3\n"
        + bundle.getString("Oil_particles") + ": " + cellView.getNumberOfParticles();
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
