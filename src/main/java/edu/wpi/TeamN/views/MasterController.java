package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.AutoCompleteComboBoxListener;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

@Slf4j
public class MasterController implements Initializable {

  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
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

  public void setAnchorPane(AnchorPane anchorPane) {
    this.anchorPane = anchorPane;
  }

  public void setSideBarController(SideBarController sideBarController) {
    this.sideBarController = sideBarController;
  }

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    log.debug(state.toString());
  }

  @FXML
  public void advanceHome(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    switch (db.getCurrentUser().getType()) {
      case ADMINISTRATOR:
        advanceHomeAdmin(childLoader, ChildAppPrimaryScene);
        break;
      case EMPLOYEE:
        advanceHomeStaff(childLoader, ChildAppPrimaryScene);
        break;
      case PATIENT:
        advanceHomePatient(childLoader, ChildAppPrimaryScene);
        break;
      case GUEST:
        advanceHomeGuest(childLoader, ChildAppPrimaryScene);
        break;
    }
  }

  private void advanceHomeStaff(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeView.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  private void advanceHomePatient(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeViewPatient.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  private void advanceHomeAdmin(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeViewAdmin.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  private void advanceHomeGuest(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeViewGuest.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void register(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("RegisterNewUser.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void advanceHome2(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeView2.fxml"));
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
    Parent root = childLoader.load(getClass().getResourceAsStream("LoginPage.fxml"));
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
    new Stage();
    Stage stage2;
    stage2 = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
    stage2.setScene(null);
    stage2.close();
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

  @SneakyThrows
  public void sideBarSetup(
      AnchorPane anchorPane, Scene appPrimaryScene, FXMLLoader loader, String type) {
    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("SideBar.fxml"));
    Parent root = loader2.load();
    AnchorPane pane = new AnchorPane(root);
    sideBarController = loader2.getController();
    sideBarController.setAppPrimaryScene(appPrimaryScene);
    sideBarController.setLoader(loader);
    sideBarController.setType(type);
    anchorPane.getChildren().setAll(pane);
  }

  public void loadEmployeeDropdown(JFXComboBox<Label> employeeComboBox) {
    HashMap<String, User> users = db.getUsersByType(UserType.EMPLOYEE);
    for (User user : users.values()) {
      Label lbl = new Label(user.getUsername());
      lbl.setId(user.getId());
      employeeComboBox.getItems().add((lbl));
    }
    new AutoCompleteComboBoxListener(employeeComboBox);
  }

  public void loadRoomDropdown(JFXComboBox<Label> roomComboBox) {
    HashSet<Node> rooms = db.getAllNodes();
    for (Node node : rooms) {
      Label lbl = new Label(node.get_longName());
      lbl.setId(node.get_nodeID());
      roomComboBox.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(roomComboBox);
  }
}
