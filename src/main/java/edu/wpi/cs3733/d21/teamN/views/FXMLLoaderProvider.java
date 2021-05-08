package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import javafx.fxml.FXMLLoader;

public class FXMLLoaderProvider extends AbstractModule {

  @Provides
  public FXMLLoader provideFXMLLoader(Injector injector) {
    FXMLLoader loader = new FXMLLoader();
    loader.setControllerFactory(injector::getInstance);
    return loader;
  }
}
