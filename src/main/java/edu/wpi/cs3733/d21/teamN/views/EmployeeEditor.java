package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserPrefs;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserType;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import edu.wpi.cs3733.d21.teamN.utilities.AutoCompleteComboBoxListener;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeEditor extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  FileChooser fileChooser = new FileChooser();

  @FXML private Label messageLabel;
  @FXML private Label loadSuccess;
  @FXML private Button HomeView;
  @FXML private JFXListView<Label> listView;
  @FXML private AnchorPane anchorPane;

  @FXML private JFXTextField txtUsername;
  @FXML private JFXTextField txtPassword;
  @FXML private JFXComboBox<Label> userTypeDropdown = new JFXComboBox<>();
  // TODO REMOVE NODE ID FIELD TO JUST LABEL AND NOT AN EDITOR.
  private Label selectedLabel;
  private Scene appPrimaryScene;
  private String selectedFilePath;
  private int numUsersAdded = 0;

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
  public void initialize(URL url, ResourceBundle rb) {

    updateStyle(db.getCurrentUser().getAppColor());
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Database");

    listView.setOnMouseClicked(
        event -> {
          Label selected = listView.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            Integer id = Integer.parseInt(selected.getId());
            User clickedUser = db.getUserById(id);
            messageLabel.setText("");
            selectedLabel = selected;
            if (!(clickedUser == null)) {
              updateTextFields(clickedUser);
            } else {
              setEmptyFields();
            }
          }
        });
    //      File file = new File("src/main/resources/MapCSV/MapNNodesAll.csv");
    //      selectedFilePath = file.getPath();
    //      loadNodes(file);
    loadFromDB();
    loadTypesDropdown();
  }

  private void updateTextFields(User clickedUser) {
    txtUsername.setText(clickedUser.getUsername());
    userTypeDropdown.getEditor().setText(clickedUser.getType().toString());
    txtPassword.setText("********");
  }

  @FXML
  private void addNew(ActionEvent actionEvent) {
    numUsersAdded++;
    Label lbl = new Label("New User " + numUsersAdded);
    lbl.setId("New User " + numUsersAdded);
    listView.getItems().add(lbl);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void commitChanges(ActionEvent actionEvent) {
    if (selectedLabel == null) {
      return;
    }
    String username = txtUsername.getText();
    String password = txtPassword.getText();

    UserType type =
        UserType.valueOf(
            userTypeDropdown.getSelectionModel().getSelectedItem().getText().toUpperCase());
    User selectedUser = db.getUserByUsername(selectedLabel.getText());

    if (!(selectedUser == null)) {
      if (password.equals("********")) {
        if (!db.updateUserUsernameType(selectedUser.getId(), username, type)) {
          messageLabel.setText("Invalid inputs");
        }
      } else if (!db.updateUser(selectedUser.getId(), username, password, type, new UserPrefs())) {
        messageLabel.setText("Invalid inputs");
      }
    } else if (db.addUser(username, password, type, new UserPrefs())) {
      updateSelectedLabel(db.getUserByUsername(username));
    } else {
      messageLabel.setText("Invalid inputs");
    }
  }

  private void updateSelectedLabel(User u) {
    selectedLabel.setId("" + u.getId());
    selectedLabel.setText(u.getUsername());
  }

  public void deleteUser(ActionEvent actionEvent) {
    if (listView.getItems().isEmpty() || selectedLabel == null) return;
    int index = listView.getItems().indexOf(selectedLabel);
    db.deleteUser(selectedLabel.getText());
    listView.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (!(listView.getItems().size() == 0)) {
      selectedLabel = listView.getItems().get(index);
      User clickedUser = db.getUserById(Integer.parseInt(selectedLabel.getId()));
      if (clickedUser != null) {
        updateTextFields(clickedUser);
      } else {
        setEmptyFields();
      }
    } else {
      selectedLabel = null;
      setEmptyFields();
    }
  }

  private void loadFromDB() {
    listView.getItems().clear();
    HashSet<User> set = db.getAllUsers();
    for (User u : set) {
      if (!u.getUsername().equals("guest")) {
        Label lbl = new Label(u.getUsername());
        lbl.setId("" + u.getId());
        listView.getItems().add(lbl);
      }
    }
  }

  private void setEmptyFields() {
    txtUsername.setText("");
    txtPassword.setText("");
    userTypeDropdown.getEditor().setText("");
  }

  private void loadTypesDropdown() {
    for (UserType type : UserType.values()) {
      String s = type.toString();
      Label lbl = new Label(s);
      lbl.setId(s);
      userTypeDropdown.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(userTypeDropdown);
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
    super.advanceViews(actionEvent);
  }

  @FXML private JFXButton commitChangesButton, addEmployeeButton, deleteUserButton;

  public void updateStyle(String color) {
    String style =
        "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 25;";
    JFXButton[] lA = {commitChangesButton, addEmployeeButton, deleteUserButton};
    for (JFXButton a : lA) a.setStyle(style);
  }
}
