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
import edu.wpi.TeamN.state.Login;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LanguageInterpretersRequestController extends masterController
    implements Initializable {

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
  private Scene appPrimaryScene;
  private HashMap<String, User> users;
  private HashMap<String, Node> rooms;

  @FXML private JFXButton submitButton;
  @FXML private JFXComboBox<Label> employeeDropdown = new JFXComboBox<>();
  @FXML private JFXComboBox<Label> patientRoomDropdown = new JFXComboBox<>();
  @FXML private JFXTimePicker timePicker;
  @FXML private JFXComboBox<Label> languageDropdown = new JFXComboBox<>();

  // @FXML private AnchorPane anchorPage;
  static Stage stage;

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
    submitButton.setDisable(true);

    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    reqInputValid.setMessage("Cannot be empty");
    employeeDropdown.getValidators().add(reqInputValid);
    employeeDropdown
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtComments.validate();
            });

    loadLanguagesDropdown();
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

    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    }
  }

  @FXML
  public void back() throws IOException {

    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceServiceRequestPatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceServiceRequestEmployee(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceServiceRequestAdmin(loader, appPrimaryScene);
    }
  }

  public void submit(ActionEvent actionEvent) throws IOException {

    employeeDropdown.setValidators();
    if (employeeDropdown.getSelectionModel().isEmpty()
        || patientRoomDropdown.getSelectionModel().isEmpty()) return;

    VBox manuContainer = new VBox();
    Label lbl1 = new Label("Are you sure the information you have provided is correct?");

    JFXButton continueButton = new JFXButton("Continue");
    continueButton.setButtonType(JFXButton.ButtonType.RAISED);
    continueButton.setStyle("-fx-background-color : #00bfff;");

    JFXButton cancelButton = new JFXButton("Cancel");
    cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
    cancelButton.setStyle("-fx-background-color : #00bfff;");

    cancelButton.setTranslateX(100);
    cancelButton.setTranslateY(65);

    continueButton.setTranslateX(200);
    continueButton.setTranslateY(25);

    manuContainer.getChildren().addAll(lbl1, cancelButton, continueButton);
    manuContainer.setPadding(new Insets(30, 50, 50, 50));
    manuContainer.setSpacing(10);
    JFXPopup popup1 = new JFXPopup(manuContainer);

    cancelButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            popup1.hide();
            submitButton.setDisable(false);
          }
        });

    continueButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @SneakyThrows
          @Override
          public void handle(ActionEvent event) {
            submitToDB();
            popup1.hide();
            Parent root =
                loader.load(getClass().getResourceAsStream("ConfirmationPageLanguage.fxml"));
            appPrimaryScene.setRoot(root);
            submitButton.setDisable(false);
          }
        });
    submitButton.setDisable(true);
    popup1.show(myStackPane2, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
  }

  public void help(ActionEvent actionEvent) throws IOException {
    String title = "Help Page";
    BoxBlur blur = new BoxBlur(3, 3, 3);
    JFXDialogLayout dialogContent = new JFXDialogLayout();
    dialogContent.setHeading(new Text(title));
    dialogContent.setBody(
        (new Text(
            "* Employee Name refers to the employee being requested to complete the job\n"
                + "* Patient Room is the room with the patient where the Translation is required\n"
                + "* Time of request refers to time at which the translation is needed\n"
                + "* Desired language refers to the language that needs to be translated\n")));
    JFXButton close = new JFXButton("close");
    close.setButtonType(JFXButton.ButtonType.RAISED);
    close.setStyle("-fx-background-color : #00bfff;");
    dialogContent.setActions(close);

    JFXDialog dialog = new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    close.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            // anchorPage.setEffect(null);
            dialog.close();
            helpButton.setDisable(false);
          }
        });
    helpButton.setDisable(true);
    dialog.show();
    // anchorPage.setEffect(blur);
  }

  private void loadEmployeeDropdown() {
    users = db.getUsersByType(UserType.EMPLOYEE);
    for (User user : users.values()) {
      Label lbl = new Label(user.getUsername());
      lbl.setId(user.getId());
      employeeDropdown.getItems().add((lbl));
    }
    new AutoCompleteComboBoxListener(employeeDropdown);
  }

  @FXML
  private void validateButton() {
    if (!employeeDropdown.getEditor().getText().isEmpty()
        && !patientRoomDropdown.getEditor().getText().isEmpty()
        && !languageDropdown.getEditor().getText().isEmpty()
        && !timePicker.getEditor().getText().isEmpty()) {
      submitButton.setDisable(false);
    } else {
      submitButton.setDisable(true);
    }
  }

  private void loadRoomDropdown() {
    rooms = db.getAllNodesMap();
    for (Node node : rooms.values()) {
      Label lbl = new Label(node.get_longName());
      lbl.setId(node.get_nodeID());
      patientRoomDropdown.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(patientRoomDropdown);
  }

  private void loadLanguagesDropdown() {
    String arr[] = {"ASL", "Spanish", "French", "Mandarin"};
    for (String s : arr) {
      Label lbl = new Label(s);
      lbl.setId(s);
      languageDropdown.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(languageDropdown);
  }

  private void submitToDB() {
    RequestType type = RequestType.LANGUAGE_INTERPRETER;
    int recieverID =
        Integer.parseInt(employeeDropdown.getSelectionModel().getSelectedItem().getId());
    String roomNodeId = patientRoomDropdown.getSelectionModel().getSelectedItem().getId();
    String content =
        "Requested Language: " + languageDropdown.getSelectionModel().getSelectedItem().getText();
    String notes = txtComments.getText();
    Request r = new Request(type, recieverID, roomNodeId, content, notes);
    db.addRequest(r);
  }
}
