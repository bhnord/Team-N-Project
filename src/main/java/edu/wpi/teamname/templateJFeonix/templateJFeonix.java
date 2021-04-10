package edu.wpi.teamname.templateJFeonix;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.validation.RequiredFieldValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class templateJFeonix implements Initializable {
  @FXML private JFXHamburger ham1;

  @FXML private JFXTextField txtUsername;

  public void initialize(URL url, ResourceBundle rb) {
    /** Hamburger ICON for menus, goes from hamburger to x back to hamburger**/
    HamburgerBasicCloseTransition basicClose = new HamburgerBasicCloseTransition(ham1);
    basicClose.setRate(-1);
    ham1.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          basicClose.setRate(basicClose.getRate() * -1);
          basicClose.play();
        });
    /**USERNAME input**/
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    txtUsername.getValidators().add(reqInputValid);
    txtUsername
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) txtUsername.validate();
            });
  }
}
