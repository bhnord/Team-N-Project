package edu.wpi.TeamN.map.googleMap;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.dlsc.gmapsfx.javascript.object.MapOptions;
import com.dlsc.gmapsfx.javascript.object.MapTypeIdEnum;
import com.dlsc.gmapsfx.service.geocoding.GeocodingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MapTest implements Initializable, MapComponentInitializedListener {

  @FXML private GoogleMapView mapView;
  private GoogleMap map;
  private GeocodingService geocodingService;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mapView.addMapInitializedListener(this);
  }

  @Override
  public void mapInitialized() {
    geocodingService = new GeocodingService();
    MapOptions mapOptions = new MapOptions();

    mapOptions
        .center(new LatLong(41.8919300, 12.5113300))
        .mapType(MapTypeIdEnum.ROADMAP)
        .overviewMapControl(false)
        .panControl(false)
        .rotateControl(false)
        .scaleControl(false)
        .streetViewControl(false)
        .zoomControl(false)
        .zoom(12);

    map = mapView.createMap(mapOptions);
  }
}
