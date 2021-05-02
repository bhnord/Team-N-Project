package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.TeamN.services.database.CovidForm;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.AutoCompleteComboBoxListener;
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

public class CovidFormEditor extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  FileChooser fileChooser = new FileChooser();

  @FXML private Label messageLabel;
  @FXML private Label loadSuccess;
  @FXML private Button HomeView;
  @FXML private JFXListView<Label> listView;
  @FXML private AnchorPane anchorPane;

  @FXML private Label txtFirstName;
  @FXML private Label txtLastName;
  @FXML private Label txtAnsweredYes;
  @FXML public JFXComboBox<Label> comboSelectOk = new JFXComboBox<>();
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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Database");

    listView.setOnMouseClicked(
        event -> {
          Label selected = listView.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            Integer id = Integer.parseInt(selected.getId());
            CovidForm clickedForm = db.getCovidForm(id);
            messageLabel.setText("");
            selectedLabel = selected;
            if (!(clickedForm == null)) {
              updateTextFields(clickedForm);
            } else {
              setEmptyFields();
            }
          }
        });
    //      File file = new File("src/main/resources/MapCSV/MapNNodesAll.csv");
    //      selectedFilePath = file.getPath();
    //      loadNodes(file);
    loadFromDB();
    loadOkDropdown();
  }

  private void updateTextFields(CovidForm clickedForm) {
    txtLastName.setText(db.getUserById(clickedForm.getUserId()).getLastname());
    txtFirstName.setText(db.getUserById(clickedForm.getUserId()).getFirstname());
    String answers = "Answered 'yes' to: ";
    boolean ans[] = clickedForm.getAnswers();
    for (int i = 0; i < ans.length; i++) {
      answers += (ans[i]) ? "" : "Q" + (i + 1) + " ";
    }
    txtAnsweredYes.setText(answers);
    if (clickedForm.isOk()) {
      comboSelectOk.getSelectionModel().select(1);
    } else {
      comboSelectOk.getSelectionModel().select(0);
    }
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
    boolean isOk = false;
    if (comboSelectOk.getSelectionModel().getSelectedItem().getId() == "1") {
      isOk = true;
    }

    CovidForm selectedForm = db.getCovidForm(Integer.parseInt(selectedLabel.getId()));

    if (!(selectedForm == null)) {
      if (!db.updateCovidForm(
          selectedForm.getId(),
          Boolean.parseBoolean(comboSelectOk.getSelectionModel().getSelectedItem().getId()))) {
        messageLabel.setText("Invalid inputs");
      }
    }
  }

  private void updateSelectedLabel(User u) {
    selectedLabel.setId("" + u.getId());
    selectedLabel.setText(u.getUsername());
  }

  private void loadFromDB() {
    listView.getItems().clear();
    HashSet<CovidForm> set = db.getAllCovidForms();
    for (CovidForm f : set) {
      Label lbl = new Label(db.getUserById(f.getUserId()).getUsername());
      lbl.setId("" + f.getId());
      listView.getItems().add(lbl);
    }
  }

  private void setEmptyFields() {
    txtAnsweredYes.setText("");
    txtFirstName.setText("");
    txtLastName.setText("");
  }

  private void loadOkDropdown() {
    Label notOk = new Label("Enter through back");
    notOk.setId("false");
    comboSelectOk.getItems().add(notOk);
    Label ok = new Label("Enter through front");
    ok.setId("true");
    comboSelectOk.getItems().add(ok);
    new AutoCompleteComboBoxListener(comboSelectOk);
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
}
