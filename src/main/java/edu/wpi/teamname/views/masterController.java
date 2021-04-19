package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

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

  @Inject
  public void setLoader(FXMLLoader loader) {
    this.loader = loader;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    log.debug(state.toString());
  }

  @FXML
  public void advanceHome(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeView.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void advanceHomePatient(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeViewPatient.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void advanceHomeAdmin(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("HomeViewAdmin.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  public void advanceServiceRequestPatient(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequestsPatient.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  public void advanceServiceRequestAdmin(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequestsAdmin.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  public void advanceServiceRequestEmployee(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequests.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  public void advanceServiceRequest(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ServiceRequests.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void returnToRequest(
      FXMLLoader childLoader, Scene ChildAppPrimaryScene, String requestPath) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream(requestPath + ".fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void logOut(FXMLLoader childLoader, Scene ChildAppPrimaryScene) throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("loginPage.fxml"));
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
}
