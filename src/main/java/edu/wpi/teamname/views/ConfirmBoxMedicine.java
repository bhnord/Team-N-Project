package edu.wpi.teamname.views;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBoxMedicine extends masterController implements Initializable {
  static Stage stage2;
  static ConfirmBoxMedicine box;
  static MedicineDeliveryRequest medicine = new MedicineDeliveryRequest();

  public static void confirm(MedicineDeliveryRequest medicineType) throws IOException {

    medicine = medicineType;
    String title = "medicine confirmation box";

    FXMLLoader fxmlLoader =
        new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmBoxMedicine.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box = fxmlLoader.getController();
    box.stage2 = new Stage();
    box.stage2.setScene(scene);

    box.stage2.initModality(
        Modality.APPLICATION_MODAL); // pop-up window won't close until you have dealt with it
    box.stage2.setAlwaysOnTop(true);
    box.stage2.setTitle(title);

    box.stage2.showAndWait();
  }

  @FXML
  public void cancel() throws IOException {
    box.stage2.close();
  }

  @FXML
  public void continuePage() throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmationPageMedicine.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box.stage2.setScene(scene);
  }

  @FXML
  public void returnToHomeMedicine() throws IOException {
    box.stage2.close();
    medicine.advanceHome();
  }
}
