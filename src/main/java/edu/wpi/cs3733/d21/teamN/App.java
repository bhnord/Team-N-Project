package edu.wpi.cs3733.d21.teamN;

import com.google.inject.*;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseServiceProvider;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserType;
import edu.wpi.cs3733.d21.teamN.state.HomeStateProvider;
import edu.wpi.cs3733.d21.teamN.views.FXMLLoaderProvider;
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

import java.io.IOException;
import java.util.Objects;

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
      db.loadCSVtoTable("/MapCSV/NODES.csv", "NODES");
      db.loadCSVtoTable("/MapCSV/EDGES.csv", "EDGES");
    }
    loader = new FXMLLoader();
    loader.setControllerFactory(injector::getInstance);

    if (db.getUserByUsername("admin") == null) db.addUser("admin", "admin", UserType.ADMINISTRATOR);
    if (db.getUserByUsername("staff") == null) db.addUser("staff", "staff", UserType.EMPLOYEE);
    if (db.getUserByUsername("patient") == null) db.addUser("patient", "patient", UserType.PATIENT);
    if (db.getUserByUsername("guest") == null) db.addUser("guest", "guest", UserType.PATIENT);

    String arr[] = {
      "Alex", "Ananya", "Bernhard", "Finn", "Jake", "John", "Josh", "Michael", "Payton", "Romish"
    };
    for (String str : arr)
      if (db.getUserByUsername(str) == null) db.addUser(str, str, UserType.EMPLOYEE);

    //    db.addForm(new NamedForm(1, "BigBoyForm", new Form("BigBoyForm")));
    //    int id = db.getFormByName("BigBoyForm").getId();
    //    db.addAppointmentType("NewType", id);
    //    db.addAppointment(
    //        new Appointment(
    //            1, 1, 1, new Form(), new Timestamp(System.currentTimeMillis()), "CSERV001L1"));
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    Parent root = loader.load(getClass().getResourceAsStream("views/HomeViewAdmin.fxml"));
    // Parent root = loader.load(getClass().getResourceAsStream("views/FacialRecTest.fxml"));
    primaryStage
        .getIcons()
        .add(
            new Image(
                Objects.requireNonNull(
                    ClassLoader.getSystemResourceAsStream("images/hospital-256.png"))));

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
