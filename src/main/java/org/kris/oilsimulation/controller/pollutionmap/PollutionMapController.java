package org.kris.oilsimulation.controller.pollutionmap;

import org.kris.oilsimulation.controller.util.WindowUtil;
import org.kris.oilsimulation.model.automatonview.AutomatonView;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.stage.Window;

public class PollutionMapController {
  private static final String ICON_PATH = "view/img/icon.png";

  @FXML
  private Canvas canvas;

  private AutomatonView automatonView;


  public static void showPollutionMap(Window mainWindow, AutomatonView automatonView) {
    Consumer<FXMLLoader> adjustFxml = fxmlLoader -> {
      PollutionMapController controller = fxmlLoader.getController();
      controller.setAutomatonView(automatonView);
    };

    WindowUtil.showWindowAndGetController(mainWindow, "view/fxml/pollutionMap.fxml",
        "pollutionMap", ICON_PATH, adjustFxml);
  }

  private void setAutomatonView(AutomatonView automatonView) {
    this.automatonView = automatonView;
    drawPollutionMap();
  }

  private void drawPollutionMap() {

  }
}
