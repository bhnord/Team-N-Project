package edu.wpi.cs3733.d21.teamN.templateJFoenix;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.validation.RequiredFieldValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class templateJFoenix implements Initializable {
  @FXML private JFXHamburger ham1;

  @FXML private JFXTextField txtUsername;

  @FXML private JFXPasswordField txtPassword;

  @FXML private JFXProgressBar jfxProgressBar;

  @FXML private JFXButton prgStart;

  @FXML private AnchorPane anchorPane;

  @FXML private JFXCheckBox checkbox1;

  @FXML private JFXComboBox<String> combobox;

  public void initialize(URL url, ResourceBundle rb) {
    /** Hamburger ICON for menus, goes from hamburger to x back to hamburger* */
    HamburgerBasicCloseTransition basicClose = new HamburgerBasicCloseTransition(ham1);
    basicClose.setRate(-1);
    ham1.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          basicClose.setRate(basicClose.getRate() * -1);
          basicClose.play();
        });
    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    txtUsername.getValidators().add(reqInputValid);
    txtUsername
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtUsername.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    txtPassword.getValidators().add(reqInputValid);
    txtPassword
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtPassword.validate();
            });

    /** ComboBox* */
    combobox.getItems().add("Option1");
    combobox.getItems().add("Option2");
    combobox.getItems().add("Option3");
    combobox.getItems().add("Option4");
  }

  /** Progress Bar w/ button* */
  @FXML
  private void handleEventButton(ActionEvent ae) {
    /*JFXProgressBar jfxBar = new JFXProgressBar();
    jfxBar.setPrefWidth(500);
    JFXProgressBar jfxBarInf = new JFXProgressBar();
    jfxBarInf.setPrefWidth(500);
    jfxBarInf.setProgress(-1.0f);
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(jfxProgressBar.progressProperty(), 0),
                new KeyValue(jfxProgressBar.progressProperty(), 0)),
            new KeyFrame(
                Duration.seconds(2),
                new KeyValue(jfxProgressBar.progressProperty(), 1),
                new KeyValue(jfxBar.progressProperty(), 1)));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();*/
    Task task = taskWorker(100);
    jfxProgressBar.progressProperty().unbind();
    jfxProgressBar.progressProperty().bind(task.progressProperty());

    // can change stage.close to go to different primary scene
    task.setOnSucceeded(
        e -> {
          Stage stage = (Stage) anchorPane.getScene().getWindow();
          stage.close();
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
