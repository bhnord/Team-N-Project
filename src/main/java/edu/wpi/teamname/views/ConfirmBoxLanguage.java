package edu.wpi.teamname.views;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBoxLanguage extends masterController implements Initializable {
  static Stage stage;
  static ConfirmBoxLanguage box;
  static LanguageInterpretersRequestController language = new LanguageInterpretersRequestController();

  public static void confirm(LanguageInterpretersRequestController languageType) throws IOException {

    language = languageType;
    String title = "Language Interpreters confirmation box";

    FXMLLoader fxmlLoader =
        new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmBoxLanguage.fxml"));
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
        new FXMLLoader(ConfirmBoxFood.class.getResource("ConfirmationPagelanguage.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box.stage.setScene(scene);
  }

  @FXML
  public void returnToHomeLanguage() throws IOException {
    box.stage.close();
    language.advanceHome();
  }
}
