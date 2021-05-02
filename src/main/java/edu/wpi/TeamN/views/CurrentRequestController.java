package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import edu.wpi.TeamN.services.database.CovidForm;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.requests.Request;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

public class CurrentRequestController extends MasterController implements Initializable {

  private Scene appPrimaryScene;
  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
  @FXML private JFXListView<Label> listView;
  @FXML private JFXButton markCompleteButton;
  @FXML private Label requestId;
  @FXML private Label requestType;
  @FXML private Label senderName;
  @FXML private Label receiverName;
  @FXML private Label content;
  @FXML private Label notes;
  @FXML private Label roomName;
  @FXML private AnchorPane anchorPane;
  private Label selectedLabel;
  @FXML private JFXListView<Label> listViewCovid;
  @FXML private Label symptoms;
  @FXML private Label tested;
  @FXML private Label treatment;
  @FXML private Label outsideTravel;
  @FXML private Label cruiseShip;
  @FXML private Label emergency;
  @FXML private JFXButton checkIn;
  private HashMap<Integer, Request> requestMap = new HashMap<>();

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }


  @Override
  public void initialize(URL url, ResourceBundle rb) {

    listView.getItems().clear();
    listViewCovid.getItems().clear();
    HashSet<CovidForm> formSet = new HashSet<>();
    HashSet<Request> set = db.getAllRequests();
    for (CovidForm covidForm : formSet) {
      Label lbl =
          new Label(
                  "Patient: "
                  + db.getUserById(covidForm.getUserId()).getLastname());
      lbl.setId(Integer.toString(covidForm.getId()));
      listView.getItems().add(lbl);
    }

    for (Request request : set) {
      Label lbl =
              new Label(
                      request.getType().getName()
                              + " request for "
                              + db.getUserById(request.getReceiverID()).getUsername());
      lbl.setId(Integer.toString(request.getId()));
      requestMap.put(request.getId(), request);
      listView.getItems().add(lbl);
    }

    listView.setOnMouseClicked(
            event -> {
              Label selected = listView.getSelectionModel().getSelectedItem();
              if (event.getButton() == MouseButton.PRIMARY && selected != null) {
                String id = selected.getId();
                Request clickedRequest = requestMap.get(Integer.valueOf(id));
                selectedLabel = selected;
                if (!(clickedRequest == null)) {
                  updateTextFields(clickedRequest);
                } else {
                  setEmptyFields();
                }
              }
            });

    listViewCovid.setOnMouseClicked(
            event -> {
              Label selected = listViewCovid.getSelectionModel().getSelectedItem();
              if (event.getButton() == MouseButton.PRIMARY && selected != null) {
                String id = selected.getId();
                selectedLabel = selected;
              }
            });

    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Database");
  }

  private void setEmptyFields() {
    requestId.setText("");
    requestType.setText("");
    senderName.setText("");
    receiverName.setText("");
    content.setText("");
    notes.setText("");
    roomName.setText("");

    symptoms.setText("");
    tested.setText("");
    treatment.setText("");
    outsideTravel.setText("");
    cruiseShip.setText("");
    emergency.setText("");


  }

  private void updateTextFields(Request clickedRequest) {
    requestId.setText(Integer.toString(clickedRequest.getId()));
    requestType.setText(clickedRequest.getType().getName());
    senderName.setText(db.getUserById(clickedRequest.getSenderID()).getUsername());
    receiverName.setText(db.getUserById(clickedRequest.getReceiverID()).getUsername());
    roomName.setText(db.getNode(clickedRequest.getRoomNodeId()).get_longName());
    content.setText(clickedRequest.getContent());
    notes.setText(clickedRequest.getNotes());
  }

  private void updateTextFieldsCovid(CovidForm covidForm) {
    symptoms.setText(covidForm.getAnswers());
    tested.setText(covidForm.getAnswers());
    treatment.setText(covidForm.getAnswers());
    outsideTravel.setText(covidForm.getAnswers());
    cruiseShip.setText(covidForm.getAnswers());
    emergency.setText(covidForm.getAnswers());
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  public void inProgress() throws IOException {}

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void advanceViews(ActionEvent actionEvent) throws IOException {
    super.advanceViews(actionEvent);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void checkIn(ActionEvent actionEvent) {
    if (listViewCovid.getItems().isEmpty() || selectedLabel == null) return;
    int index = listViewCovid.getItems().indexOf(selectedLabel);
    db.deleteRequest(Integer.valueOf(selectedLabel.getId()));
    listViewCovid.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (!(listViewCovid.getItems().size() == 0)) {
      selectedLabel = listViewCovid.getItems().get(index);
    } else {
      selectedLabel = null;
      setEmptyFields();
    }
  }

  public void markComplete(ActionEvent actionEvent) {
    if (listView.getItems().isEmpty() || selectedLabel == null) return;
    int index = listView.getItems().indexOf(selectedLabel);
    db.deleteRequest(Integer.valueOf(selectedLabel.getId()));
    listView.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (!(listView.getItems().size() == 0)) {
      selectedLabel = listView.getItems().get(index);
      Request clickedRequest = requestMap.get(Integer.valueOf(selectedLabel.getId()));
      if (clickedRequest != null) {
        updateTextFields(clickedRequest);
      } else {
        setEmptyFields();
      }
    } else {
      selectedLabel = null;
      setEmptyFields();
    }
  }

}
