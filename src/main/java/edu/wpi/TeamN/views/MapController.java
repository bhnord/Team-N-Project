package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.MapEntity.*;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.state.Login;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
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

@Slf4j
public class MapController extends masterController implements Initializable {
  @FXML private JFXColorPicker colorPicker;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @Inject DatabaseService db;

  @FXML private Label XLabel;
  @FXML private Label YLabel;
  @FXML private ImageView mapImageView;
  @FXML private AnchorPane mapAnchor;

  private Group current;
  private Node startNodePath;

  public final double downScale = 0.25;
  public final double upScale = 4;
  private int nodeCount = 0;

  private ActionHandlingI actionHandling;
  private AdminMap adminMap;
  private MapDrawing mapDrawing;
  private MapNodeEditor mapNodeEditor;
  private MapEdgeEditor mapEdgeEditor;

  private Scene appPrimaryScene;
  ArrayList<Node.Link> path = new ArrayList<>();

  @FXML private JFXTextField nodeId;
  @FXML private JFXTextField building;
  @FXML private JFXTextField floor;
  @FXML private JFXTextField longName;
  @FXML private JFXTextField shortName;
  @FXML private JFXTextField nodeType;
  @FXML private JFXTextField XCoord;
  @FXML private JFXTextField YCoord;

  @FXML private JFXTextField edgeID;
  @FXML private JFXTextField startNode;
  @FXML private JFXTextField endNode;

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
    mapNodeEditor = new MapNodeEditor(this);
    mapEdgeEditor = new MapEdgeEditor(this);
    actionHandling = new NodeActionHandling(this, this.mapNodeEditor, this.mapEdgeEditor);
    mapDrawing = new MapDrawing(this);
    mapImageView.setCursor(Cursor.CROSSHAIR);
    this.Load(new ActionEvent());
  }

  @FXML
  public void advanceHome() throws IOException {

    /*
    super.advanceHome(loader, appPrimaryScene);
    Stage stage = (Stage) appPrimaryScene.getWindow();
      // stage.setHeight(800);
      // stage.setWidth(1366);
    stage.centerOnScreen();*/

    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    } else if (login.getUsername().equals("guest") && login.getPassword().equals("guest")) {
      super.advanceHomeGuest(loader, appPrimaryScene);
    }
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
      placeNode("node_" + Integer.toString(nodeCount), mouseEvent.getX(), mouseEvent.getY());
    }

    setCancelOrSubmit(false);
  }

  public void setCancelOrSubmit(Boolean sm) {
    cancelOrSubmit = sm;
  }

  public void startLink(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() != MouseButton.PRIMARY) {
      startNodePath = adminMap.get(mouseEvent.getX(), mouseEvent.getY());
    }
  }

  public void releaseMouse(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() != MouseButton.PRIMARY) {
      Node other = adminMap.get(mouseEvent.getX(), mouseEvent.getY());
      if (other != startNodePath) {
        placeLink(startNodePath.get_nodeID() + "_" + other.get_nodeID(), startNodePath, other);
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
            mapDrawing.getCurrentMap(),
            getBuilding().getText(),
            "",
            getLongName().getText(),
            getShortName().getText());
    n.set_shape((Shape) root.getChildren().get(0));
    root.setCursor(Cursor.CROSSHAIR);
    if (!adminMap.getNodeSet().containsKey(id)) {
      adminMap.addNode(n);
      mapNodeEditor.showNodeProperties(root);
      nodeCount++;
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
    root.setCursor(Cursor.CROSSHAIR);
    if (!adminMap.getEdgeSet().containsKey(id)) {
      adminMap.addEdge(new Edge(id, node1.get_nodeID(), node2.get_nodeID()));
      mapEdgeEditor.showEdgeProperties(root);
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
    Parent root = loader.load(getClass().getResourceAsStream("mapAdmin.fxml"));
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    Stage stage = (Stage) appPrimaryScene.getWindow();
    appPrimaryScene.setRoot(root);
  }

  public void newColor(ActionEvent actionEvent) {
    mapDrawing.colorPath(colorPicker.getValue(), path);
  }

  public void PathFind(ActionEvent actionEvent) {
    mapDrawing.resetColors();
    mapDrawing.colorPath(colorPicker.getValue(), adminMap.pathfind());
  }

  private void DeleteNodesFromMap() throws IOException {
    int i = 7;
    for (javafx.scene.Node root :
        mapAnchor.getChildren().subList(i, mapAnchor.getChildren().size())) {
      if (root.getId().equals(current.getId())) {
        mapAnchor.getChildren().remove(i);
        return;
      } else {
        i++;
      }
    }
  }

  public void DeleteObjectDataBase() throws IOException {
    String id = current.getId();
    if (adminMap.getNodeSet().containsKey(id)) {
      adminMap.deleteNode(id);
    } else if (adminMap.getEdgeSet().containsKey(id)) {
      adminMap.deleteEdge(id);
    } else {
      System.out.println("Object does not exist");
    }
  }

  // Loading from the database
  public void Load(ActionEvent actionEvent) {
    mapAnchor.getChildren().remove(7, mapAnchor.getChildren().size());
    adminMap
        .getEdgeSet()
        .forEach(
            (key, value) -> {
              if (adminMap
                  .getNodeSet()
                  .get(value.getStartNode())
                  .get_floor()
                  .equals(mapDrawing.getCurrentMap())) {
                placeLink(
                    key,
                    adminMap.getNodeSet().get(value.getStartNode()),
                    adminMap.getNodeSet().get(value.getEndNode()));
              }
            });
    adminMap
        .getNodeSet()
        .forEach(
            (key, value) -> {
              if (value.get_floor().equals(mapDrawing.getCurrentMap())) {
                placeNode(key, value.get_x() * downScale, value.get_y() * downScale);
              }
            });
  }

  public void deleteCurrent(ActionEvent actionEvent) throws IOException, InterruptedException {
    DeleteNodesFromMap();
    DeleteObjectDataBase();
  }

  public void SetStartNode(ActionEvent actionEvent) {
    adminMap.SetStartNode(nodeId.getText());
  }

  public void SetEndNode(ActionEvent actionEvent) {
    adminMap.SetEndNode(nodeId.getText());
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

  public AdminMap getAdminMap() {
    return adminMap;
  }

  public void setNodeId(String nodeId) {
    this.nodeId.setText(nodeId);
  }

  public void setBuilding(String building) {
    this.building.setText(building);
  }

  public void setFloor(String floor) {
    this.floor.setText(floor);
  }

  public void setLongName(String longName) {
    this.longName.setText(longName);
  }

  public void setShortName(String shortName) {
    this.shortName.setText(shortName);
  }

  public void setNodeType(String nodeType) {
    this.nodeType.setText(nodeType);
  }

  public void setXCoord(String XCoord) {
    this.XCoord.setText(XCoord);
  }

  public void setYCoord(String YCoord) {
    this.YCoord.setText(YCoord);
  }

  public JFXTextField getNodeId() {
    return nodeId;
  }

  public JFXTextField getBuilding() {
    return building;
  }

  public JFXTextField getFloor() {
    return floor;
  }

  public JFXTextField getLongName() {
    return longName;
  }

  public JFXTextField getShortName() {
    return shortName;
  }

  public JFXTextField getNodeType() {
    return nodeType;
  }

  public JFXTextField getXCoord() {
    return XCoord;
  }

  public JFXTextField getYCoord() {
    return YCoord;
  }

  public void saveNode(ActionEvent actionEvent) {
    mapNodeEditor.commitChanges(current);
  }

  public void setCurrent(Group current) {
    this.current = current;
  }

  public Group getCurrent() {
    return current;
  }

  public void saveEdge(ActionEvent actionEvent) {
    mapEdgeEditor.saveEdge(current);
  }

  public JFXTextField getEdgeID() {
    return edgeID;
  }

  public JFXTextField getStartNode() {
    return startNode;
  }

  public JFXTextField getEndNode() {
    return endNode;
  }

  public void setEdgeID(String s) {
    edgeID.setText(s);
  }

  public void setStartNode(String s) {
    startNode.setText(s);
  }

  public void setEndNode(String s) {
    endNode.setText(s);
  }

  public ImageView getMapImageView() {
    return mapImageView;
  }

  public void setMapImageView(ImageView mapImageView) {
    this.mapImageView = mapImageView;
  }

  public void setMap(ActionEvent actionEvent) {
    mapDrawing.setMap(((Button) actionEvent.getSource()).getId());
    this.Load(actionEvent);
  }
}
