package org.kris.oilsmogsimulation;

import org.kris.oilsmogsimulation.view.JavaFxView;
import org.kris.oilsmogsimulation.view.View;


public class App {

  public static void main(String[] args) {
    View view = new JavaFxView();
    view.run();
  }

}
