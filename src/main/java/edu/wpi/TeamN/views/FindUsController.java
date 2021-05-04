package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.StaticMapsApi;
import com.google.maps.StaticMapsRequest;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Size;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.utilities.AddressAutoComplete;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Slf4j
public class FindUsController extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @FXML WebView webView;
  @FXML private ImageView mapImage;
  @FXML private JFXComboBox<String> addressBox;
  @FXML private Label directionsLabel;
  @FXML private JFXButton printButton;
  // For sidebar nested FXML implementation
  @FXML private AnchorPane anchorPane;

  private GeoApiContext context;
  private Scene appPrimaryScene;
  private WebEngine webEngine;

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
    if (db.getCurrentUser().getUsername().equals("guest")) {
      super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Login Map");
    } else {
      super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");
    }
    webEngine = webView.getEngine();
    context =
        new GeoApiContext.Builder().apiKey("AIzaSyBBszEPZvetVvgsIbt3pLtXLbPap6dT-KY" + "").build();
    new AddressAutoComplete(addressBox);
    StaticMapsRequest mapsRequest = StaticMapsApi.newRequest(context, new Size(900, 500));
    StaticMapsRequest.Markers markers = new StaticMapsRequest.Markers();
    markers.addLocation("80 Francis St, Boston, MA 02115");
    markers.addLocation("15-51 New Whitney St, Boston, MA 02115");
    mapsRequest.center("75 Francis Street, Carrie Hall 103, Boston, MA 02115");
    markers.size(StaticMapsRequest.Markers.MarkersSize.small);
    mapsRequest.markers(markers);
    mapsRequest.zoom(14);
    try {
      ByteArrayInputStream image = new ByteArrayInputStream(mapsRequest.await().imageData);
      mapImage.setImage(new Image(image));
    } catch (Exception e) {
      e.printStackTrace();
    }
    webEngine.load(
        Objects.requireNonNull(
                getClass().getResource("/edu/wpi/TeamN/views/HospitalDirections.html"))
            .toExternalForm());
  }

  @FXML
  private void submitAddress() {
    String address = addressBox.getEditor().getText();
    if (address.equals("")) return;
    StringBuilder directions = new StringBuilder();
    try {
      DirectionsResult leftLot =
          DirectionsApi.getDirections(context, address, "70 Francis St, Boston, MA 02115").await();
      DirectionsResult rightLot =
          DirectionsApi.getDirections(context, address, "15-51 New Whitney St, Boston, MA 02115")
              .await();
      DirectionsResult result =
          leftLot.routes[0].legs[0].duration.inSeconds
                  > rightLot.routes[0].legs[0].duration.inSeconds
              ? rightLot
              : leftLot;
      StaticMapsRequest mapsRequest = StaticMapsApi.newRequest(context, new Size(900, 500));
      mapsRequest.center("75 Francis Street, Carrie Hall 103, Boston, MA 02115");
      mapsRequest.path(result.routes[0].overviewPolyline);
      ByteArrayInputStream image = new ByteArrayInputStream(mapsRequest.await().imageData);
      mapImage.setImage(new Image(image));
      String instructions;
      for (DirectionsStep step : result.routes[0].legs[0].steps) {
        //        if (step.htmlInstructions.toLowerCase().contains("left")) {
        //          directions.append("<b style=\"font-size: 16px;\"><-</b>  ");
        //        } else if (step.htmlInstructions.toLowerCase().contains("right")) {
        //          directions.append("<b style=\"font-size: 16px;\">-></b>  ");
        //        }
        instructions = step.htmlInstructions.replace("<div style=\"font-size:0.9em\">", " (");
        instructions = instructions.replace("</div>", ")");
        directions.append(instructions);
        directions
            .append(" <span style=\"float:right;\">(")
            .append(step.distance)
            .append(")</span>");
        directions.append("<br/><br/>");
      }
      webEngine.executeScript(
          "document.getElementsByTagName('body')[0].insertAdjacentHTML('beforeend', '"
              + directions
              + "');");
      printButton.setDisable(false);
      directionsLabel.setText(
          "The fastest route to the hospital takes "
              + result.routes[0].legs[0].duration
              + " via "
              + result.routes[0].summary
              + ".");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void printDirections() {
    PrinterJob printerJob = PrinterJob.createPrinterJob();
    if (printerJob != null && printerJob.showPrintDialog(webView.getScene().getWindow())) {
      webEngine.print(printerJob);
      printerJob.endJob();
    }
  }
}
