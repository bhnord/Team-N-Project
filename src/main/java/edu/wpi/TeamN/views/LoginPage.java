package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.state.Login;
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
import javafx.scene.layout.AnchorPane;

public class LoginPage extends MasterController implements Initializable {

  @Inject FXMLLoader loader;
  private Scene appPrimaryScene;
  @Inject DatabaseService db;
  @Inject HomeState state;

  @FXML private JFXTextField usernameField;
  @FXML private JFXPasswordField passwordField;
  @FXML private JFXButton goToHomePage;
  @FXML private Label incorrectLogin;
  @FXML private AnchorPane anchorPane;
  private Login login;
  private String accountUsername = "";
  private String accountPassword = "";

  public void initialize(URL url, ResourceBundle rb) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Login");
    /** Locking submit button to start* */
    goToHomePage.setDisable(true);
    goToHomePage.setDefaultButton(true);

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

  @FXML
  private void continueToHomePage() throws IOException {
    if (db.login(usernameField.getText(), passwordField.getText())) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    } else {
      incorrectLogin.setText("INCORRECT USERNAME OR PASSWORD, TRY AGAIN");
      incorrectLogin.setAlignment(Pos.CENTER);
    }
  }

  @FXML
  public void register() throws IOException {
    super.register(loader, appPrimaryScene);
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
