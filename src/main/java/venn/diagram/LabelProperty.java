package venn.diagram;

import javafx.scene.text.Font;

public class LabelProperty {
    
    //setting the valid property to save and identify a label
    private int id;
    private String text;
    private double xCordinate;
    private double yCordinate;
    private Font font;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(EditableLabel label) {
        this.text = label.getText();
    }

    public double getxCordinate() {
        return xCordinate;
    }

    public void setxCordinate(EditableLabel label) {
        this.xCordinate = label.getBoundsInParent().getMinX();
    }

    public double getyCordinate() {
        return yCordinate;
    }

    public void setyCordinate(EditableLabel label) {
        this.yCordinate =label.getBoundsInParent().getMinY();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(EditableLabel label) {
        this.font = label.getFont();
    }
    
}



