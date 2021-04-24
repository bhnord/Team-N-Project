package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import edu.wpi.TeamN.services.algo.AdminMap;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapController extends masterController implements Initializable {
  @FXML private JFXColorPicker colorPicker;
  // @Inject ServiceTwo graph;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @Inject DatabaseService db;

  @FXML private Label XLabel;
  @FXML private Label YLabel;
  @FXML private Label current;
  @FXML private ImageView mapImageView;
  @FXML private AnchorPane mapAnchor;

  AdminMap adminMap;
  private String selectedID;
  private Node startNode;
  private Node endNode;
  private final double downScale = .168;
  private final double upScale = 5.9523;

  private String startNodePath;
  private String endNodePath;
  final DoubleProperty zoomProperty = new SimpleDoubleProperty();

  private Scene appPrimaryScene;
  HashMap<String, Integer> mapObjects = new HashMap<>();
  ArrayList<Node.Link> path = new ArrayList<>();

  String nodeName;
  String building;
  String floor;
  String longname;
  String shortname;
  Boolean cancelOrSubmit = false;

  HashMap<String, Node> nodeSet = new HashMap<>();
  HashMap<String, Edge> edgeSet = new HashMap<>();

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
      System.out.println(mouseEvent.getX() + ":" + mouseEvent.getY());
      System.out.println("startingLink");
      startNode = adminMap.get(mouseEvent.getX(), mouseEvent.getY());
    }
  }

  public void releaseMouse(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() != MouseButton.PRIMARY) {
      System.out.println("endingLink");
      Node other = adminMap.get(mouseEvent.getX(), mouseEvent.getY());
      System.out.println(startNode.get_nodeID() + "_" + other.get_nodeID());
      if (other != startNode) {
        placeLink(startNode.get_nodeID() + "_" + other.get_nodeID(), startNode, other);
      }
    }
  }

  private void placeNode(String id, double x, double y) {
    Circle simpleNode = new Circle(x, y, 4.5);
    simpleNode.setTranslateZ(10);
    simpleNode.setFill(Color.BLUE);
    Group root = new Group(simpleNode);
    root.setId(id);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            current.setText(root.getId());
          }
        });
    root.setOnMousePressed(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            startLink(event);
          }
        });
    root.setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            releaseMouse(event);
          }
        });
    mapAnchor.getChildren().add(root);
    Node n =
        new Node(
            x * upScale,
            y * upScale,
            id,
            this.floor,
            this.building,
            "",
            this.longname,
            this.shortname);
    n.set_shape(simpleNode);
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
    String id1 = node1.get_nodeID();
    String id2 = node2.get_nodeID();
    double distance = node1.heuristic(node2);
    Line simpleNode =
        new Line(
            node1.get_x() * downScale,
            node1.get_y() * downScale,
            node2.get_x() * downScale,
            node2.get_y() * downScale);
    simpleNode.setTranslateZ(5);
    node1.addNeighbor(id, node2, distance, simpleNode);
    node2.addNeighbor(id, node1, distance, simpleNode);
    simpleNode.setStrokeWidth(3);
    Group root = new Group(simpleNode);
    String edgeID = id1 + "_" + id2;
    root.setId(edgeID);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            current.setText(root.getId());
          }
        });
    mapAnchor.getChildren().add(root);
    if (!adminMap.getEdgeSet().containsKey(id)) {
      adminMap.addEdge(new Edge(edgeID, node1.get_nodeID(), node2.get_nodeID()));
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
    colorPath(colorPicker.getValue(), path);
  }

  public void PathFind(ActionEvent actionEvent) {
    resetColors();
    colorPath(colorPicker.getValue(), adminMap.pathfind());
  }

  private void colorPath(Color color, ArrayList<Node.Link> ret) {
    for (Node.Link c2 : ret) {
      Line simpleNode = c2._shape;
      simpleNode.setStroke(color);
    }
  }

  private void resetColors() {
    for (Node n : adminMap.getNodeSet().values()) {
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setStroke(Color.BLACK);
      }
    }
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
}
