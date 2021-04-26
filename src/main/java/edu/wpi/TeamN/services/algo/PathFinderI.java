package edu.wpi.TeamN.services.algo;

import java.util.ArrayList;

public interface PathFinderI {
  public interface Reduce {
    boolean isValid(Node.Link l);
  }

  ArrayList<Node.Link> pathfindFull(Node start, Node end, Reduce filter);
}
