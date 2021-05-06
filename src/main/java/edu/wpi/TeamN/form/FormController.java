package edu.wpi.TeamN.form;

import com.google.inject.Inject;
import edu.wpi.TeamN.views.MasterController;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class FormController extends MasterController {
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
        //form.build(this.stackpane);
    }


}
