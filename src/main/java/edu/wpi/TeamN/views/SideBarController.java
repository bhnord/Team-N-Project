package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import edu.wpi.TeamN.services.database.DatabaseService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.util.Duration;

public class SideBarController<node> extends MasterController implements Initializable {

  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
  // Tooltip for logout/back button that changes in Register Page
  @FXML private Tooltip ttLogOutButton;

  // Groups for buttons
  @FXML private Group groupLogOut;
  @FXML private Group groupCovid;
  @FXML private Group groupBack;
  @FXML private Group groupHome;
  @FXML private ImageView LogOutBack;
  @FXML private ImageView RegisterBack;

  @FXML private Group accountSettingsGroup;
  @FXML private Label AccountType;
  @FXML private Label AccountUsername;

  @FXML private AnchorPane SideAnchor;

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
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setDB(db);
    accountSettingsGroup.setTranslateX(-300);

    // ccountUsername.setLabelFor();
    //     if (db.getCurrentUser().getUsername() != null) {
    // String a = "label: " + db.getCurrentUser().getUsername();
    String a = "ccccccccccccccccccccccccccc";
    Label label = new Label(a);
    // accountSettingsGroup.getChildren().add(label);
    // }
    // AccountType.setText(db.getCurrentUser().getUsername());
    // AccountType.setText("hello");

    // if (db.getCurrentUser() != null) {
    //  a = new Label(db.getCurrentUser().getUsername());
    // }

    // accountSettingsGroup.getChildren().add(a);
    // accountSettingsGroup.get
    // accountSettingsGroup
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
    super.setDB(db);
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
    super.logOut(loader, appPrimaryScene);
  }

  // closes the application
  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
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
    } else if (type.equals("Login Map")) {
      makeInvisible(groupLogOut);
      makeInvisible(groupCovid);
      makeInvisible(groupBack);
    } else if (type.equals("Register")) {
      LogOutBack.setVisible(false);
      RegisterBack.setVisible(true);
      ttLogOutButton.setText("Back");
      makeInvisible(groupCovid);
      makeInvisible(groupBack);
      makeInvisible(groupHome);
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
}
