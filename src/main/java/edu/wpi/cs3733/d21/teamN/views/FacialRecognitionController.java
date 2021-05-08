package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.faceLogin.FaceEnroller;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import nu.pattern.OpenCV;

public class FacialRecognitionController extends MasterController implements Initializable {
  private static final File DATABASE = new File("Database");
  /** Directory on the disk containing faces */
  @Inject private DatabaseService db;

  @Inject private FXMLLoader loader;
  @FXML private ImageView imageView;
  @FXML private JFXButton saveButton;
  @FXML private JFXTextField nameField;
  @FXML private StackPane stackPane;
  private boolean shouldSave = false;
  private HashMap<BufferedImage, Integer> faceImages;
  FaceEnroller faceEnroller;

  //  private Mat bufferedImageToMat(BufferedImage bi) {
  //    Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
  //    byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
  //    mat.put(0, 0, data);
  //    return mat;
  //  }

  @FXML AnchorPane anchorPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    OpenCV.loadLocally();
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Register");

    faceEnroller = new FaceEnroller(db, imageView);
    db.setLoggedInUser(db.getUserByUsername("Michael"));
  }

  @FXML
  private void toggleSave() {
    faceEnroller.saveFace();
  }

  @FXML
  private void capture() {
    faceEnroller.startEnroller();
    //        FaceLogin facialRecognition = new FaceLogin(db);
    //        User user = facialRecognition.getUserFromFace();
    //        System.out.println(user);
    //        File classifier =
    //                new
    // File("src/main/resources/FacialRec/lbpcascades/lbpcascade_frontalface_improved.xml");
    //
    //        if (!classifier.exists()) {
    //            displayFatalError("Unable to find classifier!");
    //            return;
    //        }
    //
    //        CascadeClassifier faceDetector = new CascadeClassifier(classifier.toString());
    //        VideoCapture camera = new VideoCapture();
    //        camera.open(0);
    //
    //        if (!camera.isOpened()) {
    //            displayFatalError("No camera detected!");
    //            return;
    //        }
    //
    //        faceImages = db.getAllFaces();
    //
    //        Stage stage = (Stage) stackPane.getScene().getWindow();
    //        stage.setOnCloseRequest(
    //                e -> {
    //                    camera.release();
    //                    Platform.exit();
    //                    System.exit(0);
    //                });
    //
    //        //    if (!DATABASE.exists()) DATABASE.mkdir();
    //        Runnable frameGrabber =
    //                () -> {
    //                    Mat rawImage = new Mat();
    //                    camera.read(rawImage);
    //                    Mat newImage = detectFaces(rawImage, faceDetector);
    //                    imageView.setImage(mat2Image(newImage));
    //                };
    //
    //        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    //        timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
  }
  //
  //    private Mat detectFaces(Mat image, CascadeClassifier faceDetector) {
  //        MatOfRect faceDetections = new MatOfRect();
  //        faceDetector.detectMultiScale(image, faceDetections);
  //        Rect[] faces = faceDetections.toArray();
  //        Scalar color = new Scalar(0, 0, 255);
  //
  //        for (Rect face : faces) {
  //            Mat croppedImage = new Mat(image, face);
  //
  //            if (shouldSave) saveImage(croppedImage);
  //
  //            Imgproc.putText(image, "ID: " + identifyFace(croppedImage), face.tl(), Font.BOLD, 3,
  // color);
  //            Imgproc.rectangle(image, face.tl(), face.br(), color);
  //        }
  //
  //        int faceCount = faces.length;
  //        String message = faceCount + " face" + (faceCount == 1 ? "" : "s") + " detected!";
  //        Imgproc.putText(image, message, new Point(3, 25), Font.BOLD, 2, color);
  //
  //        return image;
  //    }
  //
  //    private String identifyFace(Mat image) {
  //        int errorThreshold = 2;
  //        int mostSimilar = -1;
  //        int mostSimilarUserID = -1;
  //
  //        for (BufferedImage capture : faceImages.keySet()) {
  //            int similarities = compareFaces(image, capture);
  //
  //            if (similarities > mostSimilar) {
  //                mostSimilar = similarities;
  //                mostSimilarUserID = faceImages.get(capture);
  //            }
  //        }
  //
  //        if (mostSimilarUserID != -1 && mostSimilar > errorThreshold) {
  //            User user = db.getUserById(mostSimilarUserID);
  //            return user.getUsername();
  //        } else return "???";
  //    }
  //
  //    private int compareFaces(Mat currentImage, BufferedImage image) {
  //        Mat compareImage = bufferedImageToMat(image);
  //
  //        ORB orb = ORB.create();
  //        int similarity = 0;
  //
  //        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
  //        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
  //        orb.detect(currentImage, keypoints1);
  //        orb.detect(compareImage, keypoints2);
  //
  //        Mat descriptors1 = new Mat();
  //        Mat descriptors2 = new Mat();
  //        orb.compute(currentImage, keypoints1, descriptors1);
  //        orb.compute(compareImage, keypoints2, descriptors2);
  //
  //        if (descriptors1.cols() == descriptors2.cols()) {
  //            MatOfDMatch matchMatrix = new MatOfDMatch();
  //            DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING)
  //                    .match(descriptors1, descriptors2, matchMatrix);
  //
  //            for (DMatch match : matchMatrix.toList()) if (match.distance <= 50) similarity++;
  //        }
  //
  //        return similarity;
  //    }
  //
  //    private void saveImage(Mat image) {
  //        System.out.println(db.updateUserImage(301, convertMatToBufferedImage(image)));
  //        //    File destination;
  //        //    String extension = ".png";
  //        //    String baseName = DATABASE + File.separator + name;
  //        //    File basic = new File(baseName + extension);
  //        //
  //        //    if (!basic.exists()) destination = basic;
  //        //    else {
  //        //      int index = 0;
  //        //
  //        //      do destination = new File(baseName + " (" + index++ + ")" + extension);
  //        //      while (destination.exists());
  //        //    }
  //        //    Imgcodecs.imwrite(destination.toString(), image);
  //        shouldSave = false;
  //    }
  //
  //    private void displayFatalError(String message) {
  //        Alert alert = new Alert(Alert.AlertType.ERROR);
  //        alert.setTitle("Fatal Error");
  //        alert.setHeaderText("Fatal Error");
  //        alert.setContentText(message);
  //        alert.showAndWait();
  //    }
  //
  //    private Image mat2Image(Mat frame) {
  //        try {
  //            return SwingFXUtils.toFXImage(convertMatToBufferedImage(frame), null);
  //        } catch (Exception e) {
  //            //      System.err.println("Cannot convert the Mat obejct: " + e);
  //            return null;
  //        }
  //    }
  //
  //    private BufferedImage convertMatToBufferedImage(Mat matrix) {
  //        int width = matrix.width();
  //        int height = matrix.height();
  //        int type = matrix.channels() != 1 ? BufferedImage.TYPE_3BYTE_BGR :
  // BufferedImage.TYPE_BYTE_GRAY;
  //
  //        if (type == BufferedImage.TYPE_3BYTE_BGR)
  //            Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2RGB);
  //
  //        byte[] data = new byte[width * height * (int) matrix.elemSize()];
  //        matrix.get(0, 0, data);
  //
  //        BufferedImage out = new BufferedImage(width, height, type);
  //        out.getRaster().setDataElements(0, 0, width, height, data);
  //
  //        return out;
  //    }
}
