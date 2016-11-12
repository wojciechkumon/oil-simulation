package org.kris.oilsimulation;

import javafx.application.Application;


public class OilSimulation {

  public static void main(String[] args) {
    enableAsyncLogger();
    Application.launch(JavaFxApplication.class);
  }

  private static void enableAsyncLogger() {
    System.setProperty("Log4jContextSelector",
        "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
  }

}
