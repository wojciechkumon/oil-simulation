package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.automatonview.AutomatonView;

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
    AutomatonView automatonView = canvasController.getCurrentView();
    double cellSize = canvasController.calculateCellSize();
    int col = (int) Math.floor(event.getX() / cellSize);
    int row = (int) Math.floor(event.getY() / cellSize);

    if (col < automatonView.getWidth() && row < automatonView.getHeight()) {
      if (lastCol != col || lastRow != row) {
        tooltip.setText(getTooltipText(automatonView, col, row, bundle));
        tooltip.show(canvas, event.getScreenX(), event.getScreenY());
        tooltipHider.playFromStart();
      }
      lastCol = col;
      lastRow = row;
    }
  }

  private String getTooltipText(AutomatonView automatonView, int col, int row, ResourceBundle bundle) {
    CellState state = automatonView.getState(row, col);

    return bundle.getString("Mass") + ": " + state.getMass() + "kg\n"
        + bundle.getString("Volume") + ": " + roundToThirdPlace(state.getVolume()) + "m^3\n"
        + bundle.getString("Oil_particles") + ": " + state.getOilParticles().size();
  }

  private double roundToThirdPlace(double value) {
    return Math.round(value * 1_000) / 1_000.0;
  }

  private void hideTooltipIfPossible(GridCanvasController canvasController, MouseEvent event) {
    AutomatonView automatonView = canvasController.getCurrentView();
    double cellSize = canvasController.calculateCellSize();
    if (event.getX() <= 0 || event.getX() >= cellSize * automatonView.getWidth()
        || event.getY() <= 0 || event.getY() >= cellSize * automatonView.getHeight()) {
      tooltip.hide();
    }
  }
}
