package edu.wpi.cs3733.d21.teamN.services.algo;

import java.util.ArrayList;

public interface IPathFinder {
  public interface Reduce {
    boolean isValid(Node.Link l);
  }

  abstract ArrayList<Node.Link> pathfindFull(Node start, Node end, Reduce filter);
}
