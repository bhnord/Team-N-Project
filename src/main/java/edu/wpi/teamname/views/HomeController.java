package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.ServiceTwo;
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
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomeController implements Initializable {

  @Inject DatabaseService db;
  @Inject ServiceTwo graph;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
  }

  @FXML
  private void advanceFloral() throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("FloralRequest.fxml"));
    appPrimaryScene.setRoot(root);
  }

  public void advanceFood(ActionEvent actionEvent) {}

  public void advanceLanguageInterpreters(ActionEvent actionEvent) {}

  public void advanceSanitationService(ActionEvent actionEvent) {}

  public void advanceLaundry(ActionEvent actionEvent) {}

  public void advanceMedicineDelivery(ActionEvent actionEvent) {}

  public void advanceReligionRequest(ActionEvent actionEvent) {}

  public void advanceInternalPatientTransport(ActionEvent actionEvent) {}

  public void advanceExternalPatient(ActionEvent actionEvent) {}

  public void advanceSecurityServices(ActionEvent actionEvent) {}

  public void advanceFacilityMaintenance(ActionEvent actionEvent) {}

  public void AdvanceServiceRequest(ActionEvent actionEvent) {}

  public void advanceAudioVisual(ActionEvent actionEvent) {}

  public void advanceLaundryService(ActionEvent actionEvent) {}
}
