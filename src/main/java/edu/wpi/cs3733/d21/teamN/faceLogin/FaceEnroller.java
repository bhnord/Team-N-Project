package edu.wpi.cs3733.d21.teamN.faceLogin;

import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FaceEnroller extends FaceRec {
  private final ImageView imageView;
  private boolean shouldSave = false;
  private VideoCapture camera = new VideoCapture();

  public FaceEnroller(DatabaseService db, ImageView imageView) {
    super(db);
    this.imageView = imageView;
  }

  /**
   * Starts the camera and starts trying to identify faces in the video while displaying it on the
   * given imageView
   */
  public void startEnroller() {
    File classifier =
        new File(
            Objects.requireNonNull(
                    getClass()
                        .getResource("/FacialRec/lbpcascades/lbpcascade_frontalface_improved.xml"))
                .getFile());

    if (!classifier.exists()) {
      displayFatalError("Unable to find classifier!");
      return;
    }

    CascadeClassifier faceDetector = new CascadeClassifier(classifier.toString());
    camera.open(0);

    if (!camera.isOpened()) {
      displayFatalError("No camera detected!");
      return;
    }
    //    imageView.getScene().setOn

    Stage stage = (Stage) imageView.getScene().getWindow();
    stage.setOnCloseRequest(
        e -> {
          camera.release();
          Platform.exit();
          System.exit(0);
        });

    stage.setOnHidden(event -> camera.release());
    Runnable frameGrabber =
        () -> {
          Mat rawImage = new Mat();
          camera.read(rawImage);
          Mat mirroredMat = new Mat();
          Core.flip(rawImage, mirroredMat, 1);
          Mat newImage = detectFaces(mirroredMat, faceDetector);
          imageView.setImage(mat2Image(newImage));
        };

    ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
  }

  /**
   * Saves the currently highlighted face to the database based on the user who is currently logged
   * in.
   */
  public void saveFace() {
    shouldSave = true;
  }

  public void releaseCamera() {
    camera.release();
  }

  private Mat detectFaces(Mat image, CascadeClassifier faceDetector) {
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    Rect[] faces = faceDetections.toArray();
    Scalar color = new Scalar(0, 0, 255);
    for (Rect face : faces) {
      Mat croppedImage = new Mat(image, face);
      if (shouldSave) saveImage(croppedImage);

      User user = identifyFace(croppedImage);
      String name = user != null ? user.getUsername() : "Unknown";

      Imgproc.putText(image, "ID: " + name, face.tl(), Font.BOLD, 3, color);
      Imgproc.rectangle(image, face.tl(), face.br(), color);
    }

    int faceCount = faces.length;
    String message = faceCount + " face" + (faceCount == 1 ? "" : "s") + " detected!";
    Imgproc.putText(image, message, new Point(3, 25), Font.BOLD, 2, color);
    return image;
  }

  private void saveImage(Mat image) {
    int currentUserID = db.getCurrentUser().getId();
    BufferedImage bufferedImage = convertMatToBufferedImage(image);
    db.updateUserImage(currentUserID, bufferedImage);
    faceImages.put(bufferedImage, currentUserID);
    shouldSave = false;
  }
}
