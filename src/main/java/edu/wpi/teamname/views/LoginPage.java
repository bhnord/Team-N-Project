package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.ServiceTwo;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import edu.wpi.teamname.state.Login;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginPage extends masterController implements Initializable {

  @Inject FXMLLoader loader;
  private Scene appPrimaryScene;
  @Inject DatabaseService db;
  @Inject ServiceTwo graph;
  @Inject HomeState state;

  @FXML private TextField usernameField;
  @FXML private TextField passwordField;
  @FXML private Button goToHomePage;

  private Login login;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @FXML
  private void continueToHomePage() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  private void validateButton() {
    if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
      goToHomePage.setDisable(false);
    }
  }
}
