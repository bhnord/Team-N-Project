package edu.wpi.teamname.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
  static Scene scene2;
  static Scene scene1;

  public static void confirm() {
    Stage window = new Stage();
    String message = "are you sure you want to proceed?";
    String title = "confirmation box";

    window.initModality(
        Modality.APPLICATION_MODAL); // pop-up window won't close until you have dealt with it
    window.setTitle(title);
    window.setWidth(500);
    window.setHeight(500);

    Label label1 = new Label(message);

    Button no = new Button("no");
    no.setOnAction(e -> window.close());

    Button yes = new Button("yes");
    yes.setOnAction(e -> window.setScene(scene2));

    // Confirmation page
    Label confirmed = new Label("Your food request is confirmed! Yay!");
    StackPane layout2 = new StackPane();
    layout2.getChildren().add(confirmed);
    scene2 = new Scene(layout2, 600, 600);

    VBox layout = new VBox(10);
    layout.getChildren().addAll(label1, yes, no);
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout);
    window.setScene(scene);
    window.showAndWait();
  }
}