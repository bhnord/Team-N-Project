package edu.wpi.TeamN.form;

import edu.wpi.TeamN.services.database.DatabaseService;
import java.io.Serializable;
import javafx.scene.Node;

public abstract class FormElement implements Serializable {
  public abstract Node build(DatabaseService db);

  public abstract boolean check();

  public abstract boolean validate();

  public abstract String getValue();

  private boolean required;
  private String question;
  private String help;

  public FormElement(boolean required, String question, String help) {
    this.required = required;
    this.question = question;
    this.help = help;
  }

  public boolean is_required() {
    return required;
  }

  public String getName() {
    return question;
  }

  public String getHelp() {
    return help;
  }
}
