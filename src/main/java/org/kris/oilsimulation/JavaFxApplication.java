package org.kris.oilsimulation;

import org.kris.oilsimulation.controller.RootController;
import org.kris.oilsimulation.controller.StartUpSettings;
import org.kris.oilsimulation.model.Model;
import org.kris.oilsimulation.model.ModelImpl;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {

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
    RootController rootController = rootLoader.getController();
    rootController.getGridCanvasController().initModel(model);
    rootController.getMenuController().getAutomatonStartController().initModel(model);
  }

  private void initStage(Stage stage, GridPane root) {
    stage.setTitle("Oil simulation");
    stage.setScene(new Scene(root));
    stage.setMinHeight(400);
    stage.setMinWidth(600);
    stage.show();
  }

  private void initModel(Model model) {
    model.init();
  }

}