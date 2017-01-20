package org.kris.oilsimulation.controller.mapgenerator;


import org.kris.oilsimulation.controller.Colors;
import org.kris.oilsimulation.controller.StartUpSettings;
import org.kris.oilsimulation.controller.util.WindowUtil;
import org.kris.oilsimulation.model.CellCoords;
import org.kris.oilsimulation.model.CellState;
import org.kris.oilsimulation.model.InitialStates;
import org.kris.oilsimulation.model.LandCellState;
import org.kris.oilsimulation.model.OilParticle;
import org.kris.oilsimulation.model.OilSource;
import org.kris.oilsimulation.model.OilSourceImpl;
import org.kris.oilsimulation.model.Size;
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
import javafx.stage.Stage;
import javafx.stage.Window;

import static org.kris.oilsimulation.controller.Colors.BACKGROUND_COLOR;
import static org.kris.oilsimulation.controller.Colors.LAND_COLOR;
import static org.kris.oilsimulation.controller.Colors.WATER_COLOR;
import static org.kris.oilsimulation.model.CellCoords.newCellCoords;

public class MapGeneratorController implements Initializable {
  private static final String ICON_PATH = "view/img/mapicon.png";
  private Map<CellCoords, OilSource> oilSources = new HashMap<>();
  private CellState[][] cellStatesMatrix;
  private double cellSize;
  private boolean saved = false;

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


  public static GeneratedMap getGeneratedMap(Window mainWindow) {
    MapGeneratorController controller = WindowUtil
        .showWindowAndGetController(mainWindow, "view/fxml/mapGenerator.fxml", "setMap", ICON_PATH);
    if (controller.saved) {
      return controller.getGeneratedMap();
    }
    return null;
  }

  @FXML
  private void save() {
    saved = true;
    Stage stage = (Stage) canvas.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void reset() {
    int mapSize = (int) mapSizeSlider.getValue();
    this.cellStatesMatrix = allocateNewCells(mapSize);
    oilSources.clear();
    redraw(mapSize);
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
    CellState[][] newCellStatesMatrix = allocateNewCells(newMapSize);
    copyCurrentMatrixToNewAndSet(newCellStatesMatrix, newMapSize);
    filterOilSources(newMapSize);
    redraw(newMapSize);
  }

  private void copyCurrentMatrixToNewAndSet(CellState[][] newCellStatesMatrix, int newMapSize) {
    int smallerSize = newMapSize <= this.cellStatesMatrix.length ?
        newMapSize : this.cellStatesMatrix.length;
    for (int i = 0; i < smallerSize; i++) {
      System.arraycopy(this.cellStatesMatrix[i], 0, newCellStatesMatrix[i], 0, smallerSize);
    }
    this.cellStatesMatrix = newCellStatesMatrix;
  }

  private CellState[][] allocateNewCells(int newMapSize) {
    CellState[][] newCellStatesMatrix = new CellState[newMapSize][newMapSize];
    for (int i = 0; i < newMapSize; i++) {
      for (int j = 0; j < newMapSize; j++) {
        newCellStatesMatrix[i][j] = WaterCellState.emptyCell();
      }
    }
    return newCellStatesMatrix;
  }

  private void filterOilSources(int mapSize) {
    this.oilSources = oilSources.entrySet().stream()
        .filter(entry -> {
          CellCoords coords = entry.getKey();
          return coords.getCol() < mapSize
              && coords.getRow() < mapSize;
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
      graphics.fillRect(coords.getCol() * cellSize, coords.getRow() * cellSize,
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
    oilSources.remove(newCellCoords(j, i));
  }

  private void setOilSource(int i, int j) {
    OilParticle particle = StartUpSettings.getDefault()
        .getOilSimulationConstants()
        .getStartingParticle();

    int numberOfParticles = Integer.parseInt(particlesNumber.getText());
    int particlesPerStep = Integer.parseInt(particlesPerIteration.getText());

    OilSource oilSource = new OilSourceImpl(getParticlesList(particle, numberOfParticles),
        particlesPerStep);
    oilSources.put(newCellCoords(j, i), oilSource);
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

  private GeneratedMap getGeneratedMap() {
    Size size = new Size(cellStatesMatrix.length, cellStatesMatrix.length);
    InitialStates initialStates = new InitialStates(toMap(cellStatesMatrix), oilSources);
    return new GeneratedMap(size, initialStates);
  }

  private Map<CellCoords, ? extends CellState> toMap(CellState[][] cellStatesMatrix) {
    int capacity = cellStatesMatrix.length * cellStatesMatrix[0].length;
    Map<CellCoords, CellState> map = new HashMap<>(capacity);
    for (int i = 0; i < cellStatesMatrix.length; i++) {
      for (int j = 0; j < cellStatesMatrix[0].length; j++) {
        map.put(newCellCoords(j, i), cellStatesMatrix[i][j]);
      }
    }
    return map;
  }
}
