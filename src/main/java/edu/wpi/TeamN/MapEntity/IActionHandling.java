package edu.wpi.TeamN.mapEntity;

import javafx.scene.Group;

public interface IActionHandling {
  public void setNodeInfo(Group node);

  public void setEdgeInfo(Group node);

  public void setNodeStartLink(Group node);

  public void setNodeEndLink(Group node);

  public void addNodeToPath(Group node);

  public void removeNodeToPath(Group node);
}
