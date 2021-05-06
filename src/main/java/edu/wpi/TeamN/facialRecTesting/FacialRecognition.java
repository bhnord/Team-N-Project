package edu.wpi.TeamN.facialRecTesting;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.services.database.DatabaseService;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import nu.pattern.OpenCV;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FacialRecognition implements Initializable {
  /** Directory on the disk containing faces */
  @Inject private DatabaseService db;

  @Inject private FXMLLoader loader;

  private static final File DATABASE = new File("Database");
  @FXML private ImageView imageView;
  @FXML private JFXButton saveButton;
  @FXML private JFXTextField nameField;
  @FXML private StackPane stackPane;
  private boolean shouldSave = false;
  private ScheduledExecutorService timer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    OpenCV.loadLocally();
  }

  @FXML
  private void toggleSave() {
    shouldSave = true;
  }

  @FXML
  private void capture() {
    File classifier =
        new File("src/main/resources/FacialRec/lbpcascades/lbpcascade_frontalface_improved.xml");

    if (!classifier.exists()) {
      displayFatalError("Unable to find classifier!");
      return;
    }

    CascadeClassifier faceDetector = new CascadeClassifier(classifier.toString());
    VideoCapture camera = new VideoCapture();
    camera.open(0);

    if (!camera.isOpened()) {
      displayFatalError("No camera detected!");
      return;
    }

    Stage stage = (Stage) stackPane.getScene().getWindow();
    stage.setOnCloseRequest(
        e -> {
          camera.release();
          Platform.exit();
          System.exit(0);
        });

    if (!DATABASE.exists()) DATABASE.mkdir();
    //    ImageFrame frame = new ImageFrame();

    //    while () {
    //      Mat rawImage = new Mat();
    //      camera.read(rawImage);
    //      Mat newImage = detectFaces(rawImage, faceDetector);
    //      imageView.setImage(mat2Image(newImage));
    //      //      frame.showImage(newImage);
    //    }
    Runnable frameGrabber =
        new Runnable() {

          @Override
          public void run() {
            Mat rawImage = new Mat();
            camera.read(rawImage);
            Mat newImage = detectFaces(rawImage, faceDetector);
            imageView.setImage(mat2Image(newImage));
          }
        };

    this.timer = Executors.newSingleThreadScheduledExecutor();
    this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

    //        camera.release();
  }

  private Mat detectFaces(Mat image, CascadeClassifier faceDetector) {
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    Rect[] faces = faceDetections.toArray();
    String name = nameField.getText();
    Scalar color = new Scalar(0, 0, 255);

    for (Rect face : faces) {
      Mat croppedImage = new Mat(image, face);

      if (shouldSave) saveImage(croppedImage, name);

      Imgproc.putText(image, "ID: " + identifyFace(croppedImage), face.tl(), Font.BOLD, 3, color);
      Imgproc.rectangle(image, face.tl(), face.br(), color);
    }

    int faceCount = faces.length;
    String message = faceCount + " face" + (faceCount == 1 ? "" : "s") + " detected!";
    Imgproc.putText(image, message, new Point(3, 25), Font.BOLD, 2, color);

    return image;
  }

  private String identifyFace(Mat image) {
    int errorThreshold = 3;
    int mostSimilar = -1;
    File mostSimilarFile = null;

    for (File capture : Objects.requireNonNull(DATABASE.listFiles())) {
      int similarities = compareFaces(image, capture.getAbsolutePath());

      if (similarities > mostSimilar) {
        mostSimilar = similarities;
        mostSimilarFile = capture;
      }
    }

    if (mostSimilarFile != null && mostSimilar > errorThreshold) {
      String faceID = mostSimilarFile.getName();
      String delimiter = faceID.contains(" (") ? "(" : ".";
      return faceID.substring(0, faceID.indexOf(delimiter)).trim();
    } else return "???";
  }

  private int compareFaces(Mat currentImage, String fileName) {
    Mat compareImage = Imgcodecs.imread(fileName);
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

  private void saveImage(Mat image, String name) {
    File destination;
    String extension = ".png";
    String baseName = DATABASE + File.separator + name;
    File basic = new File(baseName + extension);

    if (!basic.exists()) destination = basic;
    else {
      int index = 0;

      do destination = new File(baseName + " (" + index++ + ")" + extension);
      while (destination.exists());
    }

    Imgcodecs.imwrite(destination.toString(), image);
    shouldSave = false;
  }

  private void displayFatalError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Fatal Error");
    alert.setHeaderText("Fatal Error");
    alert.setContentText(message);
    alert.showAndWait();
  }

  private Image mat2Image(Mat frame) {
    try {
      return SwingFXUtils.toFXImage(convertMatToBufferedImage(frame), null);
    } catch (Exception e) {
      System.err.println("Cannot convert the Mat obejct: " + e);
      return null;
    }
  }

  private BufferedImage convertMatToBufferedImage(Mat matrix) {
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
}
