package org.kris.oilsimulation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

public class MenuController {

  @FXML
  private PasswordField passwordField;

  @FXML
  private Text actionTarget;

  @FXML
  private AutomatonStartController automatonStartController;

  public void handleSubmitButtonAction() {
    actionTarget.setText("PASS: " + passwordField.getText());
  }

  public AutomatonStartController getAutomatonStartController() {
    return automatonStartController;
  }

}
