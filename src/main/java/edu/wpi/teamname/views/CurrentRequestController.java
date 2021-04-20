package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.services.database.requests.Request;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;

public class CurrentRequestController extends masterController implements Initializable {

  private Scene appPrimaryScene;
  @FXML private JFXListView<Label> listView;
  @FXML private JFXButton markCompleteButton;
  @FXML private JFXTextField RequestId;
  @FXML private JFXTextField requestType;
  @FXML private JFXTextField senderName;
  @FXML private JFXTextField ReceiverName;
  @FXML private JFXTextField contentField;
  @FXML private JFXTextField notesField;
  private Label selectedLabel;
  private HashMap<Integer, Request> requestMap = new HashMap<>();

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    listView.getItems().clear();
    HashSet<Request> set = db.getAllRequests();
    for (Request request : set) {
      Label lbl =
          new Label(
              request.getType().toString()
                  + " request for "
                  + db.getUserById(Integer.toString(request.getSenderID())).getUsername());
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
  }

  private void setEmptyFields() {
    RequestId.setText("");
    requestType.setText("");
    senderName.setText("");
    ReceiverName.setText("");
    contentField.setText("");
    notesField.setText("");
  }

  private void updateTextFields(Request clickedRequest) {
    RequestId.setText(Integer.toString(clickedRequest.getId()));
    requestType.setText(clickedRequest.getType().toString());
    senderName.setText(
        db.getUserById(Integer.toString(clickedRequest.getSenderID())).getUsername());
    ReceiverName.setText(
        db.getUserById(Integer.toString(clickedRequest.getReceiverID())).getUsername());
    contentField.setText(clickedRequest.getContent());
    notesField.setText(clickedRequest.getNotes());
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
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
