package org.kris.oilsimulation.controller.maploader;


import org.kris.oilsimulation.controller.Colors;
import org.kris.oilsimulation.controller.StartUpSettings;
import org.kris.oilsimulation.controller.util.Mapper;
import org.kris.oilsimulation.controller.util.WindowUtil;
import org.kris.oilsimulation.model.InitialStates;
import org.kris.oilsimulation.model.Size;
import org.kris.oilsimulation.model.cell.CellCoords;
import org.kris.oilsimulation.model.cell.CellState;
import org.kris.oilsimulation.model.cell.LandCellState;
import org.kris.oilsimulation.model.cell.OilParticle;
import org.kris.oilsimulation.model.cell.OilSource;
import org.kris.oilsimulation.model.cell.OilSourceImpl;
import org.kris.oilsimulation.model.cell.WaterCellState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import static org.kris.oilsimulation.controller.Colors.BACKGROUND_COLOR;
import static org.kris.oilsimulation.controller.Colors.LAND_COLOR;
import static org.kris.oilsimulation.controller.Colors.WATER_COLOR;
import static org.kris.oilsimulation.model.cell.CellCoords.newCellCoords;

public class MapLoaderController implements Initializable {
  private static final Logger LOG = LoggerFactory.getLogger(MapLoaderController.class);
  private static final String ICON_PATH = "view/img/mapicon.png";
  private static final String MAP_EXTENSION = "map";
  private static final String EXTENSION_DESCRIPTION = "Oil simulation map";

  private final FileChooser fileChooser = new FileChooser();
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
  private ResourceBundle resources;


  public static Optional<LoadedMap> getLoadedMap(Window mainWindow) {
    MapLoaderController controller = WindowUtil
        .showWindowAndGetController(mainWindow, "view/fxml/mapLoader.fxml", "setMap", ICON_PATH);
    if (controller.saved) {
      return Optional.of(controller.getLoadedMap());
    }
    return Optional.empty();
  }

  @FXML
  private void save() {
    saved = true;
    closeWindow();
  }

  @FXML
  private void reset() {
    int mapSize = (int) mapSizeSlider.getValue();
    this.cellStatesMatrix = allocateNewCells(mapSize);
    oilSources.clear();
    redraw(mapSize);
  }

  @FXML
  private void cancel() {
    closeWindow();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.resources = resources;
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

  private LoadedMap getLoadedMap() {
    Size size = new Size(cellStatesMatrix.length, cellStatesMatrix.length);
    InitialStates initialStates = new InitialStates(Mapper.toMap(cellStatesMatrix), oilSources);
    return new LoadedMap(size, initialStates);
  }

  private void closeWindow() {
    getStage().close();
  }

  private Stage getStage() {
    return (Stage) canvas.getScene().getWindow();
  }

  @FXML
  private void saveToFile() {
    configureFileChooser(fileChooser);
    fileChooser.setTitle(resources.getString("mapSaveDialogTitle"));
    fileChooser.setInitialFileName("mapName." + MAP_EXTENSION);

    Optional.ofNullable(fileChooser.showSaveDialog(getStage()))
        .ifPresent(this::saveMap);
  }

  private void saveMap(File file) {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(getLoadedMap());
    } catch (IOException e) {
      LOG.error("error while save map to file", e);
    }
  }

  @FXML
  private void loadFromFile() {
    configureFileChooser(fileChooser);
    fileChooser.setTitle(resources.getString("mapOpenDialogTitle"));

    Optional.ofNullable(fileChooser.showOpenDialog(getStage()))
        .ifPresent(this::loadFile);
  }

  private void loadFile(File file) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
      LoadedMap loadedMap = (LoadedMap) in.readObject();
      setLoadedMap(loadedMap);
    } catch (IOException | ClassNotFoundException e) {
      LOG.error("error while save map to file", e);
    }
  }

  private void setLoadedMap(LoadedMap loadedMap) {
    this.cellStatesMatrix = Mapper.mapToCellStates(loadedMap);
    this.oilSources = loadedMap.getInitialStates().getInitialSources();

    redraw();
    mapSizeSlider.setValue(cellStatesMatrix.length);
  }

  private static void configureFileChooser(FileChooser fileChooser) {
    fileChooser.setSelectedExtensionFilter(
        new FileChooser.ExtensionFilter(EXTENSION_DESCRIPTION, MAP_EXTENSION));
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
  }
}
