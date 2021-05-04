package edu.wpi.TeamN.services.algo;

import java.util.ArrayList;

public abstract class PathFinderAlgorithm {
  abstract ArrayList<Node.Link> pathfindFull(Node start, Node end, IPathFinder.Reduce filter);
}
