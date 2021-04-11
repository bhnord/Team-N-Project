package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.ServiceTwo;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBoxFloral extends masterController implements Initializable {
  static Stage stage2;
  static ConfirmBox box;
  static FloralRequest floral;

//  @Inject DatabaseService db;
//  @Inject ServiceTwo graph;
//  @Inject FXMLLoader loader;
//  @Inject HomeState state;
//  @FXML private Label text;
//
//  private Scene appPrimaryScene;

  public static void confirm(FloralRequest floralType) throws IOException {

    floral = floralType;
    String title = "floral confirmation box";

    FXMLLoader fxmlLoader = new FXMLLoader(ConfirmBox.class.getResource("ConfirmBox.fxml"));
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
        new FXMLLoader(ConfirmBox.class.getResource("ConfirmationPageFloral.fxml"));
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
  public void returnToHomeFloral() throws IOException {
    box.stage.close();
    floral.advanceHome();
    //advanceHome();
  }
}
