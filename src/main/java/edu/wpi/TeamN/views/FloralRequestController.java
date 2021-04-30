package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.requests.Request;
import edu.wpi.TeamN.services.database.requests.RequestType;
import edu.wpi.TeamN.services.database.users.User;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.AutoCompleteComboBoxListener;
import edu.wpi.TeamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FloralRequestController extends MasterController implements Initializable {

  // @FXML private AnchorPane anchorPage;
  static Stage stage;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private Label errorLabel;
  private Label person1;
  @FXML private JFXTextField txtComments;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  @FXML private StackPane myStackPane2;
  @FXML private StackPane rootStackPane;
  private Scene appPrimaryScene;
  @FXML private Button submit;
  private HashMap<String, User> users;
  private HashMap<String, Node> rooms;
  @FXML private AnchorPane anchorPage;
  @FXML private StackPane confirmationStackPane;
  // @FXML private JFXButton submitButton;
  @FXML private JFXComboBox<Label> txtEmployeeName = new JFXComboBox<>();
  @FXML private JFXComboBox<Label> roomDropdown = new JFXComboBox<>();
  @FXML private JFXTimePicker timePicker;
  @FXML private JFXTextField bouquet;
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
    //  submitButton.setDisable(true);
    dialogFactory = new DialogFactory(rootStackPane);
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

    loadEmployeeDropdown();
    loadRoomDropdown();
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
          "Are you sure the information you have provided is correct?", "", mouse -> submitToDB());
    }
  }

  public void help(ActionEvent actionEvent) throws IOException {
    dialogFactory.creatDialogOkay(
        "Help",
        "- Employee Name refers to the employee being requested to complete the job \n- Patient Room is the room with the patient where the Translation is required \n- Time of request refers to time at which the translation is needed \n- Desired language refers to the language that needs to be translated");
  }

  private void loadEmployeeDropdown() {
    users = db.getUsersByType(UserType.EMPLOYEE);
    for (User user : users.values()) {
      Label lbl = new Label(user.getUsername());
      lbl.setId(user.getId());
      txtEmployeeName.getItems().add((lbl));
    }
    new AutoCompleteComboBoxListener(txtEmployeeName);
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

  private void submitToDB() {
    RequestType type = RequestType.FLORAL;
    int recieverID =
        Integer.parseInt(txtEmployeeName.getSelectionModel().getSelectedItem().getId());
    String roomNodeId = roomDropdown.getSelectionModel().getSelectedItem().getId();
    String content = "Time of request: " + timePicker.getEditor().getText();
    String notes = "flower type: " + bouquet.getText() + " comments: " + txtComments.getText();
    Request r = new Request(type, recieverID, roomNodeId, content, notes);
    db.addRequest(r);
  }

  private boolean validateInputs() {
    return (timePicker.getEditor().getText().isEmpty()
        || bouquet.getText().isEmpty()
        || txtEmployeeName.getEditor().getText().isEmpty()
        || roomDropdown.getEditor().getText().isEmpty());
  }
}
