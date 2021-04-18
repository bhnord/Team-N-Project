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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityServicesRequest extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private JFXTextField txtEmployeeName;
  @FXML private JFXTextField txtStartRoom;
  @FXML private JFXTextField txtEndRoom;
  private Scene appPrimaryScene;
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
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException {
    ConfirmBoxSecurity.confirm(this);
  }

  public void help(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("SecurityRequestHelpPage.fxml"));
    appPrimaryScene.setRoot(root);
    primaryStage.setScene(appPrimaryScene);
    primaryStage.setAlwaysOnTop(true);
    primaryStage.show();
  }
}
