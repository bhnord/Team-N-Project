package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.services.algo.Node;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.services.database.requests.Request;
import edu.wpi.teamname.services.database.requests.RequestType;
import edu.wpi.teamname.services.database.users.User;
import edu.wpi.teamname.services.database.users.UserType;
import edu.wpi.teamname.state.HomeState;
import edu.wpi.teamname.state.Login;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudioVisualRequestController extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private Label errorLabel;
  @FXML private JFXTextField txtTimeOfRequest;
  @FXML private JFXTextField txtEquipment;
  @FXML private JFXTextField txtComments;
  @FXML private JFXComboBox<Label> employeeDropdown;
  @FXML private JFXComboBox<Label> roomDropdown;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  private Scene appPrimaryScene;

  String helpPagePath = "AudioVisualRequestHelpPage";
  private HashMap<String, User> users;
  private HashMap<String, Node> rooms;
  JFXComboBox test = new JFXComboBox<>();

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

    //        txtEmployeeName.getValidators().add(reqInputValid);
    //        txtEmployeeName
    //            .focusedProperty()
    //            .addListener(
    //                (o, oldVal, newVal) -> {
    //                  if (!newVal) txtEmployeeName.validate();
    //                });
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

    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceServiceRequestPatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceServiceRequestEmployee(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceServiceRequestAdmin(loader, appPrimaryScene);
    }
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
    String title = "Help Page";
    JFXDialogLayout dialogContent = new JFXDialogLayout();
    dialogContent.setHeading(new Text(title));
    dialogContent.setBody(
        (new Text(
            "* Employee Name refers to the employee being requested to complete the job\n"
                + "* Patient Room is the room that the employee will deliver the medicine to\n"
                + "* Time of request refers to time the medicine should be delivered to the patient\n"
                + "* Necessary Equipment refers to additional services/equipment the patient requires\n"
                + "* Necessary Equipment refers to additional services/equipment the patient requires\n")));
    JFXButton close = new JFXButton("close");
    close.setButtonType(JFXButton.ButtonType.RAISED);
    close.setStyle("-fx-background-color : #00bfff:");
    dialogContent.setActions(close);

    JFXDialog dialog = new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    close.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dialog.close();
            helpButton.setDisable(false);
          }
        });
    helpButton.setDisable(true);
    dialog.show();
  }

  private void loadEmployeeDropdown() {
    users = db.getUsersByType(UserType.EMPLOYEE);
    for (User user : users.values()) {
      Label lbl = new Label(user.getUsername());
      lbl.setId(user.getId());
      employeeDropdown.getItems().add(lbl);
    }
    //    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    //    reqInputValid.setMessage("Cannot be empty");
    //    employeeDropdown.getValidators().add(reqInputValid);
    //    employeeDropdown
    //            .focusedProperty()
    //            .addListener(
    //                    (o, oldVal, newVal) -> {
    //                      if (employeeDropdown.getSelectionModel().isEmpty()) {
    //                        employeeDropdown.validate();
    //                      }
    //                    });
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
