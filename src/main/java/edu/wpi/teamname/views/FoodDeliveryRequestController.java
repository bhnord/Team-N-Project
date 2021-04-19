package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FoodDeliveryRequestController extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  String helpPagePath = "FoodRequestHelpPage";

  private Scene appPrimaryScene;
  Stage primaryStage;

  @FXML private ChoiceBox Food;
  ObservableList<String> foodChoices =
      FXCollections.observableArrayList(
          "select available personal", "person 1", "person 2", "person 3", "person 4");

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
    Food.setValue("select available personal");
    Food.setItems(foodChoices);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException {
    ConfirmBoxFood.confirm(this);
  }

  public void help(ActionEvent actionEvent) throws IOException {
    super.returnToRequest(loader, appPrimaryScene, helpPagePath);
  }
}