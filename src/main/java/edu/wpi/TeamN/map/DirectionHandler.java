package edu.wpi.TeamN.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.PathFinderController;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
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

    mapController.loadRoomDropdown(this.locationDropdown);
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
    texutualDescription.getItems().add(new HBox(getDirectionIcon(l), new Label(l)));
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
    JFXButton button = new JFXButton("Delete");
    button.setId(n.get_nodeID());
    button.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            stops.getItems().remove(button.getParent());
            stopNames.remove(n.get_longName());
            mapController.getPath().remove(n.get_nodeID());
            mapController.updatePath();
          }
        });
    HBox box = new HBox(label, button);
    box.setId(n.get_nodeID());

    mapController.getPath().add(n.get_nodeID());
    System.out.println(mapController.getPath());
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
}
