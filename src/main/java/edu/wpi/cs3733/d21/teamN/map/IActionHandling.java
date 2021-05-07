package edu.wpi.cs3733.d21.teamN.map;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

public interface IActionHandling {
  void setNodeInfo(Group node);

  void setEdgeInfo(Group node);

  void setNodeStartLink(Group node);

  void setNodeEndLink(Group node);

  void setNodeDrag(Circle node);

  void addNodeToPath(Group node);

  void removeNodeToPath(Group node);
}
