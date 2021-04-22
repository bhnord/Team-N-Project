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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FoodDeliveryRequestController extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private JFXTextField txtEmployeeName;
  @FXML private JFXTextField txtStartRoom;
  @FXML private JFXButton submit;
  private Scene appPrimaryScene;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
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
    submit.setDisable(true);
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

  public void Submit(ActionEvent actionEvent) throws IOException {
    ConfirmBoxFood.confirm(this);
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

  public void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  @FXML
  private void validateButton() {
    if (!txtStartRoom.getText().isEmpty() && !txtEmployeeName.getText().isEmpty()) {
      submit.setDisable(false);
    } else {
      submit.setDisable(true);
    }
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
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

  public void back(ActionEvent actionEvent) throws IOException {
    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceServiceRequestPatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceServiceRequestEmployee(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceServiceRequestAdmin(loader, appPrimaryScene);
    }
  }
}