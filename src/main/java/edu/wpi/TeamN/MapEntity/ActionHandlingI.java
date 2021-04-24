package edu.wpi.TeamN.MapEntity;

import javafx.scene.Group;

public interface ActionHandlingI {
  public void setNodeInfo(Group node);

  public void setEdgeInfo(Group node);

  public void setNodeStartLink(Group node);

  public void setNodeEndLink(Group node);
}
