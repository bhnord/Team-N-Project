package edu.wpi.teamname.views;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBoxSecurityService extends masterController implements Initializable {
  static Stage stage;
  static ConfirmBoxSecurityService box;
  static SecurityServicesRequest security = new SecurityServicesRequest();

  //  @Inject DatabaseService db;
  //  @Inject ServiceTwo graph;
  //  @Inject FXMLLoader loader;
  //  @Inject HomeState state;
  //  @FXML private Label text;
  //
  //  private Scene appPrimaryScene;

  public static void confirm(SecurityServicesRequest securityService) throws IOException {

    security = securityService;
    String title = "Security confirmation box";

    FXMLLoader fxmlLoader =
        new FXMLLoader(ConfirmBox.class.getResource("ConfirmBoxSecurityService.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box = fxmlLoader.getController();
    box.stage = new Stage();
    box.stage.setScene(scene);

    box.stage.initModality(
        Modality.APPLICATION_MODAL); // pop-up window won't close until you have dealt with it
    box.stage.setAlwaysOnTop(true);
    box.stage.setTitle(title);

    box.stage.showAndWait();
  }

  @FXML
  public void cancel() throws IOException {
    box.stage.close();
  }

  @FXML
  public void continuePage() throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(ConfirmBox.class.getResource("ConfirmationPageSecurityServices.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box.stage.setScene(scene);
  }

  //  @Inject
  //  public void setAppPrimaryScene(Scene appPrimaryScene) {
  //    this.appPrimaryScene = appPrimaryScene;
  //  }
  //
  //  @FXML
  //  public void advanceHome() throws IOException {
  //    super.advanceHome(loader, appPrimaryScene);
  //  }

  @FXML
  public void returnToHome() throws IOException {
    box.stage.close();
    security.advanceHome();
    // advanceHome();
  }
}
