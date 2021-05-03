package edu.wpi.TeamN.views;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.map.AdminMap;
import edu.wpi.TeamN.map.MapDrawer;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PathFinderController extends MapController implements Initializable {
  ArrayList<String> path = new ArrayList<String>();
  ArrayList<Node.Link> nodePath = new ArrayList<Node.Link>();
  @FXML private AnchorPane anchorPane;
  private Scene appPrimaryScene;

  @FXML private JFXListView<HBox> texutualDescription;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");
    super.init();

    adminMap = new AdminMap(db);
    mapDrawer = new MapDrawer(this);
    mapImageView.setCursor(Cursor.CROSSHAIR);
    this.Load();
    mapDrawer.setUpZoom(mapImageView, mapAnchor);

    //    texutualDescription = new JFXListView<>();
    texutualDescription.setOnMouseClicked(
        event -> {
          HBox selected = texutualDescription.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            ObservableList<Integer> seletedI =
                texutualDescription.getSelectionModel().getSelectedIndices();
            mapDrawer.colorPath(super.getPathColor().getValue(), nodePath);
            Node.Link link = nodePath.get(nodePath.size() - seletedI.get(0) - 1);
            mapDrawer.setMap(link._other.get_floor());
            mapFloor();
            link._shape.setStroke(Color.RED);
          }
        });
  }

  private void updatePath() {
    StringBuilder str = new StringBuilder();
    for (String node : path) {
      if (str.toString().equals("")) str.append(node);
      else str.append(", ").append(node);
    }
  }

  public Group placeNode(Node n) {
    Group root = super.placeNode(n);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            System.out.println(path);
            if (event.getButton() == MouseButton.PRIMARY && !path.contains(n.get_nodeID()))
              path.add(n.get_nodeID());
            else if (event.getButton() == MouseButton.SECONDARY) path.remove(n.get_nodeID());
            updatePath();
            nodeSelected(selectedNodeColor);
          }
        });
    return root;
  }

  @FXML
  private void dragMouse(MouseEvent mouseEvent) {
    this.mapDrawer.captureMouseDrag(mouseEvent);
  }

  public void newColorPath(ActionEvent actionEvent) {
    updateUserColors(pathColor.getId(), pathColor.getValue().toString());
    for (int i = 0; path.size() - 1 > i; i++) {
      System.out.println(getNodeSet());
      ArrayList<Node.Link> pathLink =
          this.pathfind(getNodeSet().get(path.get(i)), getNodeSet().get(path.get(i + 1)));
      nodePath.addAll(pathLink);
      mapDrawer.colorPath(pathColor.getValue(), pathLink);
      PathFinder p = new PathFinder();
      ArrayList<String> s = p.getDescription(pathLink);
      for (String l : s) {
        texutualDescription.getItems().add(new HBox(getDirectionIcon(l), new Label(l)));
      }
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

  public void mapFloor() {
    super.mapFloor();
    for (Node.Link link : nodePath) {
      correctFloor(link);
    }
  }

  public void nodeSelected(JFXColorPicker a) {
    for (Node n : getNodeSet().values()) {
      if (path.contains(n.get_nodeID())) {
        n.get_shape().setFill(a.getValue());
      }
    }
  }

  public void clearSelection(ActionEvent actionEvent) {
    //    reset();
    path = new ArrayList<String>();
  }

  public void newSize(ActionEvent actionEvent) {
    double nodeValue = Double.parseDouble(nodeSize.getText());
    double edgeValue = Double.parseDouble(pathSize.getText());
    if (((JFXTextField) actionEvent.getSource()).getId().equals("nodeSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      userPrefs.setNodeSize(nodeValue);
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setRadius(nodeValue);
        }
      }
    } else if (((JFXTextField) actionEvent.getSource()).getId().equals("pathSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      userPrefs.setEdgeWidth(edgeValue);
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Line) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setStrokeWidth(edgeValue);
        }
      }
    }
    db.updateUserPrefs(Integer.parseInt(String.valueOf(db.getCurrentUser().getId())), userPrefs);
  }

  public void mousePress(MouseEvent mouseEvent) {
    super.mouseClick(mouseEvent);
  }
}
