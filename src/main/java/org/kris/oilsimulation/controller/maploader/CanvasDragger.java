package org.kris.oilsimulation.controller.maploader;

import java.util.function.BiConsumer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CanvasDragger {
  private final MapLoaderController mapLoaderController;
  private final GraphicsContext graphics;
  private final BiConsumer<Integer, Integer> onSelectedCellIndexes;

  private boolean mousePressed;
  private double startMovePosX;
  private double startMovePosY;

  public CanvasDragger(MapLoaderController mapLoaderController,
                       Canvas canvas, BiConsumer<Integer, Integer> onSelectedCellIndexes) {
    this.mapLoaderController = mapLoaderController;
    this.graphics = canvas.getGraphicsContext2D();
    this.onSelectedCellIndexes = onSelectedCellIndexes;
    canvas.setOnMousePressed(this::onMousePressed);
    canvas.setOnMouseDragged(this::onMouseDrag);
    canvas.setOnMouseReleased(this::onMouseRelease);
  }

  private void onMousePressed(MouseEvent event) {
    this.mousePressed = true;
    this.startMovePosX = event.getX();
    this.startMovePosY = event.getY();
  }

  private void onMouseDrag(MouseEvent event) {
    if (!mousePressed) {
      return;
    }
    Coords coords = Coords.ascendingCoords(event.getX(), startMovePosX);
    if (coords.second > getMatrixSize() * getCellSize()) {
      coords.second = getMatrixSize() * getCellSize();
    }
    double xSize = coords.second - coords.first;
    double xStart = coords.first;

    coords = Coords.ascendingCoords(event.getY(), startMovePosY);
    if (coords.second > getMatrixSize() * getCellSize()) {
      coords.second = getMatrixSize() * getCellSize();
    }
    double ySize = coords.second - coords.first;
    redraw();
    graphics.setStroke(Color.BLACK);
    graphics.strokeRect(xStart, coords.first, xSize, ySize);
  }

  private void onMouseRelease(MouseEvent event) {
    Coords coordsX = Coords.ascendingCoords(event.getX(), startMovePosX);
    Coords coordsY = Coords.ascendingCoords(event.getY(), startMovePosY);

    coordsX.first = findPosition(coordsX.first);
    coordsX.second = findPosition(coordsX.second);
    coordsY.first = findPosition(coordsY.first);
    coordsY.second = findPosition(coordsY.second);

    for (int i = (int) coordsX.first; i <= (int) coordsX.second; i++) {
      for (int j = (int) coordsY.first; j <= (int) coordsY.second; j++) {
        if (checkCell(i, j)) {
          onSelectedCellIndexes.accept(i, j);
        }
      }
    }
    this.mousePressed = false;
    redraw();
  }

  private double findPosition(double pixel) {
    return pixel / getCellSize();
  }

  private boolean checkCell(int x, int y) {
    return x >= 0 && x < getMatrixSize()
        && y >= 0 && y < getMatrixSize();
  }

  private int getMatrixSize() {
    return mapLoaderController.getCellMatrixSize();
  }

  private double getCellSize() {
    return mapLoaderController.getCellSize();
  }

  private void redraw() {
    mapLoaderController.redraw();
  }

  private static class Coords {
    private double first;
    private double second;

    private Coords(double first, double second) {
      this.first = first;
      this.second = second;
    }

    static Coords ascendingCoords(double first, double second) {
      if (first <= second) {
        return new Coords(first, second);
      }
      return new Coords(second, first);
    }
  }
}
