package edu.wpi.cs3733.d21.teamN.faceLogin;

import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.*;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class FaceLogin extends FaceRec {
  private final VideoCapture camera = new VideoCapture();

  public FaceLogin(DatabaseService db) {
    super(db);
  }

  /**
   * Takes photos of the user and matches against all users in the database
   *
   * @return User object of the corresponding user
   */
  public User getUserFromFace() {
    ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    camera.open(0);
    Callable<User> faceIdentifier =
        () -> {
          User user;
          for (int i = 0; i < 10; i++) {
            user = identifyUser();
            if (user != null) return user;
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
        new File(
            Objects.requireNonNull(
                    getClass()
                        .getResource("/FacialRec/lbpcascades/lbpcascade_frontalface_improved.xml"))
                .getFile());

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
}
