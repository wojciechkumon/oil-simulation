package org.kris.oilsimulation.controller.mapgenerator;

import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.LandCellState;
import org.kris.oilsimulation.model.OilParticle;
import org.kris.oilsimulation.model.WaterCellState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class MapGeneratorController implements Initializable {
  private static final Logger LOG = LoggerFactory.getLogger(MapGeneratorController.class);
  private static final String ICON_PATH = "view/img/mapicon.png";
  private static final Color BACKGROUND_COLOR = Color.rgb(240, 240, 240);
  private static final Color WATER_COLOR = Color.rgb(67, 183, 222);
  private static final Color LAND_COLOR = Color.rgb(245, 189, 81);

  @FXML
  private Canvas canvas;
  @FXML
  private Button saveButton;
  @FXML
  private Button resetButton;
  @FXML
  private ToggleGroup cellClickType;
  @FXML
  private Slider mapSizeSlider;

  private CellState[][] cellStatesMatrix;
  private double cellSize;
  private boolean mousePressed;
  private double startMovePosX;
  private double startMovePosY;


  public static int getGeneratedMap(Window mainWindow) {
    try {
      ResourceBundle bundle = ResourceBundle.getBundle("i18n/lang");
      FXMLLoader fxmlLoader = new FXMLLoader(MapGeneratorController.class.getClassLoader()
          .getResource("view/fxml/mapGenerator.fxml"), bundle);
      Parent root = fxmlLoader.load();
      Stage stage = new Stage();
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initStyle(StageStyle.DECORATED);
      stage.setTitle(bundle.getString("startSettings"));
      stage.getIcons().add(new Image(MapGeneratorController.class.getClassLoader().getResourceAsStream(ICON_PATH)));
      stage.setScene(new Scene(root));
      stage.setResizable(false);
      stage.initOwner(mainWindow);
      stage.showAndWait();
    } catch (IOException e) {
      LOG.error("Error while creating map generator window", e);
    }
    return 666;
  }

  public void save() {
    System.out.println("save pressed");
  }

  public final void reset() {
    handleNewMapSize((int) mapSizeSlider.getValue());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    new CanvasDragger(this, canvas,
        (i, j) -> cellStatesMatrix[i][j] = chooseCorrectCell());

    mapSizeSlider.valueProperty().addListener(
        (observable, oldValue, newValue) -> handleNewMapSize(newValue.intValue()));
    reset();
  }

  private void handleNewMapSize(int newMapSize) {
    allocateNewCells(newMapSize);
    redraw(newMapSize);
  }

  private void allocateNewCells(int newMapSize) {
    this.cellStatesMatrix = new CellState[newMapSize][newMapSize];
    for (int i = 0; i < newMapSize; i++) {
      for (int j = 0; j < newMapSize; j++) {
        this.cellStatesMatrix[i][j] = WaterCellState.emptyCell();
      }
    }
  }

  void redraw() {
    redraw(cellStatesMatrix.length);
  }

  private void redraw(int mapSize) {
    this.cellSize = calculateCellSize(mapSize);

    GraphicsContext graphics = canvas.getGraphicsContext2D();
    clearCanvas(graphics);
    drawCells(graphics, mapSize, cellSize);
    drawGrid(graphics, mapSize, cellSize);
  }

  private void clearCanvas(GraphicsContext graphics) {
    graphics.setFill(BACKGROUND_COLOR);
    graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  private double calculateCellSize(int mapSize) {
    double cellMaxWidth = canvas.getWidth() / mapSize;
    double cellMaxHeight = canvas.getHeight() / mapSize;

    return cellMaxWidth <= cellMaxHeight ? cellMaxWidth : cellMaxHeight;
  }

  private void drawCells(GraphicsContext graphics, int mapSize, double cellSize) {
    for (int i = 0; i < mapSize; i++) {
      for (int j = 0; j < mapSize; j++) {
        drawCell(graphics, cellSize, i, j);
      }
    }
  }

  private void drawCell(GraphicsContext graphics, double cellSize, int i, int j) {
    graphics.setFill(getCellColor(cellStatesMatrix[i][j]));
    graphics.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
  }

  private void drawGrid(GraphicsContext graphics, int mapSize, double cellSize) {
    for (int i = 0; i < mapSize; i++) {
      for (int j = 0; j < mapSize; j++) {
        graphics.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
      }
    }
  }

  private Color getCellColor(CellState state) {
    if (state.isWater() && state.getOilParticles().isEmpty()) {
      return WATER_COLOR;
    } else if (!state.isWater()) {
      return LAND_COLOR;
    } else
      return Color.BLACK;
  }

  private CellState chooseCorrectCell() {
    if (cellClickType.getSelectedToggle().getUserData() == CellType.LAND) {
      return LandCellState.emptyCell();
    } else if (cellClickType.getSelectedToggle().getUserData() == CellType.WATER) {
      return WaterCellState.emptyCell();
    } else {
      OilParticle particle = new OilParticle(100, 100, 100, 100);
      return new WaterCellState(Collections.singletonList(particle));
    }
  }

  int getCellMatrixSize() {
    return cellStatesMatrix.length;
  }

  double getCellSize() {
    return cellSize;
  }
}
