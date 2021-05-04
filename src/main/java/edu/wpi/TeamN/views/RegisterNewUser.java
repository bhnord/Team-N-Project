package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.UserPrefs;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterNewUser extends MasterController implements Initializable {

  @FXML private GridPane rootGridPane;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private Label errorLabel;
  private Label Employee;
  private Label Patient;
  private Label Admin;
  @FXML private JFXTextField username;
  @FXML private JFXPasswordField password;
  @FXML private JFXPasswordField retypePassword;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  @FXML private Button submit;
  @FXML private StackPane myStackPane2;
  private Scene appPrimaryScene;

  @FXML private AnchorPane anchorPage;
  @FXML private AnchorPane anchorPane1;
  private DialogFactory dialogFactory;

  /**
   * This method allows the tests to inject the scene at a later time, since it must be done on the
   * JavaFX thread
   *
   * @param appPrimaryScene Primary scene of the app whose root will be changed
   */
  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
    super.sideBarSetup(anchorPane1, appPrimaryScene, loader, "Register");
    dialogFactory = new DialogFactory(myStackPane);
  }

  public void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  public void advanceHome() throws IOException {
    advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void back() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException, InterruptedException {

    if (validateInputs()) {
      dialogFactory.creatDialogOkay(
          "Missing Fields", "* You must fill out all required fields of the request to continue\n");
    } else if (!(password.getText().equals(retypePassword.getText()))) {
      dialogFactory.creatDialogOkay(
          "Invalid Entry", "* Passwords do not match. Retype passwords. \n");
    } else if (!(db.addUser(
        username.getText(), password.getText(), UserType.PATIENT, new UserPrefs()))) {
      dialogFactory.creatDialogOkay(
          "Invalid User Login", "* Username already exists. Choose different username.\n");
    } else {
      dialogFactory.creatDialogOkay(
          "Success!", "The account for \"" + username + "\" has been created.");
    }
  }

  public void help(ActionEvent actionEvent) throws IOException, InterruptedException {
    dialogFactory.creatDialogOkay(
        "Help Page",
        "* 'Select Login Type' refers to the type of user\n"
            + "* 'Enter Username' refers to the username you will use everytime you login to the application\n"
            + "* 'Enter Password' refers to your unique password that you will use everytime you login to the application\n"
            + "* 'Retype Password' helps authenticate your password to ensure a secure account\n");
  }

  private boolean validateInputs() {
    return (username.getText().isEmpty()
        || password.getText().isEmpty()
        || retypePassword.getText().isEmpty());
  }
}
