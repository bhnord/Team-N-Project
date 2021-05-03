package edu.wpi.TeamN.utilities;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.LatLng;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import javafx.scene.input.KeyCode;

public class AddressAutoComplete {
  final JFXComboBox addressBox;

  public AddressAutoComplete(final JFXComboBox addressBox) {
    this.addressBox = addressBox;
    GeoApiContext context =
        new GeoApiContext.Builder().apiKey("AIzaSyBBszEPZvetVvgsIbt3pLtXLbPap6dT-KY" + "").build();
    PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken();
    addressBox.setOnKeyReleased(
        key -> {
          if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.UP) return;
          AutocompletePrediction[] predictions = new AutocompletePrediction[0];
          LatLng origin = new LatLng(42.335570023832496, -71.10628519976504);
          try {
            predictions =
                PlacesApi.placeAutocomplete(context, addressBox.getEditor().getText(), token)
                    .origin(origin)
                    .radius(160934) // 100 miles in meters
                    .await();
          } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
          }
          Collection<String> address = new HashSet<>();
          for (AutocompletePrediction prediction : predictions) {
            address.add(prediction.description);
          }
          addressBox.getItems().setAll(address);
          addressBox.show();
        });
  }
}
