package edu.wpi.teamname.views;

import com.google.inject.*;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FoodRequestHelpPage extends masterController implements Initializable {

  static String path = "Requests/FoodDeliveryRequest";
  static Stage stage;
  static FoodRequestHelpPage box;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @Inject ServiceTwo graph;
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
    // super.returnToRequest(loader, appPrimaryScene);

  }
}