package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.services.algo.DataNode;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
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

  private Label selectedLabel;
  private HashMap<String, DataNode> nodeMap = new HashMap<>();
  private Scene appPrimaryScene;
  private String selectedFilePath;

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
    // log.debug(state.toString());
    listView.setOnMouseClicked(
        event -> {
          Label selected = listView.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            DataNode clickedNode = nodeMap.get(selected.getId());
            selectedLabel = selected;
            ID.setText(clickedNode.get_nodeID());
            XCoord.setText(Double.toString(clickedNode.get_x()));
            YCoord.setText(Double.toString(clickedNode.get_y()));
            Floor.setText(clickedNode.get_floor());
            Building.setText(clickedNode.get_building());
            ShortName.setText(clickedNode.get_shortName());
            LongName.setText(clickedNode.get_longName());
            NodeType.setText(clickedNode.get_nodeType());
          }
        });
    try {
      //      InputStream initialStream =
      // ClassLoader.getSystemResourceAsStream("MapCSV/MapNNodesAll.csv");
      //      File file = new File("MapCSV/targetFile.tmp");
      //      FileUtils.copyInputStreamToFile(initialStream, file);
      //      selectedFilePath = file.getPath();
      //      loadNodes(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
    Label lbl = new Label("New Node");
    String uuid = UUID.randomUUID().toString();
    lbl.setId(uuid);
    nodeMap.put(uuid, new DataNode(0, 0, "", "", "", "", "", ""));
    listView.getItems().add(lbl);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void commitChanges(ActionEvent actionEvent) {
    try {
      DataNode selectedNode = nodeMap.get(selectedLabel.getId());
      double xcoord = Double.parseDouble(XCoord.getText());
      double ycoord = Double.parseDouble(YCoord.getText());
      selectedNode.set_nodeID(ID.getText());
      selectedNode.set_x(xcoord);
      selectedNode.set_y(ycoord);
      selectedNode.set_floor(Floor.getText());
      selectedNode.set_building(Building.getText());
      selectedNode.set_nodeType(NodeType.getText());
      selectedNode.set_longName(LongName.getText());
      selectedNode.set_shortName(ShortName.getText());
      selectedLabel.setText(selectedNode.get_nodeID());
    } catch (Exception e) {
      messageLabel.setText("Invalid type in field");
    }
  }

  public void openFile(ActionEvent actionEvent) throws IOException {
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    File file = fileChooser.showOpenDialog(appPrimaryScene.getWindow());
    try {
      selectedFilePath = file.getPath();
      loadNodes(file);
    } catch (Exception e) {
      loadSuccess.setText("Error loading file, try again.");
    }
  }

  private void loadNodes(File file) throws FileNotFoundException {
    nodeMap.clear();
    csvToNodes(file);
    loadSuccess.setText("File successfully loaded!");
    messageLabel.setText("" + file);
    listView.getItems().clear();
    for (DataNode node : nodeMap.values()) {
      Label lbl = new Label(node.get_nodeID());
      lbl.setId(node.get_nodeID());
      listView.getItems().add(lbl);
    }
  }

  private HashMap<String, DataNode> csvToNodes(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    if (!scanner.hasNextLine()) return nodeMap;
    String line = scanner.nextLine();

    if (line.contains("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName")
        && scanner.hasNextLine()) {
      line = scanner.nextLine();
    }

    if (!scanner.hasNextLine()) return nodeMap;
    do {
      String[] entries = line.split(",");
      if (entries.length == 8) {
        String nodeID = entries[0];
        Double xPos = Double.parseDouble(entries[1]);
        Double yPos = Double.parseDouble(entries[2]);
        String floor = entries[3];
        String building = entries[4];
        String nodeType = entries[5];
        String longName = entries[6];
        String shortName = entries[7];
        nodeMap.put(
            nodeID,
            new DataNode(xPos, yPos, nodeID, floor, building, nodeType, longName, shortName));
      }
      line = scanner.nextLine();
    } while (scanner.hasNextLine());
    return nodeMap;
  }

  private void nodesToCsv(String outputPath) throws IOException {
    Writer fileWriter = new FileWriter(outputPath, false);
    for (DataNode node : nodeMap.values()) {
      String outputString = "";
      outputString += node.get_nodeID();
      outputString += ",";
      outputString += node.get_x();
      outputString += ",";
      outputString += node.get_y();
      outputString += ",";
      outputString += node.get_floor();
      outputString += ",";
      outputString += node.get_building();
      outputString += ",";
      outputString += node.get_nodeType();
      outputString += ",";
      outputString += node.get_longName();
      outputString += ",";
      outputString += node.get_shortName();
      outputString += "\n";
      fileWriter.write(outputString);
    }
    fileWriter.close();
  }

  public void saveFile(ActionEvent actionEvent) throws IOException {
    if (selectedFilePath != "") {
      loadSuccess.setText("Please select a file first!");
      return;
    }
    nodesToCsv(selectedFilePath);
    loadSuccess.setText("File Saved!");
  }

  public void DeleteNode(ActionEvent actionEvent) {
    if (listView.getItems().isEmpty()) return;
    int index = listView.getItems().indexOf(selectedLabel);
    nodeMap.remove(selectedLabel.getId());
    listView.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (listView.getItems().size() == 0) return;
    ;
    selectedLabel = listView.getItems().get(index);
    DataNode clickedNode = nodeMap.get(selectedLabel.getId());
    ID.setText(clickedNode.get_nodeID());
    XCoord.setText(Double.toString(clickedNode.get_x()));
    YCoord.setText(Double.toString(clickedNode.get_y()));
    Floor.setText(clickedNode.get_floor());
    Building.setText(clickedNode.get_building());
    ShortName.setText(clickedNode.get_shortName());
    LongName.setText(clickedNode.get_longName());
    NodeType.setText(clickedNode.get_nodeType());
  }
}
