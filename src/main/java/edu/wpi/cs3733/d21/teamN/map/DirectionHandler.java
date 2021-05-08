package edu.wpi.cs3733.d21.teamN.map;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.views.PathFinderController;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class DirectionHandler {
  PathFinderController mapController;
  JFXListView<HBox> stops;
  JFXListView<HBox> texutualDescription;
  JFXComboBox<Label> locationDropdown;
  ArrayList<String> stopNames = new ArrayList<String>();

  public DirectionHandler(
      PathFinderController mapController,
      JFXListView<HBox> path,
      JFXListView<HBox> texutualDescription,
      JFXComboBox<Label> locationDropdown) {
    this.stops = path;
    this.texutualDescription = texutualDescription;
    this.locationDropdown = locationDropdown;
    this.mapController = mapController;

    stops.getStylesheets().add("/StyleSheet/PathfinderListView.css");
    texutualDescription.getStylesheets().add("/StyleSheet/PathfinderListView.css");
    texutualDescription.setVisible(false);
    mapController.loadRoomDropdown(this.locationDropdown, "HALL");
    clickSetup();
  }

  private void clickSetup() {
    texutualDescription.setOnMouseClicked(
        event -> {
          HBox selected = texutualDescription.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            ObservableList<Integer> seletedI =
                texutualDescription.getSelectionModel().getSelectedIndices();
            mapController
                .getMapDrawer()
                .colorPath(mapController.getPathColor().getValue(), mapController.getNodePath());
            Node.Link link =
                mapController
                    .getNodePath()
                    .get(mapController.getNodePath().size() - seletedI.get(0) - 1);
            mapController.getMapDrawer().setMap(link._other.get_floor());
            mapController.mapFloor();
            link._shape.setStroke(Color.RED);
          }
        });
  }

  public void addDirection(String l) {
    if (l.contains("go to floor")) {
      texutualDescription.getItems().add(new HBox(getDirectionIcon(l), new Label(l)));
      texutualDescription.setVisible(true);
    } else if (mapController.getAdminMap().getNodeSet().containsKey(l)) {
      texutualDescription
          .getItems()
          .add(
              new HBox(
                  new Label(
                      "Start on floor "
                          + mapController.getAdminMap().getNodeSet().get(l).get_floor())));
      texutualDescription.setVisible(true);
    } else {
      Label label = new Label("");
      label.setId("floor");
      label.getStylesheets().add("PathfinderListView.css");
      texutualDescription.getItems().add(new HBox(label, getDirectionIcon(l), new Label(l)));
      texutualDescription.setVisible(true);
    }
  }

  private FontIcon getDirectionIcon(String l) {
    FontIcon fontIcon = new FontIcon();
    fontIcon.setIconSize(25);
    if (l.toLowerCase().contains("left")) {
      fontIcon.setIconLiteral("gmi-arrow-back");
    } else if (l.toLowerCase().contains("right")) {
      fontIcon.setIconLiteral("gmi-arrow-forward");
    } else if (l.toLowerCase().contains("straight")) {
      fontIcon.setIconLiteral("gmi-arrow-upward");
    } else if (l.toLowerCase().contains("floor")) {
      fontIcon.setIconLiteral("gmi-contacts");
    }
    return fontIcon;
  }

  public void addStop(Node n) {
    if (stopNames.contains(n.get_longName())) {
      return;
    }

    Label label = new Label(n.get_longName());

    FontIcon fontIcon = new FontIcon();
    fontIcon.setIconLiteral("gmi-clear");
    fontIcon.setId(n.get_nodeID());
    fontIcon.setIconSize(25);
    fontIcon.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            stops.getItems().remove(fontIcon.getParent());
            stopNames.remove(n.get_longName());
            mapController.getPath().remove(n.get_nodeID());
            mapController.updatePath();
            n.get_shape().setVisible(false);
          }
        });
    HBox box = new HBox(label, fontIcon);
    box.setId(n.get_nodeID());

    mapController.getPath().add(n.get_nodeID());
    mapController.updatePath();
    n.get_shape().setFill(mapController.getSelectedNodeColor().getValue());
    stopNames.add(n.get_longName());
    stops.getItems().add(box);
  }

  public void addStopClick(String text) {
    for (Node n : mapController.getAdminMap().getNodeSet().values())
      if (n.get_longName().equals(text)) {
        this.addStop(n);
        return;
      }
  }

  public void clean() {
    stopNames.clear();
    texutualDescription.getItems().clear();
    texutualDescription.setVisible(false);
  }

  public void cleanAll() {
    clean();
    stops.getItems().clear();
  }
}
