package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.state.Login;
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
import javafx.scene.control.Tooltip;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceRequests extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  // ToolTips for Iteration 2 names on service request buttons.
  @FXML private JFXButton AudioVisualRequest;
  @FXML private Tooltip ttAnanya;
  @FXML private JFXButton GiftDeliveryRequest;
  @FXML private Tooltip ttAnanya2;
  @FXML private JFXButton SecurityServicesRequest;
  @FXML private Tooltip ttRomish;
  @FXML private JFXButton FacilityMaintenanceRequest;
  @FXML private Tooltip ttJohn;
  @FXML private JFXButton ComputerServiceRequest;
  @FXML private Tooltip ttAnanya3;
  @FXML private JFXButton InternalPatientRequest;
  @FXML private Tooltip ttJacob;
  @FXML private JFXButton ExternalPatientRequest;
  @FXML private Tooltip ttMichael;
  @FXML private JFXButton MedicineDeliveryRequest;
  @FXML private Tooltip ttFinn;
  @FXML private JFXButton FloralRequest;
  @FXML private Tooltip ttAnanya4;
  @FXML private JFXButton LaundryRequest;
  @FXML private Tooltip ttAnanya5;
  @FXML private JFXButton SanitationServicesRequest;
  @FXML private Tooltip ttYongxiang;
  @FXML private JFXButton LanguageInterpretersRequest;
  @FXML private Tooltip ttBen;
  @FXML private JFXButton FoodDeliveryRequest;
  @FXML private Tooltip ttAlex;
  @FXML private JFXButton ReligiousRequest;
  @FXML private Tooltip ttPayton;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
    AudioVisualRequest.setTooltip(ttAnanya);
    GiftDeliveryRequest.setTooltip(ttAnanya2);
    SecurityServicesRequest.setTooltip(ttRomish);
    FacilityMaintenanceRequest.setTooltip(ttJohn);
    ExternalPatientRequest.setTooltip(ttMichael);
    ComputerServiceRequest.setTooltip(ttAnanya3);
    InternalPatientRequest.setTooltip(ttJacob);
    MedicineDeliveryRequest.setTooltip(ttFinn);
    FloralRequest.setTooltip(ttAnanya4);
    LaundryRequest.setTooltip(ttAnanya5);
    SanitationServicesRequest.setTooltip(ttYongxiang);
    LanguageInterpretersRequest.setTooltip(ttBen);
    FoodDeliveryRequest.setTooltip(ttAlex);
    ReligiousRequest.setTooltip(ttPayton);
  }

  @FXML
  public void advanceHome() throws IOException {
    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    }
    // super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }
}
