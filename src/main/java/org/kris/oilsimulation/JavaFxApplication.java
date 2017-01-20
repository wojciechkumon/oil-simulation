package org.kris.oilsimulation;

import org.kris.oilsimulation.controller.simulationmenu.CellChartController;
import org.kris.oilsimulation.controller.maingrid.CellTooltipController;
import org.kris.oilsimulation.controller.maingrid.GridCanvasController;
import org.kris.oilsimulation.controller.RootController;
import org.kris.oilsimulation.controller.StartUpSettings;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.ModelImpl;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {
  private static final String ICON_PATH = "view/img/icon.png";

  @Override
  public void start(Stage stage) throws Exception {
    Model model = new ModelImpl(StartUpSettings.getDefault());

    FXMLLoader rootLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/root.fxml"),
        ResourceBundle.getBundle("i18n/lang"));
    GridPane root = rootLoader.load();

    initControllers(model, rootLoader);
    initStage(stage, root);
    initModel(model);
  }

  private void initControllers(Model model, FXMLLoader rootLoader) {
    RootController root = rootLoader.getController();
    GridCanvasController canvas = root.getGridCanvasController();
    canvas.initModel(model);
    canvas.initCellTooltip(new CellTooltipController());
    CellChartController chart = root.getMenuController().getAutomatonStartController()
        .getCellChartController();
    canvas.initCellChartController(chart);
    root.getMenuController().getAutomatonStartController().initModel(model);
  }

  private void initStage(Stage stage, GridPane root) {
    Platform.setImplicitExit(false);
    stage.setOnCloseRequest(e -> Platform.exit());
    stage.setTitle("Oil simulation");
    stage.setScene(new Scene(root));
    stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(ICON_PATH)));
    stage.setMinHeight(550);
    stage.setMinWidth(1280);
    stage.show();
  }

  private void initModel(Model model) {
    model.init();
  }

}
