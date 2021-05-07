package edu.wpi.TeamN.faceLogin;

import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
import javafx.scene.control.Alert;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.*;

public class FaceLogin {
  private final DatabaseService db;
  private final HashMap<BufferedImage, Integer> faceImages;
  private final VideoCapture camera = new VideoCapture();

  public FaceLogin(DatabaseService db) {
    this.db = db;
    this.faceImages = db.getAllFaces();
  }

  private static Mat bufferedImageToMat(BufferedImage bi) {
    Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
    byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    mat.put(0, 0, data);
    return mat;
  }

  public User getUserFromFace() {
    ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    camera.open(0);
    Callable<User> faceIdentifier =
        () -> {
          User user;
          for (int i = 0; i < 10; i++) {
            user = identifyUser();
            if (user != null) {
              return user;
            }
            Thread.sleep(30);
          }
          return null;
        };
    Future<User> userFuture = timer.schedule(faceIdentifier, 0, TimeUnit.MILLISECONDS);
    try {
      User user = userFuture.get();
      timer.shutdown();
      camera.release();
      return user;
    } catch (Exception e) {
      e.printStackTrace();
      camera.release();
      timer.shutdown();
      return null;
    }
  }

  private User identifyUser() {
    File classifier =
        new File("src/main/resources/FacialRec/lbpcascades/lbpcascade_frontalface_improved.xml");

    if (!classifier.exists()) {
      displayFatalError("Unable to find classifier!");
      return null;
    }

    CascadeClassifier faceDetector = new CascadeClassifier(classifier.toString());

    if (!camera.isOpened()) {
      displayFatalError("No camera detected!");
      return null;
    }

    Mat image = new Mat();
    camera.read(image);
    Rect[] faces = getFacesInImage(image, faceDetector);
    for (Rect face : faces) {
      System.out.println(face);
      Mat croppedImage = new Mat(image, face);
      User user = identifyFace(croppedImage);
      if (user != null) {
        camera.release();
        return user;
      }
    }
    return null;
  }

  private Rect[] getFacesInImage(Mat image, CascadeClassifier faceDetector) {
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    return faceDetections.toArray();
  }

  private User identifyFace(Mat image) {
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
      User user = db.getUserById(mostSimilarUserID);
      return user;
    } else return null;
  }

  private int compareFaces(Mat currentImage, BufferedImage image) {
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

  private void displayFatalError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fatal Error");
    alert.setHeaderText("Fatal Error");
    alert.setContentText(message);
    alert.showAndWait();
  }
}
