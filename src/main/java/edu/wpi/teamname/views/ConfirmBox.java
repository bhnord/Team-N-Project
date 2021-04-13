package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
  static Scene scene2;
  static Stage stage;
  static ConfirmBox box;
  static FoodDeliveryRequest food;
  static SecurityServicesRequest security;
  // static InternalPatient patient;

  @FXML private JFXProgressBar jfxProgressBar;

  @FXML private JFXButton prgStart;

  @FXML private AnchorPane anchorPane;

  public static void confirm(FoodDeliveryRequest foodType) throws IOException {

    food = foodType;
    String title = "confirmation box";

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

  public static void confirm(SecurityServicesRequest securityServicesRequest) throws IOException {

    security = securityServicesRequest;
    String title = "confirmation box";

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

  /*

    public static void confirm(InternalPatient iP) throws IOException {

      patient = iP;
      String title = "confirmation box";

      FXMLLoader fxmlLoader =
          new FXMLLoader(ConfirmBox.class.getResource("ConfirmBoxInternalPatient.fxml"));
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
  */

  //  @FXML
  //  public void cancel() throws IOException {
  //    box.stage.close();
  //  }
  //
  //  @FXML
  //  public void continuePage() throws IOException {
  //    FXMLLoader fxmlLoader = new
  // FXMLLoader(ConfirmBox.class.getResource("ConfirmationPage.fxml"));
  //    Parent root = fxmlLoader.load();
  //    Scene scene = new Scene(root);
  //
  //    box.stage.setScene(scene);
  //  }
  // *
  @FXML
  public void returnToHome() throws IOException {
    box.stage.close();
    food.advanceHome();
  }
}

/*
@FXML
public void continuePageInternalPatient() throws IOException {
  FXMLLoader fxmlLoader =
      new FXMLLoader(ConfirmBox.class.getResource("ConfirmationPageInternalPatient.fxml"));
  Parent root = fxmlLoader.load();
  Scene scene = new Scene(root);

  box.stage.setScene(scene);


  @FXML
  public void returnToHomeInternalPatient() throws IOException {
    box.stage.close();
    patient.advanceHome();
  }
*/

  /** Progress Bar w/ button* */
  /*
  @FXML
  private void handleEventButton(ActionEvent ae) {
    Task task = taskWorker(20);
    jfxProgressBar.progressProperty().unbind();
    jfxProgressBar.progressProperty().bind(task.progressProperty());

    // can change stage.close to go to different primary scene
    task.setOnSucceeded(
        e -> {
          try {
            box.stage.close();
            returnToHomeInternalPatient();
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });
    Thread th = new Thread(task);
    th.start();
  }
  */
  /** Telling what to do while progress bar is filling up* */
  /*
  private Task taskWorker(int seconds) {
    return new Task() {
      @Override
      protected Object call() throws Exception {
        for (int i = 0; i < seconds; i++) {
          updateProgress(i + 1, seconds);
          Thread.sleep(100);
        }
        return null;
      }
    };
  }*/
