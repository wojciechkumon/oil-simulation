package org.kris.oilsimulation.controller.simulationmenu;

import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Slider;

public class WindCurrentSliders {
  private final Slider currentSliderX;
  private final Slider currentSliderY;
  private final Slider windSliderX;
  private final Slider windSliderY;

  public WindCurrentSliders(Slider currentSliderX, Slider currentSliderY,
                            Slider windSliderX, Slider windSliderY) {
    this.currentSliderX = currentSliderX;
    this.currentSliderY = currentSliderY;
    this.windSliderX = windSliderX;
    this.windSliderY = windSliderY;
  }

  public Slider currentX() {
    return currentSliderX;
  }

  public Slider currentY() {
    return currentSliderY;
  }

  public Slider windX() {
    return windSliderX;
  }

  public Slider windY() {
    return windSliderY;
  }

  public List<Slider> getSliders() {
    return Arrays.asList(currentSliderX, currentSliderY, windSliderX, windSliderY);
  }
}
