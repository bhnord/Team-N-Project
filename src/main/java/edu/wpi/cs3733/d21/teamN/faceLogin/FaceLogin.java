package edu.wpi.cs3733.d21.teamN.faceLogin;

import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class FaceLogin extends FaceRec {
  private final VideoCapture camera = new VideoCapture();
  private boolean cancelLogin = false;

  public FaceLogin(DatabaseService db) {
    super(db);
  }

  /**
   * Takes photos of the user and matches against all users in the database
   *
   * @return User object of the corresponding user
   */
  public User getUserFromFace() {
    camera.open(0);
    User user;
    for (int i = 0; i < 10; i++) {
      user = identifyUser();
      if (user != null) {
        camera.release();
        return user;
      } else if (cancelLogin) return null;
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    camera.release();
    return null;
    //    ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    //    Callable<User> faceIdentifier =
    //        () -> {
    //          User user;
    //          for (int i = 0; i < 10; i++) {
    //            user = identifyUser();
    //            if (user != null) return user;
    //            Thread.sleep(30);
    //          }
    //          return null;
    //        };
    //    Future<User> userFuture = timer.schedule(faceIdentifier, 0, TimeUnit.MILLISECONDS);
    //    try {
    //      User user = userFuture.get();
    //      timer.shutdown();
    //      camera.release();
    //      return user;
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      camera.release();
    //      timer.shutdown();
    //      return null;
  }

  private User identifyUser() {
    String classifier = getClassifier();
    if (classifier.equals("")) {
      displayFatalError("Unable to find classifier!");
      cancelLogin = true;
      return null;
    }

    CascadeClassifier faceDetector = new CascadeClassifier(classifier);

    if (!camera.isOpened()) {
      displayFatalError("No camera detected!");
      cancelLogin = true;
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
