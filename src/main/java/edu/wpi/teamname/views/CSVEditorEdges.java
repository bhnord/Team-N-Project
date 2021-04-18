package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.services.algo.Edge;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVEditorEdges extends masterController implements Initializable {

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
  @FXML private JFXTextField startNode;
  @FXML private JFXTextField endNode;

  private Label selectedLabel;
  private HashMap<String, Edge> edgeMap = new HashMap<>();
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
            Edge clickedEdge = edgeMap.get(selected.getId());
            selectedLabel = selected;
            ID.setText(clickedEdge.getEdgeID());
            startNode.setText(clickedEdge.getStartNode());
            endNode.setText(clickedEdge.getEndNode());
          }
        });
    try {
      //      InputStream initialStream =
      // ClassLoader.getSystemResourceAsStream("MapCSV/MapNEdgesAll.csv");
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
    Label lbl = new Label("New Edge");
    String uuid = UUID.randomUUID().toString();
    lbl.setId(uuid);
    edgeMap.put(uuid, new Edge("", "", ""));
    listView.getItems().add(lbl);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void commitChanges(ActionEvent actionEvent) {
    try {
      Edge selectedEdge = edgeMap.get(selectedLabel.getId());

      selectedEdge.setEdgeID(ID.getText());
      selectedEdge.setStartNode(startNode.getText());
      selectedEdge.setEndNode(endNode.getText());
      selectedLabel.setText(selectedEdge.getEdgeID());
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
    edgeMap.clear();
    csvToNodes(file);
    loadSuccess.setText("File successfully loaded!");
    messageLabel.setText("" + file);
    listView.getItems().clear();
    for (Edge e : edgeMap.values()) {
      Label lbl = new Label(e.getEdgeID());
      lbl.setId(e.getEdgeID());
      listView.getItems().add(lbl);
    }
  }

  private HashMap<String, Edge> csvToNodes(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    String line = scanner.nextLine();

    if (line.contains("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName")
        && scanner.hasNextLine()) {
      line = scanner.nextLine();
    }

    if (!scanner.hasNextLine()) return edgeMap;
    do {
      String[] entries = line.split(",");
      if (entries.length == 3) {
        String ID = entries[0];
        String sNode = entries[1];
        String eNode = entries[2];
        edgeMap.put(ID, new Edge(ID, sNode, eNode));
      }
      line = scanner.nextLine();
    } while (scanner.hasNextLine());
    return edgeMap;
  }

  private void nodesToCsv(String outputPath) throws IOException {
    Writer fileWriter = new FileWriter(outputPath, false);
    for (Edge e : edgeMap.values()) {
      String outputString = "";
      outputString += e.getEdgeID();
      outputString += ",";
      outputString += e.getStartNode();
      outputString += ",";
      outputString += e.getEndNode();
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
    edgeMap.remove(selectedLabel.getId());
    listView.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (listView.getItems().size() == 0) return;
    selectedLabel = listView.getItems().get(index);
    Edge clickedEdge = edgeMap.get(selectedLabel.getId());
    ID.setText(clickedEdge.getEdgeID());
    startNode.setText(clickedEdge.getStartNode());
    endNode.setText(clickedEdge.getEndNode());
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
