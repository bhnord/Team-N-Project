package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamname.services.ServiceTwo;
import edu.wpi.teamname.services.algo.DataNode;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVEditor extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject ServiceTwo graph;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  JFileChooser fc =
      new JFileChooser() {
        @Override
        protected JDialog createDialog(Component parent) throws HeadlessException {
          // intercept the dialog created by JFileChooser
          JDialog dialog = super.createDialog(parent);
          dialog.setModal(true); // doesn't close pop-up until dealt with
          dialog.setAlwaysOnTop(true);
          FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
          fc.setFileFilter(filter);
          return dialog;
        }
      };

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

  private HashMap<String, DataNode> nodeMap = new HashMap<>();
  private Scene appPrimaryScene;

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
    try {
      File file = new File("src/main/resources/MapCSV/MapNNodesAll.csv");
      loadNodes(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    /** PLACEHOLDER TESTING FOR CSV NODES* */
    //        for (int i = 0; i < 4; i++) {
    //            try {
    //                Label lbl = new Label("Item" + i);
    //                listView.getItems().add(lbl);
    //            } catch (Exception ex) {
    //                Logger.getLogger(CSVEditor.class.getName()).log(Level.SEVERE, null, ex);
    //            }
    //        }
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
  private void addNew(ActionEvent event) {
    listView.getItems().add(new Label("new node"));
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void commitChanges(ActionEvent actionEvent) {
    try {
      double xcoord = Double.parseDouble(XCoord.getText());
      double ycoord = Double.parseDouble(YCoord.getText());
      String nodeID = ID.getText();
      Double xPos = xcoord;
      Double yPos = ycoord;
      String floor = Floor.getText();
      String building = Building.getText();
      String nodeType = NodeType.getText();
      String longName = LongName.getText();
      String shortName = ShortName.getText();
    } catch (Exception e) {
      messageLabel.setText("Invalid type in field");
    }
  }

  public void openFile(ActionEvent actionEvent) throws IOException {
    // fc.setCurrentDirectory(new File("c:\\temp"));
    int returnValue = fc.showOpenDialog(fc);
    //    FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
    //    fc.setFileFilter(filter);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      // fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV File", "csv"));
      try {
        loadNodes(file);
      } catch (Exception e) {
        loadSuccess.setText("Error loading file, try again.");
      }
    } else {
      messageLabel.setText("no file chosen");
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
      lbl.setOnMousePressed(
          event -> {
            if (event.isPrimaryButtonDown()) {
              DataNode clickedNode = nodeMap.get(lbl.getId());
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
      listView.getItems().add(lbl);
    }
  }

  private HashMap<String, DataNode> csvToNodes(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
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
}
