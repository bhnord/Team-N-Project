package edu.wpi.TeamN.views;

import edu.wpi.TeamN.services.algo.Node;
import javafx.scene.image.ImageView;

public interface mapControllerI {
  double getDownScale();

  ImageView getMapImageView();

  double getNodeSize();

  double getPathSize();

  void correctFloor(Node.Link link);
}
