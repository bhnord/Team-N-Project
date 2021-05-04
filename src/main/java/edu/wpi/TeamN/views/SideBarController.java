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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SideBarController<node> extends MasterController implements Initializable {

  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader2;

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
  HomeControllerAdmin homeController = new HomeControllerAdmin();

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

    if (db.getCurrentUser() != null) {
      String a = "user: " + db.getCurrentUser().getUsername();
      // String a = "                  jjjjjjjjj";
      Label label = new Label(a);
      accountSettingsGroup.getChildren().add(label);
    } else {

      String a = "      user: guest";
      Label label = new Label(a);
      accountSettingsGroup.getChildren().add(label);
    }

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

    setLoader(loader);
    accountSettingsGroup.setTranslateX(-300);

    // accountSettingsGroup.getChildren().add(homeController.accountInfo());
    // Label label = new Label(homeController.accountInfo());
    // accountSettingsGroup.getChildren().add(label);
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
