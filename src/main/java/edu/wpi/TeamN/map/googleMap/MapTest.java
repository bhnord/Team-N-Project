package edu.wpi.TeamN.map.googleMap;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.StaticMapsApi;
import com.google.maps.StaticMapsRequest;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Size;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MapTest implements Initializable {

  @FXML private ImageView mapImage;
  @FXML private WebView webView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    webEngine.load(
    //        getClass().getResource("/edu/wpi/TeamN/views/MapTest/WebView.html").toExternalForm());
    WebEngine webEngine = webView.getEngine();
    StringBuilder directions =
        new StringBuilder(
            "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <title>Title</title>\n"
                + "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">\n"
                + "    <link href=\"https://fonts.googleapis.com/css2?family=Roboto:wght@300;700&display=swap\" rel=\"stylesheet\">\n"
                + "</head>\n"
                + "<body style=\"font-family: 'Roboto', sans-serif;\">");
    GeoApiContext context =
        new GeoApiContext.Builder().apiKey("AIzaSyBBszEPZvetVvgsIbt3pLtXLbPap6dT-KY" + "").build();
    try {
      DirectionsResult result =
          DirectionsApi.getDirections(
                  context, "WPI", "75 Francis Street, Carrie Hall 103, Boston, MA 02115")
              .await();
      StaticMapsRequest mapsRequest = StaticMapsApi.newRequest(context, new Size(800, 800));
      mapsRequest.center("75 Francis Street, Carrie Hall 103, Boston, MA 02115");
      //      mapsRequest.zoom(16);
      mapsRequest.path(result.routes[0].overviewPolyline);
      ByteArrayInputStream bais = new ByteArrayInputStream(mapsRequest.await().imageData);
      mapImage.setImage(new Image(bais));
      for (DirectionsStep step : result.routes[0].legs[0].steps) {
        System.out.println(step.htmlInstructions);
        directions.append(step.htmlInstructions + "<br/>");
      }
      directions.append("</body>\n" + "</html>");
      webEngine.loadContent(directions.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    //        System.out.println();
    //    WebEngine webEngine = webView.getEngine();
    //    webEngine.setUserAgent(
    //        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like
    // Gecko) Chrome/90.0.4430.93 Safari/537.36");
    //    webEngine.load(
    //
    // "https://www.google.com/maps/place/Brigham+and+Women's+Hospital/@42.33557,-71.1084739,17z/data=!3m1!4b1!4m5!3m4!1s0x89e3798e64f59d2d:0x2762dfa4fc548043!8m2!3d42.3355661!4d-71.1062852");
    //    webEngine.getDocument().createElement()
  }
}
