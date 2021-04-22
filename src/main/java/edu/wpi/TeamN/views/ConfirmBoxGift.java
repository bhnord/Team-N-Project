package edu.wpi.TeamN.views;

import edu.wpi.TeamN.state.Login;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ConfirmBoxGift extends masterController implements Initializable {
  @FXML
  public void advanceHome() throws IOException {

    Login login = Login.getLogin();

    if (login.getUsername().equals("p") && login.getPassword().equals("p")) {
      super.advanceHomePatient(loader, appPrimaryScene);
    } else if (login.getUsername().equals("e") && login.getPassword().equals("e")) {
      super.advanceHome(loader, appPrimaryScene);
    } else if (login.getUsername().equals("a") && login.getPassword().equals("a")) {
      super.advanceHomeAdmin(loader, appPrimaryScene);
    }
  }
}
