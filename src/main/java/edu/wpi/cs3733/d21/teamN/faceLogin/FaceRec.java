package edu.wpi.cs3733.d21.teamN.faceLogin;

import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.HashMap;

abstract class FaceRec {
  final DatabaseService db;
  final HashMap<BufferedImage, Integer> faceImages;

  public FaceRec(DatabaseService db) {
    this.db = db;
    // TODO? Change db to store Mat of descriptors
    faceImages = db.getAllFaces();
  }

  int compareFaces(Mat currentImage, BufferedImage image) {
    Mat compareImage = bufferedImageToMat(image);

    ORB orb = ORB.create();
    int similarity = 0;

    MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
    MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
    orb.detect(currentImage, keypoints1);
    orb.detect(compareImage, keypoints2);

    Mat descriptors1 = new Mat();
    Mat descriptors2 = new Mat();
    orb.compute(currentImage, keypoints1, descriptors1);
    orb.compute(compareImage, keypoints2, descriptors2);

    if (descriptors1.cols() == descriptors2.cols()) {
      MatOfDMatch matchMatrix = new MatOfDMatch();
      DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING)
          .match(descriptors1, descriptors2, matchMatrix);

      for (DMatch match : matchMatrix.toList()) if (match.distance <= 50) similarity++;
    }

    return similarity;
  }

  User identifyFace(Mat image) {
    int errorThreshold = 2;
    int mostSimilar = -1;
    int mostSimilarUserID = -1;

    for (BufferedImage capture : faceImages.keySet()) {
      int similarities = compareFaces(image, capture);

      if (similarities > mostSimilar) {
        mostSimilar = similarities;
        mostSimilarUserID = faceImages.get(capture);
      }
    }

    if (mostSimilarUserID != -1 && mostSimilar > errorThreshold) {
      return db.getUserById(mostSimilarUserID);
    } else return null;
  }

  Mat bufferedImageToMat(BufferedImage bi) {
    Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
    byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    mat.put(0, 0, data);
    return mat;
  }

  Image mat2Image(Mat frame) {
    try {
      return SwingFXUtils.toFXImage(convertMatToBufferedImage(frame), null);
    } catch (Exception e) {
      //      System.err.println("Cannot convert the Mat obejct: " + e);
      return null;
    }
  }

  BufferedImage convertMatToBufferedImage(Mat matrix) {
    int width = matrix.width();
    int height = matrix.height();
    int type = matrix.channels() != 1 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;

    if (type == BufferedImage.TYPE_3BYTE_BGR)
      Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2RGB);

    byte[] data = new byte[width * height * (int) matrix.elemSize()];
    matrix.get(0, 0, data);

    BufferedImage out = new BufferedImage(width, height, type);
    out.getRaster().setDataElements(0, 0, width, height, data);

    return out;
  }

  void displayFatalError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fatal Error");
    alert.setHeaderText("Fatal Error");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
