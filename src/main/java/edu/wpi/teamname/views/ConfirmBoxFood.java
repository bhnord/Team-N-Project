package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBoxFood extends masterController implements Initializable {
  static Stage stage;
  static ConfirmBoxFood box;
  static FoodDeliveryRequestController food;
  int count = 0;

  @FXML private JFXProgressBar jfxProgressBar;

  @FXML private JFXButton prgStart;

  @FXML private AnchorPane anchorPane;

  @FXML private Button returnHomeButton;

  @FXML private Button submission;

  @FXML private Label message;

  public static void confirm(FoodDeliveryRequestController foodType) throws IOException {

    food = foodType;
    String title = "confirmation box";

    FXMLLoader fxmlLoader = new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmBoxFood.fxml"));
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

  public void disable() throws IOException {
    returnHomeButton.setDisable(true);
  }

  @FXML
  public void continuePage() throws IOException {

    FXMLLoader fxmlLoader =
        new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmationPageFood.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box.stage.setScene(scene);
  }

  @FXML
  public void complete(ActionEvent actionEvent) throws IOException {
    count += 1;
    message.setText("Confirmation: request marked as completed.");
  }

  @FXML
  public void returnToHome(ActionEvent actionEvent) throws IOException {
    if (count > 0) {
      box.stage.close();
      food.advanceHome();
    } else {
      message.setText("You have to wait for the request to be completed before exiting. ");
    }
  }
}
