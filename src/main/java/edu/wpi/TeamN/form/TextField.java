package edu.wpi.TeamN.form;

import javafx.scene.Node;

public class TextField extends FormElement{

    public TextField(boolean required, String question, String help) {
        super(required, question, help);
    }

    @Override
    public Node build() {
        return null;
    }

    @Override
    public boolean check() {
        return false;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public String getValue() {
        return null;
    }
}
