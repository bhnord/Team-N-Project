package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.state.Login;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

@Slf4j
public class FoodDeliveryRequestController extends masterController implements Initializable {
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private JFXTextField txtTimeOfRequest;
  @FXML private JFXTextField txtComments;
  @FXML private JFXComboBox<Label> mainDish = new JFXComboBox<>();
  @FXML private JFXComboBox<Label> sideDish = new JFXComboBox<>();
  @FXML private JFXComboBox<Label> drinkDish = new JFXComboBox<>();
  @FXML private Button helpButton;
  @FXML private StackPane myStackPane;
  @FXML private Button submit;
  @FXML private StackPane myStackPane2;
  private Scene appPrimaryScene;
  private HashMap<String, User> users;
  private HashMap<String, Node> rooms;
  @FXML private JFXComboBox<Label> txtEmployeeName = new JFXComboBox<>();
  @FXML private JFXComboBox<Label> roomDropdown = new JFXComboBox<>();
  // @FXML private AnchorPane anchorPage;
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
    submit.setDisable(true);

    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    txtTimeOfRequest.getValidators().add(reqInputValid);
    txtTimeOfRequest
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtTimeOfRequest.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    txtComments.getValidators().add(reqInputValid);
    txtComments
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtComments.validate();
            });
    reqInputValid.setMessage("Cannot be empty");

    loadEmployeeDropdown();
    loadRoomDropdown();
    loadDishes();
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

    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    }
  }

  @FXML
  public void back() throws IOException {

    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceServiceRequestPatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceServiceRequestEmployee(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceServiceRequestAdmin(loader, appPrimaryScene);
    }
  }

  public void Submit(ActionEvent actionEvent) throws IOException {

    txtEmployeeName.setValidators();
    if (txtEmployeeName.getSelectionModel().isEmpty() || roomDropdown.getSelectionModel().isEmpty())
      return;

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
            Parent root = loader.load(getClass().getResourceAsStream("ConfirmationPageFood.fxml"));
            appPrimaryScene.setRoot(root);
            submit.setDisable(false);
          }
        });
    submit.setDisable(true);
    popup1.show(myStackPane2, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
  }

  @FXML
  private void validateButton() {
    if (!txtTimeOfRequest.getText().isEmpty() && !txtComments.getText().isEmpty()) {
      submit.setDisable(false);
    } else {
      submit.setDisable(true);
    }
  }

  public void help(ActionEvent actionEvent) throws IOException {
    String title = "Help Page";
    BoxBlur blur = new BoxBlur(3, 3, 3);
    JFXDialogLayout dialogContent = new JFXDialogLayout();
    dialogContent.setHeading(new Text(title));
    dialogContent.setBody(
        (new Text(
            "* Employee Name refers to the employee being requested to complete the job\n"
                + "* Room is the room that the employee will deliver the food to\n"
                + "* Time of request refers to time the food should be delivered to the requester\n"
                + "* Necessary Equipment refers to additional services/equipment the requester requires\n")));
    JFXButton close = new JFXButton("close");
    close.setButtonType(JFXButton.ButtonType.RAISED);
    close.setStyle("-fx-background-color : #00bfff;");
    dialogContent.setActions(close);

    JFXDialog dialog = new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    close.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            // anchorPage.setEffect(null);
            dialog.close();
            helpButton.setDisable(false);
          }
        });
    helpButton.setDisable(true);
    dialog.show();
    // anchorPage.setEffect(blur);
  }

  private void loadEmployeeDropdown() {
    users = db.getUsersByType(UserType.EMPLOYEE);
    for (User user : users.values()) {
      Label lbl = new Label(user.getUsername());
      lbl.setId(user.getId());
      txtEmployeeName.getItems().add((lbl));
    }
    new AutoCompleteComboBoxListener(txtEmployeeName);
  }

  private void loadRoomDropdown() {
    rooms = db.getAllNodesMap();
    for (Node node : rooms.values()) {
      Label lbl = new Label(node.get_longName());
      lbl.setId(node.get_nodeID());
      roomDropdown.getItems().add(lbl);
    }
    new AutoCompleteComboBoxListener(roomDropdown);
  }

  private void loadDishes() {
    String[] mainDishes = {"Beef Stew", "Cheese Pizza", "Salad"};
    String[] sideDishes = {"Apple slices", "Vanilla Pudding", "side Salad"};
    String[] drinks = {"Apple Juice", "Orange Juice", "Water"};

    for (String dish : mainDishes) {
      Label lbl = new Label(dish);
      lbl.setId(dish);
      mainDish.getItems().add(lbl);
      new AutoCompleteComboBoxListener(mainDish);
    }
    for (String dish : sideDishes) {
      Label lbl = new Label(dish);
      lbl.setId(dish);
      sideDish.getItems().add(lbl);
      new AutoCompleteComboBoxListener(sideDish);
    }
    for (String drink : drinks) {
      Label lbl = new Label(drink);
      lbl.setId(drink);
      drinkDish.getItems().add(lbl);
      new AutoCompleteComboBoxListener(drinkDish);
    }
  }
}
