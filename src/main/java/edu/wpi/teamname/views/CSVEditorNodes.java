package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.services.algo.Node;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
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
            Node clickedNode = db.getNode(id);
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

  private void updateTextFields(Node clickedNode) {
    ID.setText(clickedNode.get_nodeID());
    XCoord.setText(Double.toString(clickedNode.get_x()));
    YCoord.setText(Double.toString(clickedNode.get_y()));
    Floor.setText(clickedNode.get_floor());
    Building.setText(clickedNode.get_building());
    ShortName.setText(clickedNode.get_shortName());
    LongName.setText(clickedNode.get_longName());
    NodeType.setText(clickedNode.get_nodeID());
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
    if (selectedLabel == null) {
      return;
    }
    String id = selectedLabel.getId();
    double x, y;
    String f = Floor.getText();
    String b = Building.getText();
    String nt = NodeType.getText();
    String ln = LongName.getText();
    String sn = ShortName.getText();
    Node selectedNode = db.getNode(selectedLabel.getId());

    try {
      x = Double.parseDouble(XCoord.getText());
      y = Double.parseDouble(YCoord.getText());

      if (!(selectedNode == null)) {

        if (!id.equals(selectedNode.get_nodeID())) {
          if (!db.updateNode(id, x, y, f, b, nt, ln, sn)) {
            messageLabel.setText("Invalid inputs");
          }
        } else {
          messageLabel.setText("You cannot change the ID of an already made node");
        }
      } else {
        id = ID.getText();
        Node n = new Node(x, y, id, f, b, nt, ln, sn);
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
    if (file != null) {
      selectedFilePath = file.getPath();
      db.deleteNodeRows();
      listView.getItems().clear();
      boolean loaded = db.loadCSVtoTable(file.getPath(), "NODES");
      if (!loaded) {
        loadSuccess.setText("Error loading file.");
      } else {
        HashSet<Node> set = db.getAllNodes();
        for (Node n : set) {
          Label lbl = new Label(n.get_nodeID());
          lbl.setId(n.get_nodeID());
          listView.getItems().add(lbl);
        }
        loadSuccess.setText("Nodes file successfully loaded!");
      }
    }
  }

  private void nodesToCsv(String outputPath) throws IOException {
    Writer fileWriter = new FileWriter(outputPath, false);
    HashSet<Node> set = db.getAllNodes();
    for (Node node : set) {
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
    if (!(selectedFilePath == null)) {
      nodesToCsv(selectedFilePath);
      loadSuccess.setText("File Saved!");
    } else {
      messageLabel.setText("Select a filepath first with \"Open File\"");
    }
  }

  public void deleteNode(ActionEvent actionEvent) {
    if (listView.getItems().isEmpty() || selectedLabel == null) return;
    int index = listView.getItems().indexOf(selectedLabel);
    db.deleteNode(selectedLabel.getId());
    listView.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (!(listView.getItems().size() == 0)) {
      selectedLabel = listView.getItems().get(index);
      Node clickedNode = db.getNode(selectedLabel.getId());
      if (clickedNode != null) {
        updateTextFields(clickedNode);
      } else {
        setEmptyFields();
      }
    } else {
      selectedLabel = null;
      setEmptyFields();
    }
  }

  private void loadFromDB() {
    listView.getItems().clear();
    HashSet<Node> set = db.getAllNodes();
    for (Node n : set) {
      Label lbl = new Label(n.get_nodeID());
      lbl.setId(n.get_nodeID());
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
