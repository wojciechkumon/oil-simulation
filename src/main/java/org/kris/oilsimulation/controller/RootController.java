package org.kris.oilsimulation.controller;

import org.kris.oilsimulation.controller.maingrid.GridCanvasController;
import org.kris.oilsimulation.controller.simulationmenu.MenuController;

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
