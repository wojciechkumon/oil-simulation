package org.kris.oilsimulation.controller;

import javafx.fxml.FXML;

public class RootController {

  @FXML
  private GridCanvasController gridCanvasController;

  @FXML
  private MenuController menuController;

  public GridCanvasController getGridCanvasController() {
    return gridCanvasController;
  }

  public MenuController getMenuController() {
    return menuController;
  }

}
