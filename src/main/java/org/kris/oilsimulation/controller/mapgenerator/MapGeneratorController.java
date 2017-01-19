package org.kris.oilsimulation.controller.mapgenerator;


import org.kris.oilsimulation.controller.Colors;
import org.kris.oilsimulation.controller.StartUpSettings;
import org.kris.oilsimulation.controller.util.WindowUtil;
import org.kris.oilsimulation.model.CellCoords;
import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.LandCellState;
import org.kris.oilsimulation.model.OilParticle;
import org.kris.oilsimulation.model.OilSource;
import org.kris.oilsimulation.model.OilSourceImpl;
import org.kris.oilsimulation.model.WaterCellState;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;

import static org.kris.oilsimulation.controller.Colors.BACKGROUND_COLOR;
import static org.kris.oilsimulation.controller.Colors.LAND_COLOR;
import static org.kris.oilsimulation.controller.Colors.WATER_COLOR;
import static org.kris.oilsimulation.model.CellCoords.newCellCoords;

public class MapGeneratorController implements Initializable {
  private static final String ICON_PATH = "view/img/mapicon.png";
  private final Map<CellCoords, OilSource> oilSources = new HashMap<>();
  private CellState[][] cellStatesMatrix;
  private double cellSize;

  @FXML
  private Canvas canvas;
  @FXML
  private ToggleGroup cellClickType;
  @FXML
  private Slider mapSizeSlider;
  @FXML
  private TextField particlesNumber;
  @FXML
  private TextField particlesPerIteration;
  @FXML
  private HBox oilSourcesFields;


  public static int getGeneratedMap(Window mainWindow) {
    MapGeneratorController controller = WindowUtil
        .showWindowAndGetController(mainWindow, "view/fxml/mapGenerator.fxml", "startSettings", ICON_PATH);
    return controller.getCellMatrixSize();
  }

  public void save() {
    System.out.println("save pressed");
  }

  public final void reset() {
    handleNewMapSize((int) mapSizeSlider.getValue());
    oilSources.clear();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    new CanvasDragger(this, canvas, this::setCorrectCellState);

    mapSizeSlider.valueProperty().addListener(
        (observable, oldValue, newValue) -> handleNewMapSize(newValue.intValue()));
    reset();
    addTextFieldsIntegerInterceptors();
    setOilSourceFieldsHider();
  }

  private void addTextFieldsIntegerInterceptors() {
    addIntegerInterceptor(particlesNumber);
    addIntegerInterceptor(particlesPerIteration);
  }

  private void addIntegerInterceptor(TextField textField) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        textField.setText(newValue.replaceAll("[^\\d]", ""));
      }
    });
  }

  private void setOilSourceFieldsHider() {
    cellClickType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.getUserData() == CellType.OIL_SOURCE) {
        oilSourcesFields.setVisible(true);
      } else {
        oilSourcesFields.setVisible(false);
      }
    });
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
    drawOilSources(graphics, cellSize);
  }

  private void drawOilSources(GraphicsContext graphics, double cellSize) {
    graphics.setFill(Colors.OIL_SOURCES_COLOR);
    oilSources.entrySet().forEach(entry -> {
      CellCoords coords = entry.getKey();
      graphics.fillRect(coords.getRow() * cellSize, coords.getCol() * cellSize,
          cellSize, cellSize);
    });
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

  private void setCorrectCellState(int i, int j) {
    if (cellClickType.getSelectedToggle().getUserData() == CellType.LAND) {
      setCellState(i, j, LandCellState.emptyCell());
    } else if (cellClickType.getSelectedToggle().getUserData() == CellType.WATER) {
      setCellState(i, j, WaterCellState.emptyCell());
    } else {
      setOilSource(i, j);
    }
  }

  private void setCellState(int i, int j, CellState cellState) {
    cellStatesMatrix[i][j] = cellState;
    oilSources.remove(newCellCoords(i, j));
  }

  private void setOilSource(int i, int j) {
    OilParticle particle = StartUpSettings.getDefault()
        .getOilSimulationConstants()
        .getStartingParticle();

    int numberOfParticles = Integer.parseInt(particlesNumber.getText());
    int particlesPerStep = Integer.parseInt(particlesPerIteration.getText());

    OilSource oilSource = new OilSourceImpl(getParticlesList(particle, numberOfParticles),
        particlesPerStep);
    oilSources.put(newCellCoords(i, j), oilSource);
    cellStatesMatrix[i][j] = WaterCellState.emptyCell();
  }

  private List<OilParticle> getParticlesList(OilParticle particle, int numberOfParticles) {
    return Stream
        .generate(() -> particle)
        .limit(numberOfParticles)
        .collect(Collectors.toList());
  }

  int getCellMatrixSize() {
    return cellStatesMatrix.length;
  }

  double getCellSize() {
    return cellSize;
  }
}
