package edu.wpi.TeamN;

import com.google.inject.*;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.DatabaseServiceProvider;
import edu.wpi.TeamN.services.database.users.UserPrefs;
import edu.wpi.TeamN.services.database.users.UserType;
import edu.wpi.TeamN.state.HomeStateProvider;
import edu.wpi.TeamN.views.FXMLLoaderProvider;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  private FXMLLoader loader;
  private Scene primaryScene;

  public static Node getPrimaryStage() {
    return null;
  }

  @Override
  public void init() {
    log.info("Starting Up");
    primaryScene = new Scene(new AnchorPane());
    Injector injector =
        Guice.createInjector(
            new DatabaseServiceProvider(),
            new HomeStateProvider(),
            new FXMLLoaderProvider(),
            new AbstractModule() {
              @Provides
              @Singleton
              public Scene providePrimaryScene() {
                return primaryScene;
              }
            });
    DatabaseService db = injector.getInstance(DatabaseService.class);
    if (db.initTables()) {
      db.loadCSVtoTable("src/main/resources/MapCSV/bwNnodes.csv", "NODES");
      db.loadCSVtoTable("src/main/resources/MapCSV/bwNedges.csv", "EDGES");
    }
    loader = new FXMLLoader();
    loader.setControllerFactory(injector::getInstance);

    if (db.getUserByUsername("admin") == null)
      db.addUser("admin", "admin", UserType.ADMINISTRATOR, new UserPrefs());
    if (db.getUserByUsername("staff") == null)
      db.addUser("staff", "staff", UserType.EMPLOYEE, new UserPrefs());
    if (db.getUserByUsername("patient") == null)
      db.addUser("patient", "patient", UserType.PATIENT, new UserPrefs());
    if (db.getUserByUsername("guest") == null)
      db.addUser("guest", "guest", UserType.PATIENT, new UserPrefs());

    if (db.getUserByUsername("Alex") == null)
      db.addUser("Alex", "Alex", UserType.EMPLOYEE, new UserPrefs());
    if (db.getUserByUsername("Ananya") == null)
      db.addUser("Ananya", "Ananya", UserType.EMPLOYEE, new UserPrefs());
    if (db.getUserByUsername("Finn") == null)
      db.addUser("Finn", "Finn", UserType.EMPLOYEE, new UserPrefs());
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("views/loginPage.fxml"));
    // TODO: uncomment above line and comment below line to switch between template and app
    // Parent root = loader.load(getClass().getResourceAsStream("views/templateJFeonix.fxml"));
    primaryStage
        .getIcons()
        .add(new Image(ClassLoader.getSystemResourceAsStream("images/hospital-256.png")));
    // primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setTitle("Team N Application");
    primaryScene.setRoot(root);
    primaryStage.setScene(primaryScene);
    primaryStage.setMinHeight(800);
    primaryStage.setMinWidth(1366);

    // primaryStage.setResizable(false);
    //    primaryStage.setAlwaysOnTop(true);

    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
