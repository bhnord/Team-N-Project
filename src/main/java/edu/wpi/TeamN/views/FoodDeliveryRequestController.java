package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.requests.Request;
import edu.wpi.TeamN.services.database.requests.RequestType;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.AutoCompleteComboBoxListener;
import edu.wpi.TeamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FoodDeliveryRequestController extends MasterController implements Initializable {
  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
  @Inject private HomeState state;
  @FXML private JFXTimePicker txtTimeOfRequest;
  @FXML private JFXTextField txtComments;
  @FXML private JFXComboBox<Label> mainDish;
  @FXML private JFXComboBox<Label> sideDish;
  @FXML private JFXComboBox<Label> drinkDish;
  @FXML private JFXComboBox<Label> txtEmployeeName;
  @FXML private JFXComboBox<Label> roomDropdown;
  @FXML private StackPane rootStackPane;
  private DialogFactory dialogFactory;
  private Scene appPrimaryScene;

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
    dialogFactory = new DialogFactory(rootStackPane);
    loadEmployeeDropdown(txtEmployeeName);
    loadRoomDropdown(roomDropdown);
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
    super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void back() throws IOException {
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  public void Submit(ActionEvent actionEvent) throws IOException {
    if (validateInputs()) {
      dialogFactory.creatDialogOkay(
          "Missing Fields", "You must fill out all required fields of the request to continue\n");
    } else {
      dialogFactory.creatDialogConfirmCancel(
          "Are you sure the information you have provided is correct?", "", mouse -> submitToDB());
    }
  }

  public void help(ActionEvent actionEvent) throws IOException {
    dialogFactory.creatDialogOkay(
        "Help",
        "- Employee Name refers to the employee being requested to complete the job\n - Room is the room that the employee will deliver the food to\n - Time of request refers to time the food should be delivered to the requester\n - Necessary Equipment refers to additional services/equipment the requester requires\n");
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

  private void submitToDB() {
    Request r =
        new Request(
            RequestType.FOOD_DELIVERY,
            Integer.parseInt(txtEmployeeName.getSelectionModel().getSelectedItem().getId()),
            roomDropdown.getSelectionModel().getSelectedItem().getId(),
            " MainDish: "
                + mainDish.getValue().getText()
                + ", Side Dish: "
                + sideDish.getValue().getText()
                + ", Drink: "
                + drinkDish.getValue().getText(),
            txtComments.getText());
    db.addRequest(r);
  }

  private boolean validateInputs() {
    return (mainDish.getValue() == null
        || sideDish.getValue() == null
        || drinkDish.getValue() == null
        || txtEmployeeName.getValue() == null
        || roomDropdown.getValue() == null
        || txtTimeOfRequest.getEditor().getText().isEmpty());
  }
}
