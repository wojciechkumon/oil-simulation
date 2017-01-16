package org.kris.oilsimulation.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class MapGeneratorController {

  @FXML
  public Canvas canvas;

  public void loadGenerationMenu() throws Exception {
    new MapGeneratorView();
  }
}
