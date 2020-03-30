package venn.diagram;

import java.io.Serializable;

public class Properties implements Serializable {
    private String text;
    private String description;
    private double x;
    private double y;
    private String fontColor;
    private String fontName;
    private double fontSize;

    public Properties(String text, double x, double y, String fontColor, String fontName, double fontSize) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.fontColor = fontColor;
        this.fontName = fontName;
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getFontName() {
        return fontName;
    }

    public double getFontSize() {
        return fontSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

