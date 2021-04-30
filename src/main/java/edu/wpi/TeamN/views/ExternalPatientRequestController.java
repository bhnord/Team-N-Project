package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.LatLng;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.requests.Request;
import edu.wpi.TeamN.services.database.requests.RequestType;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.DialogFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;

@Slf4j
public class ExternalPatientRequestController extends MasterController implements Initializable {

  // @FXML private AnchorPane anchorPage;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label errorLabel;
  @FXML private JFXTextField commentsBox;
  @FXML private JFXComboBox addressBox;
  @FXML private JFXComboBox transportTypeDropdown;
  @FXML private JFXComboBox<Label> employeeDropdown;
  @FXML private JFXComboBox<Label> patientRoomDropdown;
  @FXML private JFXTimePicker departureTIme;
  @FXML private AnchorPane anchorPage;
  @FXML private StackPane rootStackPane;
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
    dialogFactory = new DialogFactory(rootStackPane);
    transportTypeDropdown.getItems().add("Ambulance");
    transportTypeDropdown.getItems().add("Helicopter");
    transportTypeDropdown.getItems().add("Plane");
    loadEmployeeDropdown(employeeDropdown);
    loadRoomDropdown(patientRoomDropdown);
    loadMapsStuff();
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

  private boolean validateInputs() {
    return (departureTIme.getEditor().getText().isEmpty()
        || transportTypeDropdown.getSelectionModel().isEmpty()
        || patientRoomDropdown.getEditor().getText().isEmpty()
        || employeeDropdown.getEditor().getText().isEmpty()
        || addressBox.getEditor().getText().isEmpty());
  }
}
