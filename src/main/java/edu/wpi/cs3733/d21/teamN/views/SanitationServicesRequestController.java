package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.requests.Request;
import edu.wpi.cs3733.d21.teamN.services.database.requests.RequestType;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import edu.wpi.cs3733.d21.teamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SanitationServicesRequestController extends MasterController implements Initializable {

  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
  @Inject private HomeState state;
  @FXML private Label errorLabel;
  @FXML private JFXTextField txtComments;
  @FXML private JFXComboBox<Label> txtEmployeeName;
  @FXML private JFXComboBox<Label> roomDropdown;
  @FXML private JFXTimePicker timePicker;
  @FXML private JFXTextField maintenanceRequest;
  @FXML private StackPane rootStackPane;
  @FXML private AnchorPane anchorPane;
  private DialogFactory dialogFactory;
  private Scene appPrimaryScene;

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
    loadEmployeeDropdown(txtEmployeeName);
    loadRoomDropdown(roomDropdown);
  }

  @FXML
  public void advanceHome() throws IOException {

    super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void back() throws IOException {

    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  public void submit(ActionEvent actionEvent) throws IOException {
    if (validateInputs()) {
      dialogFactory.creatDialogOkay(
          "Missing Fields", "You must fill out all required fields of the request to continue\n");
    } else {
      dialogFactory.creatDialogConfirmCancel(
          "Are you sure the information you have provided is correct?",
          "",
          mouse -> {
            try {
              submitToDB();
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    }
  }

  public void help(ActionEvent actionEvent) throws IOException {
    dialogFactory.creatDialogOkay(
        "Help",
        "- Employee Name refers to the employee being requested to complete the job \n- Patient Room is the room with the patient where the sanitation is required \n- Time of request refers to time at which the sanitation is needed \n- Sanitary details refers to the necessary sanitary information that is required \n- Comments refers to any additional information needed");
  }

  private void submitToDB() throws IOException {
    advanceHome();
    RequestType type = RequestType.SANITATION;
    int receiverID =
        Integer.parseInt(txtEmployeeName.getSelectionModel().getSelectedItem().getId());
    String roomNodeId = roomDropdown.getSelectionModel().getSelectedItem().getId();
    String content = "sanitation details: " + maintenanceRequest.getText();
    String notes = txtComments.getText();
    Request r = new Request(type, receiverID, roomNodeId, content, notes);
    db.addRequest(r);
  }

  private boolean validateInputs() {
    return (timePicker.getEditor().getText().isEmpty()
        || maintenanceRequest.getText().isEmpty()
        || txtEmployeeName.getEditor().getText().isEmpty()
        || roomDropdown.getEditor().getText().isEmpty());
  }
}
