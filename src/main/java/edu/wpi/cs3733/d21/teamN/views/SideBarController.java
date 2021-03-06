package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.d21.teamN.faceLogin.FaceEnroller;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserType;
import edu.wpi.cs3733.d21.teamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SideBarController extends MasterController implements Initializable {

  @Inject private FXMLLoader loader2;
  private DatabaseService db;

  // Tooltip for logout/back button that changes in Register Page
  @FXML private Tooltip ttLogOutButton;

  @FXML private JFXButton switchDB;

  // Groups for buttons
  @FXML
  private Group groupLogOut,
      groupCovid,
      groupBack,
      groupBack1,
      groupHome,
      groupAccountSettings,
      groupAboutUs,
      covidInfo;
  @FXML private ImageView LogOutBack;
  @FXML private ImageView RegisterBack;

  @FXML private Group accountSettingsGroup;
  @FXML private Label AccountType;
  @FXML private Label AccountUsername;
  @FXML private Label AccountParkingSpot;

  @FXML private JFXColorPicker appColor;
  @FXML private JFXToggleButton darkModeSwitch;
  @FXML private Rectangle settingsRec;

  private User user;
  private FaceEnroller faceEnroller;

  @FXML private AnchorPane SideAnchor;
  //  HomeControllerAdmin homeController;
  private static boolean open = false;
  public static boolean darkOn = false;

  @FXML
  public void accountSettings() {

    // accountSettingsGroup.setTranslateX(100);
    TranslateTransition tt = new TranslateTransition();
    tt.setNode(accountSettingsGroup);
    tt.setDuration(new Duration(500));
    // tt.setFromX(0);
    tt.setToX(0);
    tt.setAutoReverse(true);
    tt.play();
    open = true;
    if (db.getCurrentUser() != null) {

      user = db.getCurrentUser();
      appColor.setValue((Color.web(user.getAppColor())));

      darkModeSwitch.setSelected(user.getDarkMode());

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
  }

  @FXML
  public void accountSettingsBack() {

    TranslateTransition tt = new TranslateTransition();
    tt.setNode(accountSettingsGroup);
    tt.setDuration(new Duration(500));
    // tt.setFromX(100);
    tt.setToX(-370);
    tt.setAutoReverse(true);
    tt.play();
    open = false;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setLoader(loader);
    if (!open) {
      accountSettingsGroup.setTranslateX(-370);
    }

    darkModeSwitch.setSelected(darkOn);
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
    if (!user.getUsername().equals("guest")) {
      if (user.getAppColor().equals("#ffffff")) {
        updateStyle("0x748cdc");
      } else {
        updateStyle(user.getAppColor());
      }
    } else {
      updateStyle("0x748cdc");
    }
    if (open) {
      accountSettings();
    }
  }

  /**
   * --------------------------------------------------------------------------------------------------
   */

  /** ------------------------------- Button Functionality ------------------------------- */

  // advance function for specific instances
  @Override
  public void advanceViews(ActionEvent actionEvent) throws IOException {
    open = false;
    super.advanceViews(actionEvent);
  }

  @FXML
  public void advanceEditorPage() throws IOException {
    super.setDB(db);
    Parent root = loader.load(getClass().getResourceAsStream("EditorPage.fxml"));
    appPrimaryScene.setRoot(root);
  }

  // advances to the service request page (for back buutton group)
  @FXML
  public void advanceServiceRequest() throws IOException {
    super.setDB(db);
    open = false;
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  // advances to the current users home page
  @FXML
  public void advanceHome() throws IOException {
    open = false;
    if (faceEnroller != null) {
      faceEnroller.releaseCamera();
      faceEnroller = null;
    }
    super.advanceHome(loader, appPrimaryScene);
  }

  // logs out the current user
  @FXML
  public void logOut() throws IOException {
    super.setDB(db);
    open = false;
    super.logOut(loader, appPrimaryScene);
  }

  // closes the application
  @FXML
  public void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void faceRec(ActionEvent actionEvent) throws IOException {
    accountSettingsGroup.setTranslateX(-370);
    open = !open;
    Parent root = loader.load(getClass().getResourceAsStream("FacialRecognitionAdd.fxml"));
    open = false;
    appPrimaryScene.setRoot(root);
  }

  public void aboutUs(ActionEvent actionEvent) throws IOException {
    // accountSettingsGroup.setTranslateX(0);
    if (faceEnroller != null) {
      faceEnroller.releaseCamera();
      faceEnroller = null;
    }
    Parent root = loader.load(getClass().getResourceAsStream("AboutUs.fxml"));
    // open = false;
    appPrimaryScene.setRoot(root);
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
    makeInvisible(groupBack1);
    if (db.getCurrentUser().getType().equals(UserType.ADMINISTRATOR)) {
      switchDB.setVisible(true);
    }
    switch (type) {
      case "Home":
        makeInvisible(groupBack);
        makeInvisible(groupHome);
        RegisterBack.setVisible(false);
        appColor.setVisible(true);
        break;
      case "Map":
      case "Database":
        makeInvisible(groupBack);
        RegisterBack.setVisible(false);
        makeInvisible(groupAccountSettings);
        break;
      case "Service Request":
        // all buttons
        RegisterBack.setVisible(false);
        break;
      case "Covid Form":
        makeInvisible(groupCovid);
        makeInvisible(groupBack);
        makeInvisible(groupAccountSettings);
        RegisterBack.setVisible(false);
        break;
      case "Login":
        makeInvisible(groupLogOut);
        makeInvisible(groupCovid);
        makeInvisible(groupBack);
        makeInvisible(groupHome);
        makeInvisible(groupAccountSettings);
        updateStyle("0x748cdc");
        break;
      case "Login Map":
        makeInvisible(groupLogOut);
        makeInvisible(groupCovid);
        makeInvisible(groupBack);
        makeInvisible(groupAccountSettings);
        break;
      case "Register":
        LogOutBack.setVisible(false);
        RegisterBack.setVisible(true);
        ttLogOutButton.setText("Back");
        makeInvisible(groupCovid);
        makeInvisible(groupBack);
        makeInvisible(groupHome);
        makeInvisible(groupAccountSettings);
        break;
      case "FaceRec":
        makeInvisible(groupCovid);
        makeInvisible(groupBack);
        makeInvisible(groupLogOut);
        makeInvisible(groupAccountSettings);
        break;
      case "About Us":
        makeInvisible(groupLogOut);
        makeInvisible(groupCovid);
        makeInvisible(groupBack);
        makeInvisible(groupAccountSettings);
        makeInvisible(groupAboutUs);
        break;
      case "CovidInformation":
        makeInvisible(groupLogOut);
        makeInvisible(groupCovid);
        makeInvisible(groupBack);
        makeInvisible(groupAccountSettings);
        makeInvisible(groupAboutUs);
        makeInvisible(covidInfo);
        break;
      case "Editor":
        makeInvisible(groupHome);
        makeInvisible(groupBack);
        RegisterBack.setVisible(false);
        makeInvisible(groupAccountSettings);
        groupBack1.setVisible(true);
        groupBack1.setManaged(true);
        break;
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
  @FXML
  Label label1,
      label2,
      label3,
      label4,
      label5,
      label6,
      label7,
      label8,
      label9,
      labelBack,
      labelB1,
      labelB2;

  @FXML Rectangle rectangle1;
  @FXML JFXButton faceRec;

  public void updateStyle(String color) {
    String style = "-fx-background-color: " + "#" + color.substring(2) + ";";
    Label[] lA = {
      label1, label2, label3, label4, label5, label6, label7, label8, label9, labelB1, labelB2
    };
    for (Label a : lA) a.setStyle(style);
    // label1.setStyle(style);
    // rectangle1.setStyle(style);
    labelB1.setStyle(style + "-fx-background-radius: 50;");
    faceRec.setStyle(style);
    switchDB.setStyle(style);

    Color appC = Color.web(color);
    String s = appC.darker().darker().darker().desaturate().toString();
    String c = "-fx-background-color: " + "#" + s.substring(2) + ";";
    labelBack.setStyle(c);
  }

  @FXML
  private void newAppDarkMode(ActionEvent actionEvent) {

    if (db != null) {
      user.setDarkMode(darkModeSwitch.isSelected());
      db.updateUserPrefs(user.getId(), user.getUserPrefs());
      darkOn = !darkOn;
    }

    super.advanceHome(loader, appPrimaryScene);
  }

  public void setFaceEnroller(FaceEnroller faceEnroller) {
    this.faceEnroller = faceEnroller;
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

  public void covidinformation(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("CovidInformation.fxml"));
    // open = false;
    appPrimaryScene.setRoot(root);
  }

  @FXML
  private void switchDB() throws IOException {
    DialogFactory dialogFactory = new DialogFactory(SideAnchor);
    try {
      logOut();
    } catch (IOException e) {
      e.printStackTrace();
    }
    db.switchConnection();
  }
}
