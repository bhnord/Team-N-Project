package edu.wpi.TeamN.facialRecTesting;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.services.database.users.User;
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
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
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
  private HashMap<BufferedImage, Integer> faceImages;

  private Image testImage;

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

    faceImages = db.getAllFaces();

    Stage stage = (Stage) stackPane.getScene().getWindow();
    stage.setOnCloseRequest(
        e -> {
          camera.release();
          Platform.exit();
          System.exit(0);
        });

    //    if (!DATABASE.exists()) DATABASE.mkdir();
    Runnable frameGrabber =
        () -> {
          Mat rawImage = new Mat();
          camera.read(rawImage);
          Mat newImage = detectFaces(rawImage, faceDetector);
          if (shouldSave) {
            imageView.setImage(testImage);
          }
          imageView.setImage(mat2Image(newImage));
        };

    this.timer = Executors.newSingleThreadScheduledExecutor();
    this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
  }

  private Mat detectFaces(Mat image, CascadeClassifier faceDetector) {
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    Rect[] faces = faceDetections.toArray();
    String name = nameField.getText();
    Scalar color = new Scalar(0, 0, 255);

    for (Rect face : faces) {
      Mat croppedImage = new Mat(image, face);

      if (shouldSave) {
        saveImage(croppedImage, name);
        //        Gson gson = new Gson();
        //        System.out.println(1);
        //        Image temp = mat2Image(croppedImage);
        //        System.out.println(2);
        //        System.out.println(convertMatToBufferedImage(croppedImage));
        //        System.out.println(3);
        //        convertMatToBufferedImage(croppedImage);
        //        System.out.println(gson.toJson(convertMatToBufferedImage(croppedImage)).length());
        //        System.out.println(4);
        //        testImage = temp;
        //        timer.shutdown();
        //        String json = gson.toJson(temp);
        //        System.out.println(3);
        //        System.out.println(json.length());
        //        System.out.println(4);
        //        testImage = gson.fromJson(json, Image.class);
        //        new Mat(Long.parseLong(substring));
        //                testImage = (mat2Image(gson.fromJson(json, Mat.class)));
        //        System.out.println(croppedImage.equals(gson.fromJson(json, Mat.class)));
        try {
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          ImageIO.write(convertMatToBufferedImage(croppedImage), "PNG", outputStream);
          ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      Imgproc.putText(image, "ID: " + identifyFace(croppedImage), face.tl(), Font.BOLD, 3, color);
      Imgproc.rectangle(image, face.tl(), face.br(), color);
    }

    int faceCount = faces.length;
    String message = faceCount + " face" + (faceCount == 1 ? "" : "s") + " detected!";
    Imgproc.putText(image, message, new Point(3, 25), Font.BOLD, 2, color);

    return image;
  }

  private String identifyFace(Mat image) {
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
      return user.getUsername();
      //      String delimiter = faceID.contains(" (") ? "(" : ".";
      //      return faceID.substring(0, faceID.indexOf(delimiter)).trim();
    } else return "???";
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

  private void saveImage(Mat image, String name) {
    System.out.println(db.updateUserImage(301, convertMatToBufferedImage(image)));
    //    File destination;
    //    String extension = ".png";
    //    String baseName = DATABASE + File.separator + name;
    //    File basic = new File(baseName + extension);
    //
    //    if (!basic.exists()) destination = basic;
    //    else {
    //      int index = 0;
    //
    //      do destination = new File(baseName + " (" + index++ + ")" + extension);
    //      while (destination.exists());
    //    }
    //    Imgcodecs.imwrite(destination.toString(), image);
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
      //      System.err.println("Cannot convert the Mat obejct: " + e);
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

  public static Mat bufferedImageToMat(BufferedImage bi) {
    Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
    byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    mat.put(0, 0, data);
    return mat;
  }
}
