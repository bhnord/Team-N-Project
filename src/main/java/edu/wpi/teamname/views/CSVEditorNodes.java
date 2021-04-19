package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.services.algo.DataNode;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVEditorNodes extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  FileChooser fileChooser = new FileChooser();

  @FXML private JFXButton commitChangesButton;
  @FXML private Label messageLabel;
  @FXML private Label loadSuccess;
  @FXML private Button HomeView;
  @FXML private JFXListView<Label> listView;

  @FXML private JFXTextField ID;
  @FXML private JFXTextField XCoord;
  @FXML private JFXTextField YCoord;
  @FXML private JFXTextField Floor;
  @FXML private JFXTextField Building;
  @FXML private JFXTextField ShortName;
  @FXML private JFXTextField LongName;
  @FXML private JFXTextField NodeType;
  // TODO REMOVE NODE ID FIELD TO JUST LABEL AND NOT AN EDITOR.
  private Label selectedLabel;
  private Scene appPrimaryScene;
  private String selectedFilePath;
  private int numNodesAdded = 0;

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
  public void initialize(URL url, ResourceBundle rb) {
    listView.setOnMouseClicked(
        event -> {
          Label selected = listView.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            String id = selected.getId();
            DataNode clickedNode = db.getNode(id);
            messageLabel.setText("");
            selectedLabel = selected;
            if (!(clickedNode == null)) {
              updateTextFields(clickedNode);
            } else {
              setEmptyFields();
            }
          }
        });
    //      File file = new File("src/main/resources/MapCSV/MapNNodesAll.csv");
    //      selectedFilePath = file.getPath();
    //      loadNodes(file);
    loadFromDB();
  }

  private void updateTextFields(DataNode clickedNode) {
    ID.setText(clickedNode.getNodeID());
    XCoord.setText(Double.toString(clickedNode.get_x()));
    YCoord.setText(Double.toString(clickedNode.get_y()));
    Floor.setText(clickedNode.getFloor());
    Building.setText(clickedNode.getBuilding());
    ShortName.setText(clickedNode.getShortName());
    LongName.setText(clickedNode.getLongName());
    NodeType.setText(clickedNode.getNodeType());
  }

  @FXML
  private void load(ActionEvent event) {

    if (!listView.isExpanded()) {
      listView.setExpanded(true);
      listView.depthProperty().set(1);
      listView.setExpanded(false);
      listView.depthProperty().set(1);
      listView.setExpanded(true);
      listView.depthProperty().set(1);
    } else {
      listView.setExpanded(false);
      listView.depthProperty().set(0);
    }
  }

  @FXML
  private void addNew(ActionEvent actionEvent) {
    numNodesAdded++;
    Label lbl = new Label("New Node " + numNodesAdded);
    lbl.setId("New Node " + numNodesAdded);
    listView.getItems().add(lbl);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void commitChanges(ActionEvent actionEvent) {
    String id = selectedLabel.getId();
    double x, y;
    String f = Floor.getText();
    String b = Building.getText();
    String nt = NodeType.getText();
    String ln = LongName.getText();
    String sn = ShortName.getText();
    DataNode selectedNode = db.getNode(selectedLabel.getId());
    try {
      x = Double.parseDouble(XCoord.getText());
      y = Double.parseDouble(YCoord.getText());

      if (!(selectedNode == null)) {
        if (!db.updateNode(id, x, y, f, b, nt, ln, sn)) {
          messageLabel.setText("Invalid inputs");
        }
      } else {
        DataNode n = new DataNode(x, y, id, f, b, nt, ln, sn);
        if (db.addNode(n)) {
          updateSelectedLabel(id);
        } else {
          messageLabel.setText("Invalid inputs");
        }
      }
    } catch (Exception e) {
      messageLabel.setText("Invalid type in field");
    }
  }

  private void updateSelectedLabel(String idText) {
    selectedLabel.setId(idText);
    selectedLabel.setText(idText);
  }

  public void openFile(ActionEvent actionEvent) throws IOException {
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    File file = fileChooser.showOpenDialog(appPrimaryScene.getWindow());
    selectedFilePath = file.getPath();
    db.deleteNodeRows();
    listView.getItems().clear();
    boolean loaded = db.loadCSVtoTable(file.getPath(), "NODES");
    if (!loaded) {
      loadSuccess.setText("Error loading file.");
    } else {
      HashSet<DataNode> set = db.getAllNodes();
      for (DataNode n : set) {
        Label lbl = new Label(n.getNodeID());
        lbl.setId(n.getNodeID());
        listView.getItems().add(lbl);
      }
      loadSuccess.setText("Edges file successfully loaded!");
    }
  }

  //  private void loadNodes(File file) throws FileNotFoundException {
  //    nodeMap.clear();
  //    csvToNodes(file);
  //    loadSuccess.setText("File successfully loaded!");
  //    messageLabel.setText("" + file);
  //    listView.getItems().clear();
  //    for (DataNode node : nodeMap.values()) {
  //      Label lbl = new Label(node.get_nodeID());
  //      lbl.setId(node.get_nodeID());
  //      listView.getItems().add(lbl);
  //    }
  //  }

  //  private HashMap<String, DataNode> csvToNodes(File file) throws FileNotFoundException {
  //    Scanner scanner = new Scanner(file);
  //    if (!scanner.hasNextLine()) return nodeMap;
  //    String line = scanner.nextLine();
  //
  //    if (line.contains("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName")
  //        && scanner.hasNextLine()) {
  //      line = scanner.nextLine();
  //    }
  //
  //    if (!scanner.hasNextLine()) return nodeMap;
  //    do {
  //      String[] entries = line.split(",");
  //      if (entries.length == 8) {
  //        String nodeID = entries[0];
  //        Double xPos = Double.parseDouble(entries[1]);
  //        Double yPos = Double.parseDouble(entries[2]);
  //        String floor = entries[3];
  //        String building = entries[4];
  //        String nodeType = entries[5];
  //        String longName = entries[6];
  //        String shortName = entries[7];
  //        nodeMap.put(
  //            nodeID,
  //            new DataNode(xPos, yPos, nodeID, floor, building, nodeType, longName, shortName));
  //      }
  //      line = scanner.nextLine();
  //    } while (scanner.hasNextLine());
  //    return nodeMap;
  //  }

  private void nodesToCsv(String outputPath) throws IOException {
    Writer fileWriter = new FileWriter(outputPath, false);
    HashSet<DataNode> set = db.getAllNodes();
    for (DataNode node : set) {
      String outputString = "";
      outputString += node.getNodeID();
      outputString += ",";
      outputString += node.get_x();
      outputString += ",";
      outputString += node.get_y();
      outputString += ",";
      outputString += node.getFloor();
      outputString += ",";
      outputString += node.getBuilding();
      outputString += ",";
      outputString += node.getNodeType();
      outputString += ",";
      outputString += node.getLongName();
      outputString += ",";
      outputString += node.getShortName();
      outputString += "\n";
      fileWriter.write(outputString);
    }
    fileWriter.close();
  }

  public void saveFile(ActionEvent actionEvent) throws IOException {
    nodesToCsv(selectedFilePath);
    loadSuccess.setText("File Saved!");
  }

  public void DeleteNode(ActionEvent actionEvent) {
    if (listView.getItems().isEmpty()) return;
    int index = listView.getItems().indexOf(selectedLabel);
    db.deleteNode(selectedLabel.getId());
    listView.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (!(listView.getItems().size() == 0)) {
      selectedLabel = listView.getItems().get(index);
      DataNode clickedNode = db.getNode(selectedLabel.getId());
      if (clickedNode != null) {
        updateTextFields(clickedNode);
      } else {
        setEmptyFields();
      }
    } else {
      setEmptyFields();
    }
  }

  private void loadFromDB() {
    listView.getItems().clear();
    HashSet<DataNode> set = db.getAllNodes();
    for (DataNode n : set) {
      Label lbl = new Label(n.getNodeID());
      lbl.setId(n.getNodeID());
      listView.getItems().add(lbl);
    }
  }

  private void setEmptyFields() {
    ID.setText("");
    XCoord.setText("");
    YCoord.setText("");
    Floor.setText("");
    Building.setText("");
    ShortName.setText("");
    LongName.setText("");
    NodeType.setText("");
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }
}
