package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.*;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CovidForm extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  private Scene appPrimaryScene;
  @FXML private JFXButton submit = new JFXButton();
  @FXML private AnchorPane anchorPage;
  @FXML private StackPane myStackPane;

  /**
   * This method allows the tests to inject the scene at a later time, since it must be done on the
   * JavaFX thread
   *
   * @param appPrimaryScene Primary scene of the app whose root will be changed
   */
  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @FXML JFXComboBox<String> comboBox = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox2 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox3 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox4 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox6 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox5 = new JFXComboBox<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // submit.setDisable(true);
    comboBox.getItems().add("yes");
    comboBox.getItems().add("no");

    comboBox2.getItems().add("yes");
    comboBox2.getItems().add("no");

    comboBox3.getItems().add("yes");
    comboBox3.getItems().add("no");

    comboBox4.getItems().add("yes");
    comboBox4.getItems().add("no");

    comboBox5.getItems().add("yes");
    comboBox5.getItems().add("no");

    comboBox6.getItems().add("yes");
    comboBox6.getItems().add("no");
  }

  @FXML
  public void advanceHome() throws IOException {
    advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  public void back() throws IOException {
    advanceHome(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void goToRequestPage(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    /* Parent root = childLoader.load(getClass().getResourceAsStream("ConfirmationPageCovid.fxml"));
    ChildAppPrimaryScene.setRoot(root);*/

    if ((comboBox.getValue() == null
        || comboBox2.getValue() == null
        || comboBox3.getValue() == null
        || comboBox4.getValue() == null
        || comboBox5.getValue() == null
        || comboBox6.getValue() == null)) {
      String title = "Missing Fields";
      JFXDialogLayout dialogContent = new JFXDialogLayout();
      dialogContent.setHeading(new Text(title));
      dialogContent.setBody(
          (new Text("* You must fill out all required fields of the request to continue\n")));
      JFXButton close = new JFXButton("close");
      close.setButtonType(JFXButton.ButtonType.RAISED);
      close.setStyle("-fx-background-color : #00bfff;");
      dialogContent.setActions(close);

      JFXDialog dialog =
          new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
      // actionEvent.consume();
      close.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              dialog.close();
              //  helpButton.setDisable(false);
            }
          });
      // helpButton.setDisable(true);
      dialog.show();

    } else {

      VBox menuContainer = new VBox();
      Label lbl1 = new Label("Are you sure the information you have provided is correct?");

      JFXButton continueButton = new JFXButton("Continue");
      continueButton.setButtonType(JFXButton.ButtonType.RAISED);
      continueButton.setStyle("-fx-background-color : #00bfff");

      JFXButton cancelButton = new JFXButton("Cancel");
      cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
      cancelButton.setStyle("-fx-background-color : #00bfff");

      cancelButton.setTranslateX(100);
      cancelButton.setTranslateY(65);

      continueButton.setTranslateX(200);
      continueButton.setTranslateY(25);

      menuContainer.getChildren().addAll(lbl1, cancelButton, continueButton);
      menuContainer.setPadding(new Insets(30, 50, 50, 50));
      menuContainer.setSpacing(10);
      JFXPopup popup1 = new JFXPopup(menuContainer);
      // actionEvent.consume();
      popup1.setAutoHide(false);

      cancelButton.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              popup1.hide();
              submit.setDisable(false);
            }
          });

      continueButton.setOnAction(
          new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
              popup1.hide();

              BoxBlur blur = new BoxBlur(7, 7, 7);

              VBox manuContainer = new VBox();
              Label lbl1 =
                  new Label(
                      "Your request Has been submitted!                                                          ");

              JFXButton continueButton = new JFXButton("Return To Home");
              continueButton.setButtonType(JFXButton.ButtonType.RAISED);
              continueButton.setStyle("-fx-background-color : #00bfff;");

              JFXButton cancelButton = new JFXButton("Complete Another Request");
              cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
              cancelButton.setStyle("-fx-background-color : #00bfff;");

              cancelButton.setTranslateX(0);
              cancelButton.setTranslateY(65);

              continueButton.setTranslateX(350);
              continueButton.setTranslateY(25);

              manuContainer.getChildren().addAll(lbl1, cancelButton, continueButton);
              manuContainer.setPadding(new Insets(30, 50, 50, 50));
              manuContainer.setSpacing(10);
              JFXPopup popup1 = new JFXPopup(manuContainer);
              // actionEvent.consume();
              popup1.setAutoHide(false);

              // return to request page
              cancelButton.setOnAction(
                  new EventHandler<ActionEvent>() {
                    @SneakyThrows
                    @Override
                    public void handle(ActionEvent event) {
                      popup1.hide();
                      submit.setDisable(false);
                      back();
                    }
                  });

              // go back to home page
              continueButton.setOnAction(
                  new EventHandler<ActionEvent>() {
                    @SneakyThrows
                    @Override
                    public void handle(ActionEvent event) {
                      anchorPage.setEffect(null);
                      // txtEmployeeName.setEffect(null);
                      popup1.hide();
                      advanceHome();
                      //   submit.setDisable(false);
                    }
                  });
              // submit.setDisable(true);
              anchorPage.setEffect(blur);
              //  txtEmployeeName.setEffect(blur);
              popup1.show(
                  myStackPane, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
              // submit.setDisable(false);
            }
          });
      // submit.setDisable(true);
      popup1.show(myStackPane, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
    }
  }

  @FXML
  public void continuePage(ActionEvent actionEvent) throws IOException {
    goToRequestPage(loader, appPrimaryScene);
  }

  @FXML
  private void validateButton() {
    /* if (comboBox.getValue() == null
        || comboBox2.getValue() == null
        || comboBox3.getValue() == null
        || comboBox4.getValue() == null
        || comboBox5.getValue() == null
        || comboBox6.getValue() == null) {
      submit.setDisable(true);
    } else {
      submit.setDisable(false);
    }*/
  }
}
