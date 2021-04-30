package edu.wpi.TeamN.map;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;

public class UserPreferences {
    private Paint nodeColor;
    private Paint EXIT;

    public Paint getNodeColor() {
        return nodeColor;
    }

    public void setNodeColor(Paint nodeColor) {
        this.nodeColor = nodeColor;
    }

    public Paint getEXIT() {
        return EXIT;
    }

    public void setEXIT(Paint EXIT) {
        this.EXIT = EXIT;
    }

    public Paint getELEV() {
        return ELEV;
    }

    public void setELEV(Paint ELEV) {
        this.ELEV = ELEV;
    }

    public Paint getSTAI() {
        return STAI;
    }

    public void setSTAI(Paint STAI) {
        this.STAI = STAI;
    }

    public Paint getPathColor() {
        return pathColor;
    }

    public void setPathColor(Paint pathColor) {
        this.pathColor = pathColor;
    }

    public Paint getSelectedNodeColor() {
        return selectedNodeColor;
    }

    public void setSelectedNodeColor(Paint selectedNodeColor) {
        this.selectedNodeColor = selectedNodeColor;
    }

    public double getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(double nodeSize) {
        this.nodeSize = nodeSize;
    }

    public double getPathSize() {
        return pathSize;
    }

    public void setPathSize(double pathSize) {
        this.pathSize = pathSize;
    }

    private Paint ELEV;
    private Paint STAI;
    private Paint pathColor;
    private Paint selectedNodeColor;
    private double nodeSize;
    private double pathSize;
}
