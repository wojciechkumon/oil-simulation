package org.kris.oilsimulation.controller.simulationmenu;

import org.kris.oilsimulation.controller.handler.Handler;
import org.kris.oilsimulation.controller.simulationmenu.SimulationSpeed;
import org.kris.oilsimulation.controller.simulationmenu.WindCurrentSliders;
import org.kris.oilsimulation.model.OilAutomatonNextSettings;
import org.kris.oilsimulation.model.Vector;

import javafx.scene.control.ToggleGroup;

public class ViewData {
  private final ToggleGroup iterationDelayMillis;
  private final Handler onClearHandler;

  private OilAutomatonNextSettings currentSettings;

  public ViewData(ToggleGroup iterationDelayMillis, WindCurrentSliders sliders) {
    this.iterationDelayMillis = iterationDelayMillis;
    this.currentSettings = newSettings(sliders.currentX().getValue(), sliders.currentY().getValue(),
        sliders.windX().getValue(), sliders.windY().getValue());

    sliders.currentX().valueProperty().addListener((observable, old, newXValue) ->
        currentSettings = newSettings(newXValue.doubleValue(), sliders.currentY().getValue(),
            sliders.windX().getValue(), sliders.windY().getValue()));

    sliders.currentY().valueProperty().addListener((observable, old, newYValue) ->
        currentSettings = newSettings(sliders.currentX().getValue(), newYValue.doubleValue(),
            sliders.windX().getValue(), sliders.windY().getValue()));

    sliders.windX().valueProperty().addListener((observable, old, newXValue) ->
        currentSettings = newSettings(sliders.currentX().getValue(), sliders.currentY().getValue(),
            newXValue.doubleValue(), sliders.windY().getValue()));

    sliders.windY().valueProperty().addListener((observable, old, newYValue) ->
        currentSettings = newSettings(sliders.currentX().getValue(), sliders.currentY().getValue(),
            sliders.windX().getValue(), newYValue.doubleValue()));

    this.onClearHandler = () -> sliders.getSliders().forEach(slider -> slider.setValue(0));
  }

  private OilAutomatonNextSettings newSettings(double currentXValue, double currentYValue,
                                               double windXValue, double windYValue) {
    return new OilAutomatonNextSettings(new Vector(currentXValue, currentYValue),
        new Vector(windXValue, windYValue));
  }

  public OilAutomatonNextSettings getOilAutomatonNextSettings() {
    return currentSettings;
  }

  public int getIterationDelayMillis() {
    SimulationSpeed simulationSpeed =
        (SimulationSpeed) iterationDelayMillis.getSelectedToggle().getUserData();
    return simulationSpeed.getIterationDelayMillis();
  }

  public Handler onClearHandler() {
    return onClearHandler;
  }
}
