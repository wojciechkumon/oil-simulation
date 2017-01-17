package org.kris.oilsimulation.controller.util;

import org.kris.oilsimulation.controller.mapgenerator.MapGeneratorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class WindowUtil {
  private static final Logger LOG = LoggerFactory.getLogger(MapGeneratorController.class);

  public static <T> T showWindowAndGetController(Window mainWindow, String fxmlPath, String title,
                                                 String icon) {
    return showWindowAndGetController(mainWindow, fxmlPath, title, icon, x -> {});
  }

  public static <T> T showWindowAndGetController(Window mainWindow, String fxmlPath, String title,
                                                 String icon, Consumer<FXMLLoader> adjustFxml) {
    try {
      ResourceBundle bundle = ResourceBundle.getBundle("i18n/lang");
      FXMLLoader fxmlLoader = new FXMLLoader(WindowUtil.class.getClassLoader()
          .getResource(fxmlPath), bundle);
      Parent root = fxmlLoader.load();
      adjustFxml.accept(fxmlLoader);
      Stage stage = new Stage();
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initStyle(StageStyle.DECORATED);
      stage.setTitle(bundle.getString(title));
      stage.getIcons().add(new Image(WindowUtil.class.getClassLoader().getResourceAsStream(icon)));
      stage.setScene(new Scene(root));
      stage.setResizable(false);
      stage.initOwner(mainWindow);
      stage.showAndWait();
      return fxmlLoader.getController();
    } catch (IOException e) {
      LOG.error("Error while creating map generator window", e);
      throw new RuntimeException(e);
    }
  }

}
