package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.ServiceTwo;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FoodDeliveryRequest extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject ServiceTwo graph;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  private Scene appPrimaryScene;
  Stage primaryStage;

  @FXML private ChoiceBox Food;
  ObservableList<String> foodChoices =
      FXCollections.observableArrayList("Select food", "Pizza", "Apple stew", "Beef stew");

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
    Food.setValue("Select food");
    Food.setItems(foodChoices);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException {
    ConfirmBox.confirm(this);
  }

  public void help(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("FoodRequestHelpPage.fxml"));
    appPrimaryScene.setRoot(root);
    primaryStage.setScene(appPrimaryScene);
    primaryStage.setAlwaysOnTop(true);
    primaryStage.show();
  }
}
