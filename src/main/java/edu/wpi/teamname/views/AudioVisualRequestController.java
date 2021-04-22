package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import edu.wpi.teamname.state.Login;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudioVisualRequestController extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private Label errorLabel;
  @FXML private JFXTextField txtEmployeeName;
  @FXML private JFXTextField txtRoom;
  @FXML private JFXTextField txtTimeOfRequest;
  @FXML private JFXTextField txtEquipment;
  @FXML private JFXTextField txtComments;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  @FXML private Button submit;
  @FXML private StackPane myStackPane2;
  private Scene appPrimaryScene;
  static Stage stage;

  String helpPagePath = "AudioVisualRequestHelpPage";
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
    txtRoom.getValidators().add(reqInputValid);
    txtRoom
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtRoom.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    txtEquipment.getValidators().add(reqInputValid);
    txtEquipment
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtEquipment.validate();
            });
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
    // super.advanceServiceRequest(loader, appPrimaryScene);
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

  public void Submit(ActionEvent actionEvent) throws IOException {

    String title = "Confirm";
    JFXDialogLayout dialogContent = new JFXDialogLayout();
    dialogContent.setHeading(new Text(title));
    dialogContent.setBody((new Text("Are you sure the information you have entered is correct\n")));

    JFXButton close = new JFXButton("Cancel");
    close.setButtonType(JFXButton.ButtonType.RAISED);
    close.setStyle("-fx-background-color : #00bfff:");
    // close.setTranslateX(100);
    // close.setTranslateY(100);

    dialogContent.setActions(close);

    JFXButton continueButton = new JFXButton("Continue");
    continueButton.setButtonType(JFXButton.ButtonType.RAISED);
    continueButton.setStyle("-fx-background-color : #00bfff:");
    dialogContent.setActions(continueButton);

    JFXDialog dialog =
        new JFXDialog(myStackPane2, dialogContent, JFXDialog.DialogTransition.BOTTOM);

    close.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dialog.close();
            submit.setDisable(false);
          }
        });

    continueButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @SneakyThrows
          @Override
          public void handle(ActionEvent event) {
            // dialog.close();
            Parent root =
                loader.load(getClass().getResourceAsStream("ConfirmationPageAudioVisual.fxml"));
            appPrimaryScene.setRoot(root);
            submit.setDisable(false);
          }
        });

    submit.setDisable(true);
    dialog.show();
  }

  /*
      Request r =
          new Request(
              RequestType.AUDIO_VISUAL,
              2,
              txtRoom.getText(),
              txtEquipment.getText(),
              txtComments.getText());
      if (!db.addRequest(r)) {
        errorLabel.setText("Invalid Input(s)");
      }

  */

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
}
