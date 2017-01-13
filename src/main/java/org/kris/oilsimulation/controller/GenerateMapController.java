package org.kris.oilsimulation.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import org.kris.oilsimulation.model.automatonview.GenerateMapView;

public class GenerateMapController {

    @FXML
    private GridCanvasController gridCanvasController;

    public GridCanvasController getGridCanvasController() {
        return gridCanvasController;
    }
        public GenerateMapController(){
    }

    public void LoadGenerationMenu() throws Exception{
        new GenerateMapView();
    }
}
