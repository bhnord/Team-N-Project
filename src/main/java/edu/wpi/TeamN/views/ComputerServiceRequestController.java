package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComputerServiceRequestController extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private Label errorLabel;
  private Label person1;
  @FXML private JFXTextField txtTimeOfRequest;
  @FXML private JFXTextField txtEquipment;
  @FXML private JFXTextField txtComments;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  @FXML private Button submit;
  @FXML private StackPane myStackPane2;
  private Scene appPrimaryScene;
  private HashMap<String, User> users;
  private HashMap<String, Node> rooms;
  @FXML private JFXComboBox<Label> txtEmployeeName = new JFXComboBox<>();
  @FXML private JFXComboBox<Label> roomDropdown = new JFXComboBox<>();
  CurrentRequestController currentRequest = new CurrentRequestController();

  @FXML private AnchorPane anchorPage;

  @FXML private StackPane confirmationStackPane;
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
    // submit.setDisable(true);

    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    txtTimeOfRequest.getValidators().add(reqInputValid);
    txtTimeOfRequest
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtTimeOfRequest.validate();
            });
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

    if (login.getUsername().equals("patient") && login.getPassword().equals("patient")) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("staff") && login.getPassword().equals("staff")) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (login.getUsername().equals("admin") && login.getPassword().equals("admin")) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    } else if (login.getUsername().equals("guest") && login.getPassword().equals("guest")) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    }
  }

  @FXML
  public void back() throws IOException {

    Login login = Login.getLogin();

    if (login.getUsername().equals("patient") && login.getPassword().equals("patient")) {
      super.advanceServiceRequestPatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("staff") && login.getPassword().equals("staff")) {
      super.advanceServiceRequestEmployee(loader, appPrimaryScene);
    } else if (login.getUsername().equals("admin") && login.getPassword().equals("admin")) {
      super.advanceServiceRequestAdmin(loader, appPrimaryScene);
    }
  }

  public void Submit(ActionEvent actionEvent) throws IOException, InterruptedException {

    /*
        txtEmployeeName.setValidators();
        if (txtEmployeeName.getSelectionModel().isEmpty() || roomDropdown.getSelectionModel().isEmpty())
          return;
    */

    if (txtEmployeeName.getValue() == null
        || roomDropdown.getValue() == null
        || txtTimeOfRequest.getText().isEmpty()
        || txtEquipment.getText().isEmpty()) {
      String title = "Missing Fields";
      JFXDialogLayout dialogContent = new JFXDialogLayout();
      dialogContent.setHeading(new Text(title));
      dialogContent.setBody(
          (new Text("* You must fill out all required fields of the request to continue\n")));
      JFXButton close = new JFXButton("close");
      close.setButtonType(JFXButton.ButtonType.RAISED);
      close.setStyle("-fx-background-color : #00bfff;");
      dialogContent.setActions(close);

      JFXDialog dialog =
          new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
      actionEvent.consume();
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

    } else {

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
      actionEvent.consume();
      popup1.setAutoHide(false);

      cancelButton.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              popup1.hide();
              submit.setDisable(false);
            }
          });

      continueButton.setOnAction(
          new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
              popup1.hide();

              BoxBlur blur = new BoxBlur(7, 7, 7);

              VBox manuContainer = new VBox();
              Label lbl1 =
                  new Label(
                      "Your request Has been submitted!                                                          ");

              JFXButton continueButton = new JFXButton("Return To Home");
              continueButton.setButtonType(JFXButton.ButtonType.RAISED);
              continueButton.setStyle("-fx-background-color : #00bfff;");

              JFXButton cancelButton = new JFXButton("Complete Another Request");
              cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
              cancelButton.setStyle("-fx-background-color : #00bfff;");

              cancelButton.setTranslateX(0);
              cancelButton.setTranslateY(65);

              continueButton.setTranslateX(350);
              continueButton.setTranslateY(25);

              manuContainer.getChildren().addAll(lbl1, cancelButton, continueButton);
              manuContainer.setPadding(new Insets(30, 50, 50, 50));
              manuContainer.setSpacing(10);
              JFXPopup popup1 = new JFXPopup(manuContainer);
              actionEvent.consume();
              popup1.setAutoHide(false);

              // return to request page
              cancelButton.setOnAction(
                  new EventHandler<ActionEvent>() {
                    @SneakyThrows
                    @Override
                    public void handle(ActionEvent event) {
                      popup1.hide();
                      submit.setDisable(false);
                      back();
                    }
                  });

              // go back to home page
              continueButton.setOnAction(
                  new EventHandler<ActionEvent>() {
                    @SneakyThrows
                    @Override
                    public void handle(ActionEvent event) {
                      anchorPage.setEffect(null);
                      txtEmployeeName.setEffect(null);
                      popup1.hide();
                      advanceHome();
                      submit.setDisable(false);
                    }
                  });
              submit.setDisable(true);
              anchorPage.setEffect(blur);
              txtEmployeeName.setEffect(blur);
              popup1.show(
                  confirmationStackPane,
                  JFXPopup.PopupVPosition.BOTTOM,
                  JFXPopup.PopupHPosition.LEFT);
              submit.setDisable(false);
            }
          });
      submit.setDisable(true);
      popup1.show(myStackPane2, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
    }
  }

  public void help(ActionEvent actionEvent) throws IOException, InterruptedException {
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
    close.setStyle("-fx-background-color : #00bfff;");
    dialogContent.setActions(close);

    JFXDialog dialog = new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    actionEvent.consume();
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
}
