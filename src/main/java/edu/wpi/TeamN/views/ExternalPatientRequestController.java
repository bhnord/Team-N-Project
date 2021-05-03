package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.requests.Request;
import edu.wpi.TeamN.services.database.requests.RequestType;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.AddressAutoComplete;
import edu.wpi.TeamN.utilities.DialogFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class ExternalPatientRequestController extends MasterController implements Initializable {

  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
  @Inject private HomeState state;
  @FXML private Label errorLabel;
  @FXML private JFXTextField commentsBox;
  @FXML private JFXComboBox<String> addressBox;
  @FXML private JFXComboBox<String> transportTypeDropdown;
  @FXML private JFXComboBox<Label> employeeDropdown;
  @FXML private JFXComboBox<Label> patientRoomDropdown;
  @FXML private JFXTimePicker departureTIme;
  @FXML private StackPane rootStackPane;
  private DialogFactory dialogFactory;
  private Scene appPrimaryScene;
  @FXML private AnchorPane anchorPane;

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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Service Request");
    dialogFactory = new DialogFactory(rootStackPane);
    transportTypeDropdown.getItems().add("Ambulance");
    transportTypeDropdown.getItems().add("Helicopter");
    transportTypeDropdown.getItems().add("Plane");
    loadEmployeeDropdown(employeeDropdown);
    loadRoomDropdown(patientRoomDropdown);
    new AddressAutoComplete(addressBox);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void back() throws IOException {
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException {
    if (validateInputs()) {
      dialogFactory.creatDialogOkay(
          "Missing Fields", "You must fill out all required fields of the request to continue\n");
    } else {
      dialogFactory.creatDialogConfirmCancel(
          "Are you sure the information you have provided is correct?", "", mouse -> submitToDB());
    }
  }

  public void help(ActionEvent actionEvent) throws IOException {
    dialogFactory.creatDialogOkay(
        "Help",
        "- Transportation type refers to the kind of transportation a patient requests \n- Patient Room is the room with the patient where the transportation is required \n- Employee is the kind of employee needed \n- Destination is the location of where a patient needs to be taken \n- Time of departure refers to time at which the transportation is departing \n- Comments refers to any additional information needed");
  }

  private void submitToDB() {
    Request request =
        new Request(
            RequestType.EXTERNAL_PATIENT_TRANSPORTATION,
            Integer.parseInt(employeeDropdown.getSelectionModel().getSelectedItem().getId()),
            patientRoomDropdown.getSelectionModel().getSelectedItem().getId(),
            "Transportation Method: "
                + transportTypeDropdown.getSelectionModel().getSelectedItem()
                + " Destination: "
                + addressBox.getSelectionModel().getSelectedItem(),
            commentsBox.getText());
    db.addRequest(request);
  }

  private boolean validateInputs() {
    return (departureTIme.getEditor().getText().isEmpty()
        || transportTypeDropdown.getSelectionModel().isEmpty()
        || patientRoomDropdown.getEditor().getText().isEmpty()
        || employeeDropdown.getEditor().getText().isEmpty()
        || addressBox.getEditor().getText().isEmpty());
  }
}
