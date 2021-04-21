package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.services.algo.Node;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.services.database.requests.Request;
import edu.wpi.teamname.services.database.requests.RequestType;
import edu.wpi.teamname.services.database.users.User;
import edu.wpi.teamname.services.database.users.UserType;
import edu.wpi.teamname.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
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
public class AudioVisualRequestController extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  Stage primaryStage;
  String helpPagePath = "AudioVisualRequestHelpPage";
  @FXML private Label text;
  @FXML private Label errorLabel;
  @FXML private JFXTextField txtTimeOfRequest;
  @FXML private JFXTextField txtEquipment;
  @FXML private JFXTextField txtComments;
  @FXML private JFXComboBox<Label> employeeDropdown;
  @FXML private JFXComboBox<Label> roomDropdown;
  private Scene appPrimaryScene;
  private HashMap<String, User> users;
  private HashMap<String, Node> rooms;

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
    //    log.debug(state.toString());
    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    employeeDropdown.getValidators().add(reqInputValid);
    employeeDropdown
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (employeeDropdown.getSelectionModel().isEmpty()) {
                employeeDropdown.validate();
              }
            });
    //        txtEmployeeName.getValidators().add(reqInputValid);
    //        txtEmployeeName
    //            .focusedProperty()
    //            .addListener(
    //                (o, oldVal, newVal) -> {
    //                  if (!newVal) txtEmployeeName.validate();
    //                });
    reqInputValid.setMessage("Cannot be empty");
    //    txtRoom.getValidators().add(reqInputValid);
    //    txtRoom
    //        .focusedProperty()
    //        .addListener(
    //            (o, oldVal, newVal) -> {
    //              if (!newVal) txtRoom.validate();
    //            });
    reqInputValid.setMessage("Cannot be empty");
    txtEquipment.getValidators().add(reqInputValid);
    txtEquipment
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtEquipment.validate();
            });

    loadEmployeeDropdown();
    loadRoomDropdown();
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException {
    employeeDropdown.setValidators();
    if (employeeDropdown.getSelectionModel().isEmpty()
        || roomDropdown.getSelectionModel().isEmpty()) return;
    ConfirmBoxAudioVisual.confirm(this);

    Request r =
        new Request(
            RequestType.AUDIO_VISUAL,
            Integer.parseInt(employeeDropdown.getSelectionModel().getSelectedItem().getId()),
            roomDropdown.getSelectionModel().getSelectedItem().getId(),
            txtEquipment.getText(),
            txtComments.getText());
    if (!db.addRequest(r)) {
      errorLabel.setText("Invalid Input(s)");
    }
  }

  public void help(ActionEvent actionEvent) throws IOException {
    super.returnToRequest(loader, appPrimaryScene, helpPagePath);
  }

  private void loadEmployeeDropdown() {
    users = db.getUsersByType(UserType.EMPLOYEE);
    for (User user : users.values()) {
      Label lbl = new Label(user.getUsername());
      lbl.setId(user.getId());
      employeeDropdown.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(employeeDropdown);
  }

  private void loadRoomDropdown() {
    rooms = db.getAllNodesMap();
    for (Node node : rooms.values()) {
      Label lbl = new Label(node.get_longName());
      lbl.setId(node.get_nodeID());
      roomDropdown.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(roomDropdown);
  }
}
