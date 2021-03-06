package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.services.database.AppointmentType;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject private HomeState state;
  @FXML private Label text;

  @FXML private AnchorPane anchorPane;
  @FXML private SideBarController sideBarController;
  protected Scene appPrimaryScene;

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

  @Inject
  public void setLoader(FXMLLoader loader) {
    this.loader = loader;
  }

  @Inject
  public void setDB(DatabaseService db) {
    this.db = db;
  }

  public void setAnchorPane(AnchorPane anchorPane) {
    this.anchorPane = anchorPane;
  }

  public void setSideBarController(SideBarController sideBarController) {
    this.sideBarController = sideBarController;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void advanceHome(FXMLLoader childLoader, Scene ChildAppPrimaryScene) {
    Parent root = null;
    try {
      root = childLoader.load(getClass().getResourceAsStream("HomeViewAdmin.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    ChildAppPrimaryScene.setRoot(root);
  }

  public void advanceMap(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("Pathfinder.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void register(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("RegisterNewUser.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void credits(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("CreditsPage.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void helpPage(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HelpPage.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  private void advanceServiceRequestPatient(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequestsPatient.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  private void advanceServiceRequestAdmin(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequestsAdmin.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  private void advanceServiceRequestEmployee(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequests.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  public void advanceServiceRequest(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    //    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequests.fxml"));
    //    ChildAppPrimaryScene.setRoot(root);
    switch (db.getCurrentUser().getType()) {
      case ADMINISTRATOR:
        advanceServiceRequestAdmin(childLoader, ChildAppPrimaryScene);
        break;
      case EMPLOYEE:
        advanceServiceRequestEmployee(childLoader, ChildAppPrimaryScene);
        break;
      case PATIENT:
        advanceServiceRequestPatient(childLoader, ChildAppPrimaryScene);
        break;
    }
  }

  @FXML
  public void returnToRequest(
      FXMLLoader childLoader, Scene ChildAppPrimaryScene, String requestPath) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream(requestPath + ".fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void logOut(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {

    db.logout();

    Parent root = childLoader.load(getClass().getResourceAsStream("HomeViewAdmin.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  public void closePopUp(ActionEvent actionEvent) throws IOException {
    Parent root =
        loader.load(
            getClass()
                .getResourceAsStream("src/main/java/edu/wpi/teamname/views/HomeController.java"));
    appPrimaryScene.setRoot(root);

    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
    stage.close();
  }

  public void cancel(ActionEvent actionEvent) throws IOException {
    System.exit(0);
    //    new Stage();
    //    Stage stage2;
    //    stage2 = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
    //    stage2.setScene(null);
    //    stage2.close();
  }

  public void advance(ActionEvent actionEvent) throws IOException {
    String file = "Requests/" + ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  public SideBarController sideBarSetup(
      AnchorPane anchorPane, Scene appPrimaryScene, FXMLLoader loader, String type) {
    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("SideBar.fxml"));
    Parent root = null;
    try {
      root = loader2.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AnchorPane pane = new AnchorPane(root);
    sideBarController = loader2.getController();
    sideBarController.setAppPrimaryScene(appPrimaryScene);
    sideBarController.setLoader(loader);
    sideBarController.setDB(db);
    sideBarController.setType(type);
    anchorPane.getChildren().setAll(pane);
    return sideBarController;
  }

  public SideBarController sideBarSetup(
      AnchorPane anchorPane, Scene appPrimaryScene, FXMLLoader loader, String type, String any) {
    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("SideBar.fxml"));
    Parent root = null;
    try {
      root = loader2.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AnchorPane pane = new AnchorPane(root);
    sideBarController = loader2.getController();
    sideBarController.setAppPrimaryScene(appPrimaryScene);
    sideBarController.setLoader(loader);
    sideBarController.setDB(db);
    sideBarController.setType(type);
    anchorPane.getChildren().setAll(pane);
    return sideBarController;
  }

  // TODO REFACTOR AFTER SPLITTING USERS AND EMPLOYEES
  public void loadEmployeeDropdown(JFXComboBox<Label> employeeComboBox) {
    HashSet<User> users = db.getUsersByType(UserType.EMPLOYEE);
    for (User user : users) {
      Label lbl = new Label(user.getUsername());
      lbl.setId("" + user.getId());
      employeeComboBox.getItems().add((lbl));
    }
    new AutoCompleteComboBoxListener(employeeComboBox);
  }

  public void loadAppointmentTypeDropdown(JFXComboBox<Label> employeeComboBox) {
    HashSet<AppointmentType> appoints = db.getAllAppointmentTypes();
    for (AppointmentType appoint : appoints) {
      Label lbl = new Label(appoint.getType());
      lbl.setId("" + appoint.getId());
      employeeComboBox.getItems().add((lbl));
    }
    new AutoCompleteComboBoxListener(employeeComboBox);
  }

  public void loadPatientDropdown(JFXComboBox<Label> employeeComboBox) {
    HashSet<User> users = db.getUsersByType(UserType.PATIENT);
    for (User user : users) {
      Label lbl = new Label(user.getUsername());
      lbl.setId("" + user.getId());
      employeeComboBox.getItems().add((lbl));
    }
    new AutoCompleteComboBoxListener(employeeComboBox);
  }

  public void loadRoomDropdown(JFXComboBox<Label> roomComboBox, String filter) {
    HashSet<Node> rooms = db.getAllNodes();
    for (Node node : rooms) {
      if (filter == null || (!node.get_nodeType().contains(filter))) {
        if (!node.get_longName().isEmpty()) {
          Label lbl = new Label(node.get_longName());
          lbl.setId(node.get_nodeID());
          roomComboBox.getItems().add(lbl);
        }
      }
    }
    new AutoCompleteComboBoxListener(roomComboBox);
  }

  public void loadRoomDropdown(JFXComboBox<Label> roomComboBox) {
    this.loadRoomDropdown(roomComboBox, null);
  }
}
