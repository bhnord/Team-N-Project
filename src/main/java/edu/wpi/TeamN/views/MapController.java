package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import edu.wpi.TeamN.MapEntity.ActionHandlingI;
import edu.wpi.TeamN.MapEntity.AdminMap;
import edu.wpi.TeamN.MapEntity.MapDrawing;
import edu.wpi.TeamN.MapEntity.NodeActionHandling;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Slf4j
public class MapController extends masterController implements Initializable {
  @FXML private JFXColorPicker colorPicker;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @Inject DatabaseService db;

  @FXML private Label XLabel;
  @FXML private Label YLabel;
  @FXML private Label current;
  @FXML private ImageView mapImageView;

  @FXML private AnchorPane mapAnchor;
  private Node startNode;
  public final double downScale = .168;

  public final double upScale = 5.9523;

  private ActionHandlingI actionHandling;
  private AdminMap adminMap;
  private MapDrawing mapDrawing;

  private Scene appPrimaryScene;
  ArrayList<Node.Link> path = new ArrayList<>();

  String nodeName;
  String building;
  String floor;
  String longname;
  String shortname;
  Boolean cancelOrSubmit = false;

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

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
    colorPicker.setValue(Color.BLUE);
    adminMap = new AdminMap(db);
    actionHandling = new NodeActionHandling(this);
    mapDrawing = new MapDrawing(this);
    this.Load(new ActionEvent());
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
    Stage stage = (Stage) appPrimaryScene.getWindow();
    // stage.setHeight(800);
    // stage.setWidth(1366);
    stage.centerOnScreen();
  }

  /**
   * Prints the x and y values of the cursor on the bottom left of the screen
   *
   * @param mouseDragEvent
   */
  public void xyPrint(MouseEvent mouseDragEvent) {
    XLabel.setText(String.valueOf(mouseDragEvent.getX()));
    YLabel.setText(String.valueOf(mouseDragEvent.getY()));
  }

  /**
   * on mouse click on the map places a node with no id but when clicked it returns the node FX:ID
   *
   * @param mouseEvent
   */
  public void placeNodeClick(MouseEvent mouseEvent) throws IOException {
    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
      NameNode nameNodeClass = new NameNode();
      nameNodeClass.confirm(this);
      if (cancelOrSubmit && !nodeName.equals("")) {
        placeNode(nodeName, mouseEvent.getX(), mouseEvent.getY());
      }
    }

    setCancelOrSubmit(false);
  }

  public void setNodeProperties(
      String nodeName, String floor, String building, String longname, String shortname) {
    this.nodeName = nodeName;
    this.floor = floor;
    this.longname = longname;
    this.shortname = shortname;
    this.building = building;
  }

  public void setCancelOrSubmit(Boolean sm) {
    cancelOrSubmit = sm;
  }

  public void startLink(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() != MouseButton.PRIMARY) {
      startNode = adminMap.get(mouseEvent.getX(), mouseEvent.getY());
    }
  }

  public void releaseMouse(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() != MouseButton.PRIMARY) {
      Node other = adminMap.get(mouseEvent.getX(), mouseEvent.getY());
      if (other != startNode) {
        placeLink(startNode.get_nodeID() + "_" + other.get_nodeID(), startNode, other);
      }
    }
  }

  private void placeNode(String id, double x, double y) {
    Group root = mapDrawing.drawNode(id, x, y);
    actionHandling.setNodeInfo(root);
    actionHandling.setNodeStartLink(root);
    actionHandling.setNodeEndLink(root);
    mapAnchor.getChildren().add(root);
    Node n =
        new Node(
            x * getUpScale(),
            y * getUpScale(),
            id,
            this.floor,
            this.building,
            "",
            this.longname,
            this.shortname);
    n.set_shape((Shape) root.getChildren().get(0));
    if (!adminMap.getNodeSet().containsKey(id)) {
      adminMap.addNode(n);
      setNodeProperties("", "", "", "", "");
    }
  }

  /**
   * Places a line between two nodes
   *
   * @param node1 node id of first node
   * @param node2 node id of the second node
   */
  private void placeLink(String id, Node node1, Node node2) {
    Group root = mapDrawing.drawLine(id, node1, node2);
    adminMap.makeEdge(id, node1, node2, (Line) root.getChildren().get(0));
    actionHandling.setEdgeInfo(root);
    mapAnchor.getChildren().add(root);
    if (!adminMap.getEdgeSet().containsKey(id)) {
      adminMap.addEdge(new Edge(id, node1.get_nodeID(), node2.get_nodeID()));
    }
  }

  /**
   * restarts the map class. Needs work.
   *
   * @param actionEvent
   * @throws IOException
   */
  // Reloading the map scene
  public void clear(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("map.fxml"));
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    Stage stage = (Stage) appPrimaryScene.getWindow();
    // stage.setX(bounds.getMinX());
    // stage.setY(bounds.getMinY());
    // stage.setWidth(bounds.getWidth());
    // stage.setHeight(bounds.getHeight());
    appPrimaryScene.setRoot(root);
  }

  public void newColor(ActionEvent actionEvent) {
    adminMap.colorPath(colorPicker.getValue(), path);
  }

  public void PathFind(ActionEvent actionEvent) {
    adminMap.resetColors();
    adminMap.colorPath(colorPicker.getValue(), adminMap.pathfind());
  }

  private void DeleteNodesFromMap() throws IOException {
    int i = 1;
    for (javafx.scene.Node root :
        mapAnchor.getChildren().subList(1, mapAnchor.getChildren().size() - 1)) {
      if (root.getId().equals(current.getText())) {
        mapAnchor.getChildren().remove(i);
        return;
      } else {
        i++;
      }
    }
  }

  public void DeleteObjectDataBase() throws IOException {
    String id = current.getText();
    if (adminMap.getNodeSet().containsKey(id)) {
      adminMap.deleteNode(id);
    } else if (adminMap.getEdgeSet().containsKey(id)) {
      adminMap.deleteEdge(id);
    } else {
      System.out.println("Object does not exist");
    }
    current.setText("No object Selected");
  }

  // Loading from the database
  public void Load(ActionEvent actionEvent) {

    adminMap
        .getEdgeSet()
        .forEach(
            (key, value) -> {
              placeLink(
                  key,
                  adminMap.getNodeSet().get(value.getStartNode()),
                  adminMap.getNodeSet().get(value.getEndNode()));
            });
    adminMap
        .getNodeSet()
        .forEach(
            (key, value) -> {
              placeNode(key, value.get_x() * downScale, value.get_y() * downScale);
            });
  }

  public void deleteCurrent(ActionEvent actionEvent) throws IOException, InterruptedException {
    DeleteNodesFromMap();
    DeleteObjectDataBase();
  }

  public void SetStartNode(ActionEvent actionEvent) {
    adminMap.SetStartNode(current.getText());
  }

  public void SetEndNode(ActionEvent actionEvent) {
    adminMap.SetEndNode(current.getText());
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  public double getDownScale() {
    return downScale;
  }

  public double getUpScale() {
    return upScale;
  }

  public Label getCurrent() {
    return current;
  }
}
