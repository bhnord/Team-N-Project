package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import edu.wpi.TeamN.state.HomeState;
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
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceRequests extends MasterController implements Initializable {

  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private AnchorPane anchorPane;
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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Database");
    switch (db.getCurrentUser().getType()) {
        // different login cases to be implemented later
      case ADMINISTRATOR:
      case EMPLOYEE:
      case PATIENT:
        break;
    }
  }

  /**
   * advance Takes from Service Request Page to separate service requests
   *
   * @param actionEvent Button click
   * @throws IOException
   */
  public void advance(ActionEvent actionEvent) throws IOException {
    String file = "Requests/" + ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }
}
