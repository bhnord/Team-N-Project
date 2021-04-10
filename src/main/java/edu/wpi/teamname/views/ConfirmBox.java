package edu.wpi.teamname.views;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
  static Scene scene2;
  static Stage stage;
  static ConfirmBox box;
  static FoodDeliveryRequest food;

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

  @FXML
  public void cancel() throws IOException {
    box.stage.close();
  }

  @FXML
  public void continuePage() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ConfirmBox.class.getResource("ConfirmationPage.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);

    box.stage.setScene(scene);
  }

  @FXML
  public void returnToHome() throws IOException {
    box.stage.close();
    food.advanceHome();
  }
}
