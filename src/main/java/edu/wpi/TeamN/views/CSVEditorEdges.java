package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
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

  private Scene appPrimaryScene;
  private String selectedFilePath;
  private int numEdgesAdded = 0;

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
            Edge clickedEdge = db.getEdge(id);
            messageLabel.setText("");
            selectedLabel = selected;
            if (!(clickedEdge == null)) {
              ID.setText(clickedEdge.getEdgeID());
              startNode.setText(clickedEdge.getStartNode());
              endNode.setText(clickedEdge.getEndNode());
            } else {
              setEmptyFields();
            }
          }
        });
    //      File file = new File("src/main/resources/MapCSV/MapNEdgesAll.csv");
    //      selectedFilePath = file.getPath();
    //      loadEdges(file);
    loadFromDB();
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
    numEdgesAdded++;
    Label lbl = new Label("New Edge " + numEdgesAdded);
    lbl.setId("New Edge " + numEdgesAdded);
    listView.getItems().add(lbl);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void commitChanges(ActionEvent actionEvent) {
    if (selectedLabel == null) {
      return;
    }
    Edge selectedEdge = db.getEdge(selectedLabel.getId());
    String idText = ID.getText();
    if (!(selectedEdge == null)) {
      if (selectedEdge.getEdgeID().equals(idText)) {
        if (!db.updateEdge(idText, startNode.getText(), endNode.getText())) {
          messageLabel.setText(("Invalid inputs"));
        }
      } else {
        Edge e = new Edge(idText, startNode.getText(), endNode.getText());
        if (db.addEdge(e)) {
          db.deleteEdge(selectedEdge.getEdgeID());
          updateSelectedLabel(idText);
        } else {
          messageLabel.setText("Invalid inputs");
        }
      }
    } else {
      Edge e = new Edge(ID.getText(), startNode.getText(), endNode.getText());
      if (db.addEdge(e)) {
        updateSelectedLabel(idText);
      } else {
        messageLabel.setText("Invalid inputs");
      }
    }
  }

  @FXML
  public void openFile(ActionEvent actionEvent) {
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    File file = fileChooser.showOpenDialog(appPrimaryScene.getWindow());
    if (file == null) {
      return;
    }
    selectedFilePath = file.getPath();
    db.deleteEdgeRows();
    listView.getItems().clear();
    boolean loaded = db.loadCSVtoTable(file.getPath(), "EDGES");
    if (!loaded) {
      loadSuccess.setText(
          "Error loading file; Make sure all edges have corresponding nodes and try again.");
    } else {
      HashSet<Edge> set = db.getAllEdges();
      for (Edge e : set) {
        Label lbl = new Label(e.getEdgeID());
        lbl.setId(e.getEdgeID());
        listView.getItems().add(lbl);
      }
      loadSuccess.setText("Edges file successfully loaded!");
    }
  }

  private void updateSelectedLabel(String idText) {
    selectedLabel.setId(idText);
    selectedLabel.setText(idText);
  }

  private void loadFromDB() {
    listView.getItems().clear();
    HashSet<Edge> edgeSet = db.getAllEdges();
    for (Edge e : edgeSet) {
      Label lbl = new Label(e.getEdgeID());
      lbl.setId(e.getEdgeID());
      listView.getItems().add(lbl);
    }
  }

  private void edgesToCsv(String outputPath) throws IOException {
    Writer fileWriter = new FileWriter(outputPath, false);
    HashSet<Edge> allEdges = db.getAllEdges();
    for (Edge e : allEdges) {
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
    if (!(selectedFilePath == null)) {
      edgesToCsv(selectedFilePath);
      loadSuccess.setText("File Saved!");
    } else {
      messageLabel.setText("Select a filepath first with \"Open File\"");
    }
  }

  public void deleteEdge(ActionEvent actionEvent) {
    if (listView.getItems().isEmpty() || selectedLabel == null) return;
    int index = listView.getItems().indexOf(selectedLabel);
    db.deleteEdge(selectedLabel.getId());
    listView.getItems().remove(selectedLabel);
    if (index != 0) index--;
    if (!(listView.getItems().size() == 0)) {
      selectedLabel = listView.getItems().get(index);
      Edge clickedEdge = db.getEdge(selectedLabel.getId());
      if (clickedEdge != null) {
        ID.setText(clickedEdge.getEdgeID());
        startNode.setText(clickedEdge.getStartNode());
        endNode.setText(clickedEdge.getEndNode());
      } else {
        setEmptyFields();
      }
    } else {
      selectedLabel = null;
      setEmptyFields();
    }
  }

  private void setEmptyFields() {
    ID.setText("");
    startNode.setText("");
    endNode.setText("");
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
    super.advanceViews(actionEvent);
  }
}
