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
  static FoodDelivery food;

  public static void confirm(FoodDelivery foodType) throws IOException {

    food = foodType;

    // Stage window = new Stage();
    // String message = "are you sure you want to proceed?";
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

    // window.setWidth(500);
    // window.setHeight(500);

    // Label label1 = new Label(message);

    // Button no = new Button("no");
    // no.setOnAction(e -> window.close());

    // Button yes = new Button("yes");
    // yes.setOnAction(e -> window.setScene(scene2));

    // layout 2
    // Button confirmed = new Button("exit");
    // Label confirmation = new Label("Your request has been submitted! This is a confirmation!");
    // Box layout2 = new VBox(10); // StackPane layout2 = new StackPane();
    //
    //    layout2.getChildren().addAll(confirmation, confirmed);
    //    scene2 = new Scene(layout2, 600, 600);
    //    confirmed.setOnAction(e -> window.close());
    //
    //    VBox layout = new VBox(10);
    //    layout.getChildren().addAll(label1, yes, no);
    //    layout.setAlignment(Pos.CENTER);
    //
    //    Scene scene = new Scene(layout);
    //    window.setScene(scene);
    //    window.showAndWait();
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
