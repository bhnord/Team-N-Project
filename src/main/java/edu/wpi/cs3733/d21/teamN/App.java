package edu.wpi.cs3733.d21.teamN;

import com.google.inject.*;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.Appointment;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseServiceProvider;
import edu.wpi.cs3733.d21.teamN.services.database.NamedForm;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserPrefs;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserType;
import edu.wpi.cs3733.d21.teamN.state.HomeStateProvider;
import edu.wpi.cs3733.d21.teamN.views.FXMLLoaderProvider;
import java.io.IOException;
import java.sql.Timestamp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nu.pattern.OpenCV;

@Slf4j
public class App extends Application {

  private FXMLLoader loader;
  private Scene primaryScene;
  @Inject DatabaseService db;

  public static Node getPrimaryStage() {
    return null;
  }

  @Override
  public void init() {
    log.info("Starting Up");

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
    primaryScene = new Scene(new AnchorPane());
    if (db.initTables()) {
      db.loadCSVtoTable("src/main/resources/MapCSV/NODES.csv", "NODES");
      db.loadCSVtoTable("src/main/resources/MapCSV/EDGES.csv", "EDGES");
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

    db.addForm(new NamedForm("BigBoyForm", new Form()));
    db.getForm(1).getForm().tester();
    db.addAppointmentType("NewType", 1);
    db.addAppointment(
        new Appointment(
            1, 1, 1, new Form(), new Timestamp(System.currentTimeMillis()), "CSERV001L1"));
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    Parent root = loader.load(getClass().getResourceAsStream("views/HomeViewAdmin.fxml"));
    // Parent root = loader.load(getClass().getResourceAsStream("views/FacialRecTest.fxml"));
    primaryStage
        .getIcons()
        .add(new Image(ClassLoader.getSystemResourceAsStream("images/hospital-256.png")));

    // primaryStage.initStyle(StageStyle.UNDECORATED);

    primaryStage.setTitle("Team N Application");
    primaryScene.setRoot(root);
    primaryStage.setScene(primaryScene);
    primaryStage.setMinHeight(800);
    primaryStage.setMinWidth(1366);
    OpenCV.loadLocally();

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
