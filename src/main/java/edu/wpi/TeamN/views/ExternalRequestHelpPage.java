package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalRequestHelpPage extends masterController implements Initializable {

  static String path = "Requests/ExternalPatientRequest";
  static Stage stage;
  static ExternalRequestHelpPage box;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  Stage primaryStage;
  private Scene appPrimaryScene;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
  }

  public void returnToRequestPage(ActionEvent actionEvent) throws IOException {

    super.returnToRequest(loader, appPrimaryScene, path);
  }
}
