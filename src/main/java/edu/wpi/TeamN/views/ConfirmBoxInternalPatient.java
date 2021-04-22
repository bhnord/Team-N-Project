package edu.wpi.TeamN.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBoxInternalPatient {
  static Scene scene2;
  static Stage stage;
  static edu.wpi.TeamN.views.ConfirmBoxInternalPatient box;

  static InternalPatientRequestController patient;

  @FXML private JFXProgressBar jfxProgressBar;

  @FXML private JFXButton prgStart;

  @FXML private AnchorPane anchorPane;

  public static void confirm(InternalPatientRequestController iP) throws IOException {

    patient = iP;
    String title = "confirmation box";

    FXMLLoader fxmlLoader =
        new FXMLLoader(
            ConfirmBoxInternalPatient.class.getResource("ConfirmBoxInternalPatient.fxml"));
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
        new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmationPageInternalPatient.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);

    box.stage.setScene(scene);
  }

  @FXML
  public void returnToHome() throws IOException {
    box.stage.close();
    patient.advanceHome();
  }

  /** Progress Bar w/ button* */
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
            returnToHome();
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });
    Thread th = new Thread(task);
    th.start();
  }

  /** Telling what to do while progress bar is filling up* */
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
  }
}
