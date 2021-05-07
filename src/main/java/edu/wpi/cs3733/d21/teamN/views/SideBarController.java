package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class SideBarController extends MasterController implements Initializable {

  @Inject private FXMLLoader loader2;
  private DatabaseService db;

  // Tooltip for logout/back button that changes in Register Page
  @FXML private Tooltip ttLogOutButton;

  // Groups for buttons
  @FXML private Group groupLogOut;
  @FXML private Group groupCovid;
  @FXML private Group groupBack;
  @FXML private Group groupHome;
  @FXML private Group groupAccountSettings;
  @FXML private ImageView LogOutBack;
  @FXML private ImageView RegisterBack;

  @FXML private Group accountSettingsGroup;
  @FXML private Label AccountType;
  @FXML private Label AccountUsername;
  @FXML private Label AccountParkingSpot;

  @FXML private JFXColorPicker appColor;

  private User user;

  @FXML private AnchorPane SideAnchor;
  HomeControllerAdmin homeController;
  static boolean open = false;

  @FXML
  public void accountSettings() {

    // accountSettingsGroup.setTranslateX(100);
    TranslateTransition tt = new TranslateTransition();
    tt.setNode(accountSettingsGroup);
    tt.setDuration(new Duration(1000));
    // tt.setFromX(0);
    tt.setToX(0);
    tt.setAutoReverse(true);
    tt.play();
    open = true;
    if (db.getCurrentUser() != null) {

      user = db.getCurrentUser();
      appColor.setValue((Color.web(user.getAppColor())));

      String a = "Username: " + user.getUsername();
      AccountType.setText(a);
      // accountaSettingsGroup.getChildren().add(AccountType);
      String b = "Privileges: " + user.getType().toString();
      AccountUsername.setText(b);
      String parkingSpot = user.getParkingSpot();
      if (parkingSpot != null) {
        AccountParkingSpot.setText("Parking Spot: " + parkingSpot);
      } else {
        AccountParkingSpot.setText("Parking Spot: Not Set");
      }
      //      accountSettingsGroup.getChildren().add(AccountUsername);
    }
    //    } catch (Exception e) {
    //
    //    }
  }

  @FXML
  public void accountSettingsBack() {

    TranslateTransition tt = new TranslateTransition();
    tt.setNode(accountSettingsGroup);
    tt.setDuration(new Duration(1000));
    // tt.setFromX(100);
    tt.setToX(-300);
    tt.setAutoReverse(true);
    tt.play();
    open = false;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setLoader(loader);
    if (!open) {
      accountSettingsGroup.setTranslateX(-300);
    }

    //    if (db != null) {
    //      user = db.getCurrentUser();
    //      appColor.setValue((Color.web(user.getAppColor())));
    //      // user.setAppColor(appColor.getValue().toString());
    //      // db.updateUserPrefs(user.getId(), user.getUserPrefs());
    //    }
  }

  /**
   * ------------------------------- Setters for super MasterController
   * -------------------------------
   */
  @Override
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    super.setAppPrimaryScene(appPrimaryScene);
  }

  @Override
  public void setLoader(FXMLLoader loader) {
    super.setLoader(loader);
  }

  @Override
  public void setDB(DatabaseService db) {
    this.db = db;
    user = db.getCurrentUser();
    appColor.setValue((Color.web(user.getAppColor())));
    // String color = "-fx-background-color: " + "#" + user.getAppColor().substring(2) + ";";
    updateStyle(user.getAppColor());
  }

  /**
   * --------------------------------------------------------------------------------------------------
   */

  /** ------------------------------- Button Functionality ------------------------------- */

  // advance function for specific instances
  @Override
  public void advanceViews(ActionEvent actionEvent) throws IOException {
    super.advanceViews(actionEvent);
  }

  // advances to the service request page (for back buutton group)
  @FXML
  public void advanceServiceRequest() throws IOException {
    super.setDB(db);
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  // advances to the current users home page
  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  // logs out the current user
  @FXML
  public void logOut() throws IOException {
    super.setDB(db);
    super.logOut(loader, appPrimaryScene);
  }

  // closes the application
  @FXML
  public void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  /** ------------------------------------------------------------------------------------ */

  /**
   * ------------------------------- SideBar initialization on FXML load
   * -------------------------------
   */

  /**
   * setType
   *
   * @param type: a String to give what the type of page the sidebar needs to load on
   */
  public void setType(String type) {
    if (type.equals("Home")) {
      makeInvisible(groupBack);
      makeInvisible(groupHome);
      RegisterBack.setVisible(false);
      appColor.setVisible(true);
    } else if (type.equals("Map") || type.equals("Database")) {
      makeInvisible(groupBack);
      RegisterBack.setVisible(false);
    } else if (type.equals("Service Request")) {
      // all buttons
      RegisterBack.setVisible(false);
    } else if (type.equals("Covid Form")) {
      makeInvisible(groupCovid);
      makeInvisible(groupBack);
      RegisterBack.setVisible(false);
    } else if (type.equals("Login")) {
      makeInvisible(groupLogOut);
      makeInvisible(groupCovid);
      makeInvisible(groupBack);
      makeInvisible(groupHome);
      makeInvisible(groupAccountSettings);
      updateStyle("0x748cdc");
    } else if (type.equals("Login Map")) {
      makeInvisible(groupLogOut);
      makeInvisible(groupCovid);
      makeInvisible(groupBack);
      makeInvisible(groupAccountSettings);
    } else if (type.equals("Register")) {
      LogOutBack.setVisible(false);
      RegisterBack.setVisible(true);
      ttLogOutButton.setText("Back");
      makeInvisible(groupCovid);
      makeInvisible(groupBack);
      makeInvisible(groupHome);
      makeInvisible(groupAccountSettings);
    }
  }

  /**
   * makeInvisible
   *
   * @param group: a group to be taken out and managed in the sidebar
   */
  public void makeInvisible(Group group) {
    group.setVisible(false);
    group.setManaged(false);
  }

  /**
   * ---------------------------------------------------------------------------------------------------
   */
  @FXML Label label1, label2, label3, label4, label5, label6, labelBack, labelB1, labelB2;

  @FXML Rectangle rectangle1;

  public void updateStyle(String color) {
    String style = "-fx-background-color: " + "#" + color.substring(2) + ";";
    Label[] lA = {label1, label2, label3, label4, label5, label6, labelB1, labelB2};
    for (Label a : lA) a.setStyle(style);
    // label1.setStyle(style);
    // rectangle1.setStyle(style);
    labelB1.setStyle(style + "-fx-background-radius: 50;");

    Color appC = Color.web(color);
    String s = appC.darker().darker().darker().desaturate().toString();
    String c = "-fx-background-color: " + "#" + s.substring(2) + ";";
    labelBack.setStyle(c);
  }

  @FXML
  private void newAppColor(ActionEvent actionEvent) {
    if (db != null) {

      user = db.getCurrentUser();
      // label1.getStylesheets().remove("src/main/resources/StyleSheet/Dynamic.css");
      user.setAppColor(appColor.getValue().toString());
      db.updateUserPrefs(user.getId(), user.getUserPrefs());

      updateStyle(user.getAppColor());

      super.advanceHome(loader, appPrimaryScene);

      // label1.setStyle("-fx-background-color: " + color + "; ");
      // label1.getStylesheets().add("src/main/resources/StyleSheet/Dynamic.css");
    }
  }
}
