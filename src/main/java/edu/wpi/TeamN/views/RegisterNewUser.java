package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import edu.wpi.TeamN.services.database.users.UserPrefs;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterNewUser extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  @FXML private Label errorLabel;
  private Label Employee;
  private Label Patient;
  private Label Admin;
  @FXML private JFXTextField username;
  @FXML private JFXPasswordField password;
  @FXML private JFXPasswordField retypePassword;
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  @FXML private Button submit;
  @FXML private StackPane myStackPane2;
  private Scene appPrimaryScene;
  private HashMap<String, User> users;
  private HashMap<String, Node> rooms;

  @FXML private AnchorPane anchorPage;
  @FXML private AnchorPane anchorPane;

  @FXML private StackPane confirmationStackPane;
  static Stage stage;

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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Service Request");

    // submit.setDisable(true);

    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    /*reqInputValid.setMessage("Cannot be empty");
    username.getValidators().add(reqInputValid);
    username
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) username.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    password.getValidators().add(reqInputValid);
    password
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) password.validate();
            });

    reqInputValid.setMessage("Cannot be empty");
    retypePassword.getValidators().add(reqInputValid);
    retypePassword
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) retypePassword.validate();
            });*/
  }

  public void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  public void advanceHome() throws IOException {
    advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void back() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException, InterruptedException {

    if (username.getText().isEmpty()
        || password.getText().isEmpty()
        || retypePassword.getText().isEmpty()) {
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
      actionEvent.consume();
      close.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              dialog.close();
              helpButton.setDisable(false);
              submit.setDisable(false);
            }
          });
      helpButton.setDisable(true);
      submit.setDisable(true);
      dialog.show();

    } else if (!(password.getText().equals(retypePassword.getText()))) {
      String title = "Invalid Entry";
      JFXDialogLayout dialogContent = new JFXDialogLayout();
      dialogContent.setHeading(new Text(title));
      dialogContent.setBody((new Text("* Passwords do not match. Retype passwords. \n")));
      JFXButton close = new JFXButton("close");
      close.setButtonType(JFXButton.ButtonType.RAISED);
      close.setStyle("-fx-background-color : #00bfff;");
      dialogContent.setActions(close);

      JFXDialog dialog =
          new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
      actionEvent.consume();
      close.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              dialog.close();
              helpButton.setDisable(false);
            }
          });
      helpButton.setDisable(true);
      dialog.show();
    } else if (!(db.addUser(
        username.getText(), password.getText(), UserType.PATIENT, new UserPrefs()))) {
      String title = "Invalid User Login";
      JFXDialogLayout dialogContent = new JFXDialogLayout();
      dialogContent.setHeading(new Text(title));
      dialogContent.setBody((new Text("* Username already exists. Choose different username.\n")));
      JFXButton close = new JFXButton("close");
      close.setButtonType(JFXButton.ButtonType.RAISED);
      close.setStyle("-fx-background-color : #00bfff;");
      dialogContent.setActions(close);

      JFXDialog dialog =
          new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
      actionEvent.consume();
      close.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              dialog.close();
              helpButton.setDisable(false);
            }
          });
      helpButton.setDisable(true);
      dialog.show();
    } else {

      VBox manuContainer = new VBox();
      Label lbl1 = new Label("Are you sure the information you have provided is correct?");

      JFXButton continueButton = new JFXButton("Continue");
      continueButton.setButtonType(JFXButton.ButtonType.RAISED);
      continueButton.setStyle("-fx-background-color : #00bfff;");

      JFXButton cancelButton = new JFXButton("Cancel");
      cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
      cancelButton.setStyle("-fx-background-color : #00bfff;");

      cancelButton.setTranslateX(100);
      cancelButton.setTranslateY(65);

      continueButton.setTranslateX(200);
      continueButton.setTranslateY(25);

      manuContainer.getChildren().addAll(lbl1, cancelButton, continueButton);
      manuContainer.setPadding(new Insets(30, 50, 50, 50));
      manuContainer.setSpacing(10);
      JFXPopup popup1 = new JFXPopup(manuContainer);
      actionEvent.consume();
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

              JFXButton continueButton = new JFXButton("Return To Login Screen");
              continueButton.setButtonType(JFXButton.ButtonType.RAISED);
              continueButton.setStyle("-fx-background-color : #00bfff;");

              //         continueButton.setTranslateX(350);
              //         continueButton.setTranslateY(25);

              manuContainer.getChildren().addAll(lbl1, continueButton);
              manuContainer.setPadding(new Insets(30, 50, 50, 50));
              manuContainer.setSpacing(10);
              JFXPopup popup1 = new JFXPopup(manuContainer);
              actionEvent.consume();
              popup1.setAutoHide(false);

              continueButton.setOnAction(
                  new EventHandler<ActionEvent>() {
                    @SneakyThrows
                    @Override
                    public void handle(ActionEvent event) {
                      anchorPage.setEffect(null);
                      // txtEmployeeName.setEffect(null);
                      popup1.hide();
                      logOut();
                      submit.setDisable(false);
                    }
                  });
              submit.setDisable(true);
              anchorPage.setEffect(blur);
              //   txtEmployeeName.setEffect(blur);
              popup1.show(
                  myStackPane, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
              submit.setDisable(false);
            }
          });
      submit.setDisable(true);
      popup1.show(myStackPane2, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
    }
  }

  public void help(ActionEvent actionEvent) throws IOException, InterruptedException {
    String title = "Help Page";
    JFXDialogLayout dialogContent = new JFXDialogLayout();
    dialogContent.setHeading(new Text(title));
    dialogContent.setBody(
        (new Text(
            "* 'Select Login Type' refers to the type of user '\n"
                + "* 'Enter Username' refers to the username you will use everytime you login to the application\n"
                + "* 'Enter Password' refers to your unique password that you will use everytime you login to the application\n"
                + "* 'Retype Password' helps authenticate your password to ensure a secure account\n")));
    JFXButton close = new JFXButton("close");
    close.setButtonType(JFXButton.ButtonType.RAISED);
    close.setStyle("-fx-background-color : #00bfff;");
    dialogContent.setActions(close);

    JFXDialog dialog = new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    actionEvent.consume();
    close.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dialog.close();
            helpButton.setDisable(false);
          }
        });
    helpButton.setDisable(true);
    dialog.show();
  }
}
