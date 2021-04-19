package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LaundryRequestController extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private JFXTextField txtEmployeeName;
  @FXML private JFXTextField txtStartRoom;
  @FXML private JFXTextField txtEndRoom;
  private Scene appPrimaryScene;
  String helpPagePath = "LaundryRequestHelpPage";
  static LoginPage loginAccount = new LoginPage();
  Stage primaryStage;
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
    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    txtEmployeeName.getValidators().add(reqInputValid);
    txtEmployeeName
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtEmployeeName.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    txtStartRoom.getValidators().add(reqInputValid);
    txtStartRoom
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtStartRoom.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    txtEndRoom.getValidators().add(reqInputValid);
    txtEndRoom
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtEndRoom.validate();
            });
  }

  @FXML
  public void advanceHome() throws IOException {

    /*if (loginAccount.getUsername().equals("p") && loginAccount.getPassword().equals("p")) {
      // if (loginAccount.accountType().equals("patient"))
      super.advanceServiceRequestPatient(loader, appPrimaryScene);
    }
    if (loginAccount.getUsername().equals("e") && loginAccount.getPassword().equals("e")) {
      // if (loginAccount.accountType().equals("employee"))
      super.advanceServiceRequestEmployee(loader, appPrimaryScene);
    }
    if (loginAccount.getUsername().equals("a") && loginAccount.getPassword().equals("a")) {
      // if (loginAccount.accountType().equals("admin"))
      super.advanceServiceRequestAdmin(loader, appPrimaryScene);
    }
    if (loginAccount.getUsername().equals("") && loginAccount.getPassword().equals("")) {
      // if (loginAccount.accountType().equals("admin"))
      System.out.println("didn't work");
    }*/

    loginAccount.accountType();
  }

  public void Submit(ActionEvent actionEvent) throws IOException {
    ConfirmBoxLaundry.confirm(this);
  }

  public void help(ActionEvent actionEvent) throws IOException {
    super.returnToRequest(loader, appPrimaryScene, helpPagePath);
  }
}