package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import edu.wpi.teamname.state.Login;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class LoginPage extends masterController implements Initializable {

  @Inject FXMLLoader loader;
  private Scene appPrimaryScene;
  @Inject DatabaseService db;
  @Inject HomeState state;

  @FXML private JFXTextField usernameField;
  @FXML private JFXPasswordField passwordField;
  @FXML private JFXButton goToHomePage;
  @FXML private Label incorrectLogin;

  private Login login;
  public String accountUsername = "";
  public String accountPassword = "";
  String patientUsername = "p";
  String patientPassword = "p";

  String employeeUsername = "e";
  String employeePassword = "e";

  String adminUsername = "a";
  String adminPassword = "a";

  /*  LoginPage() {
    this.account = "";
  }*/

  public void initialize(URL url, ResourceBundle rb) {
    /** Locking submit button to start* */
    goToHomePage.setDisable(true);

    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    usernameField.getValidators().add(reqInputValid);
    usernameField
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) usernameField.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    passwordField.getValidators().add(reqInputValid);
    passwordField
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) passwordField.validate();
            });
  }

  public String getUsername() {
    accountUsername = usernameField.getText();
    return usernameField.getText();
  }

  public String getPassword() {
    accountPassword = passwordField.getText();
    return passwordField.getText();
  }

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  public void accountType() throws IOException {
    /*   if (getUsername().equals(patientUsername) && getPassword().equals(patientPassword)) {
      account = "patient";
    }

    if (getUsername().equals(employeeUsername) && getPassword().equals(employeePassword)) {
      account = "employee";
    }

    if (getUsername().equals(adminUsername) && getPassword().equals(adminPassword)) {
      account = "admin";
    }
    return account;*/

    if (accountUsername.equals(patientUsername) && accountPassword.equals(patientPassword)) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (accountUsername.equals(employeeUsername)
        && accountPassword.equals(employeePassword)) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (accountUsername.equals(adminUsername) && accountPassword.equals(adminPassword)) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    } else super.advanceHomeAdmin(loader, appPrimaryScene);
  }

  @FXML
  private void continueToHomePage() throws IOException {
    if (getUsername().equals(patientUsername) && getPassword().equals(patientPassword)) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (getUsername().equals(employeeUsername) && getPassword().equals(employeePassword)) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (getUsername().equals(adminUsername) && getPassword().equals(adminPassword)) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    } else {
      incorrectLogin.setText("INCORRECT USERNAME OR PASSWORD, TRY AGAIN");
      incorrectLogin.setAlignment(Pos.CENTER);
    }
  }

  @FXML
  private void validateButton() {
    if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
      goToHomePage.setDisable(false);
    } else {
      goToHomePage.setDisable(true);
    }
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }
}
