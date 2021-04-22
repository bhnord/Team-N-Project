package edu.wpi.TeamN;

import com.google.inject.*;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.DatabaseServiceProvider;
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
    // db.initTables();
    loader = new FXMLLoader();
    loader.setControllerFactory(injector::getInstance);
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
    primaryStage.setAlwaysOnTop(true);
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
