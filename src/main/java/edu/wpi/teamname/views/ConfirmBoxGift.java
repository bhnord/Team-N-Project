package edu.wpi.teamname.views;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBoxGift extends masterController implements Initializable {
  static Stage stage;
  static ConfirmBoxGift box;
  static GiftDeliveryRequestController gift = new GiftDeliveryRequestController();

  public static void confirm(GiftDeliveryRequestController giftType) throws IOException {

    gift = giftType;
    String title = "gift delivery confirmation box";

    FXMLLoader fxmlLoader = new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmBoxGift.fxml"));
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
        new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmationPageGift.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box.stage.setScene(scene);
  }

  @FXML
  public void returnToHomeGift() throws IOException {
    box.stage.close();
    gift.advanceHome();
  }
}
