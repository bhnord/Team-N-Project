package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.LatLng;
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
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalPatientRequestController extends MasterController implements Initializable {

  // @FXML private AnchorPane anchorPage;
  static Stage stage;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label errorLabel;
  @FXML private JFXTextField commentsBox;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  @FXML private Button submit;
  @FXML private StackPane myStackPane2;
  @FXML private JFXComboBox addressBox;
  @FXML private JFXComboBox transportTypeDropdown;
  @FXML private JFXComboBox<Label> employeeDropdown;
  @FXML private JFXComboBox<Label> patientRoomDropdown;
  @FXML private JFXTimePicker departureTIme;
  private Scene appPrimaryScene;
  @FXML private AnchorPane anchorPage;
  @FXML private StackPane confirmationStackPane;

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
    log.debug(state.toString());
    // submit.setDisable(true);
    transportTypeDropdown.getItems().add("Ambulance");
    transportTypeDropdown.getItems().add("Helicopter");
    transportTypeDropdown.getItems().add("Plane");
    loadEmployeeDropdown();
    loadRoomDropdown();
    loadMapsStuff();
    // setupValidation();
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

  public void Submit(ActionEvent actionEvent) throws IOException {

    if (departureTIme.getEditor().getText().isEmpty()
        || transportTypeDropdown.getSelectionModel().isEmpty()
        || patientRoomDropdown.getEditor().getText().isEmpty()
        || employeeDropdown.getEditor().getText().isEmpty()
        || addressBox.getEditor().getText().isEmpty()) {
      String title = "Missing Fields";
      JFXDialogLayout dialogContent = new JFXDialogLayout();
      dialogContent.setHeading(new Text(title));
      dialogContent.setBody(
          (new Text("* You must fill out all required fields of the request to continue\n")));
      JFXButton close = new JFXButton("close");
      close.setButtonType(JFXButton.ButtonType.RAISED);
      close.setStyle("-fx-background-color : #748cdc;");
      close.setTextFill(Paint.valueOf("#FFFFFF"));
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

      VBox menuContainer = new VBox();
      Label lbl1 = new Label("Are you sure the information you have provided is correct?");

      JFXButton continueButton = new JFXButton("Continue");
      continueButton.setButtonType(JFXButton.ButtonType.RAISED);
      continueButton.setStyle("-fx-background-color : #748cdc;");
      continueButton.setTextFill(Paint.valueOf("#FFFFFF"));

      JFXButton cancelButton = new JFXButton("Cancel");
      cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
      cancelButton.setStyle("-fx-background-color : #748cdc;");
      cancelButton.setTextFill(Paint.valueOf("#FFFFFF"));

      cancelButton.setTranslateX(100);
      cancelButton.setTranslateY(65);

      continueButton.setTranslateX(200);
      continueButton.setTranslateY(24);

      menuContainer.getChildren().addAll(lbl1, cancelButton, continueButton);
      menuContainer.setPadding(new Insets(30, 50, 50, 50));
      menuContainer.setSpacing(10);
      JFXPopup popup1 = new JFXPopup(menuContainer);
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
              submitToDatabase();

              BoxBlur blur = new BoxBlur(7, 7, 7);

              VBox manuContainer = new VBox();
              Label lbl1 =
                  new Label(
                      "Your request Has been submitted!                                                          ");

              JFXButton continueButton = new JFXButton("Return To Home");
              continueButton.setButtonType(JFXButton.ButtonType.RAISED);
              continueButton.setStyle("-fx-background-color : #748cdc;");
              continueButton.setTextFill(Paint.valueOf("#FFFFFF"));

              JFXButton cancelButton = new JFXButton("Complete Another Request");
              cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
              cancelButton.setStyle("-fx-background-color : #748cdc;");
              cancelButton.setTextFill(Paint.valueOf("#FFFFFF"));

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
                      // txtEmployeeName.setEffect(null);
                      popup1.hide();
                      advanceHome();
                      //   submit.setDisable(false);
                    }
                  });
              // submit.setDisable(true);
              anchorPage.setEffect(blur);
              //  txtEmployeeName.setEffect(blur);
              popup1.show(
                  myStackPane, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
              // submit.setDisable(false);
            }
          });
      // submit.setDisable(true);
      popup1.show(myStackPane2, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
    }
  }

  @FXML
  private void validateButton() {
    if (!departureTIme.getEditor().getText().isEmpty()
        && !transportTypeDropdown.getSelectionModel().isEmpty()
        && !patientRoomDropdown.getEditor().getText().isEmpty()
        && !employeeDropdown.getEditor().getText().isEmpty()
        && !addressBox.getEditor().getText().isEmpty()) {
      // submit.setDisable(false);
    } else {
      // submit.setDisable(true);
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
    close.setStyle("-fx-background-color : #748cdc");
    close.setTextFill(Paint.valueOf("#FFFFFF"));
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

  private boolean submitToDatabase() {
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
    return db.addRequest(request);
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

  private void loadRoomDropdown() {
    rooms = db.getAllNodesMap();
    for (Node node : rooms.values()) {
      Label lbl = new Label(node.get_longName());
      lbl.setId(node.get_nodeID());
      patientRoomDropdown.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(patientRoomDropdown);
  }

  private void loadMapsStuff() {

    GeoApiContext test =
        new GeoApiContext.Builder().apiKey("AIzaSyBBszEPZvetVvgsIbt3pLtXLbPap6dT-KY" + "").build();
    PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken();

    addressBox.setOnKeyReleased(
        key -> {
          if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.UP) return;
          AutocompletePrediction[] predictions = new AutocompletePrediction[0];
          LatLng origin = new LatLng(42.335570023832496, -71.10628519976504);
          try {
            predictions =
                PlacesApi.placeAutocomplete(test, addressBox.getEditor().getText(), token)
                    .origin(origin)
                    .radius(160934) // 100 miles in meters
                    .await();
          } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
          }
          Collection<String> address = new HashSet<>();
          for (AutocompletePrediction prediction : predictions) {
            address.add(prediction.description);
          }
          addressBox.getItems().setAll(address);
          addressBox.show();
        });
  }

  private void setupValidation() {
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    commentsBox.getValidators().add(reqInputValid);
    commentsBox
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) commentsBox.validate();
            });

    reqInputValid.setMessage("Cannot be empty");
    transportTypeDropdown.getValidators().add(reqInputValid);
    transportTypeDropdown
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) transportTypeDropdown.validate();
            });

    reqInputValid.setMessage("Cannot be empty");
    patientRoomDropdown.getValidators().add(reqInputValid);
    patientRoomDropdown
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) patientRoomDropdown.validate();
            });

    reqInputValid.setMessage("Cannot be empty");
    employeeDropdown.getValidators().add(reqInputValid);
    employeeDropdown
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) employeeDropdown.validate();
            });

    reqInputValid.setMessage("Cannot be empty");
    addressBox.getValidators().add(reqInputValid);
    addressBox
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) addressBox.validate();
            });

    reqInputValid.setMessage("Cannot be empty");
    departureTIme.getValidators().add(reqInputValid);
    departureTIme
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) departureTIme.validate();
            });
  }
}
