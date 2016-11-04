package org.kris.oilsmogsimulation.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class JavaFxView extends Application implements View {

  @Override
  public void start(Stage primaryStage) {
    Button btn = new Button("OK");
    Scene scene = new Scene(btn, 200, 250);
    primaryStage.setTitle("OK");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void run() {
    new Thread(() -> Application.launch(getClass(), new String[0])).start();
  }

}
