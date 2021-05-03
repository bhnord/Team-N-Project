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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
  private int userId;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    listView.getItems().clear();
    listViewCovid.getItems().clear();
    HashSet<CovidForm> formSet = db.getAllCovidForms();
    HashSet<Request> set = db.getAllRequests();
    for (CovidForm covidForm : formSet) {
      userId = covidForm.getUserId();
      Label lbl = new Label("Survey completed by: " + db.getCurrentUser().getUsername());
      lbl.setId(Integer.toString(covidForm.getId()));
      listViewCovid.getItems().add(lbl);
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
            Integer id = Integer.parseInt(selected.getId());
            CovidForm covidForm = db.getCovidForm(id);
            selectedLabel = selected;
            if (!(covidForm == null)) {
              updateTextFieldsCovid(covidForm);
            } else {
              setEmptyFields();
            }
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

    boolean[] ans = covidForm.getAnswers();
    ArrayList<String> a = new ArrayList<String>(ans.length);
    for (int i = 0; i < ans.length; i++) {

      if (String.valueOf(ans[i]) == "false") {
        a.add("no");
      } else if (String.valueOf(ans[i]) == "true") {
        a.add("yes");
      }
    }

    symptoms.setText(a.get(0));
    tested.setText(a.get(1));
    treatment.setText(a.get(2));
    outsideTravel.setText(a.get(3));
    cruiseShip.setText(a.get(4));
    emergency.setText(a.get(5));
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
      Integer id = Integer.parseInt(selectedLabel.getId());
      CovidForm covidForm = db.getCovidForm(id);
      if (!(covidForm == null)) {
        updateTextFieldsCovid(covidForm);
      } else {
        setEmptyFields();
      }
    } else {
      selectedLabel = null;
      setEmptyFields();
    }
  }

  public void inProgress(ActionEvent actionEvent) {
    listView.setCellFactory(
        list -> {
          ListCell<Label> cell =
              new ListCell<Label>() {
                protected void updateItem(Label item, boolean empty) {
                  super.updateItem(item, empty);
                  if (empty || item == null) {
                    // There is no item to display in this cell, so leave it empty
                    setGraphic(null);

                    // Clear the style from the cell
                    setStyle(null);
                  } else {
                    // If the item is equal to the first item in the list, set the style
                    if (item == (list.getItems())) {
                      // Set the background color to blue
                      setStyle("-fx-background-color: blue; -fx-text-fill: white");
                    }
                    // Finally, show the item text in the cell
                    setText(item.toString());
                  }
                }
              };
          return cell;
        });
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
