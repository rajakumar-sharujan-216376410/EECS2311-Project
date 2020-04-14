package venn.diagram;

import java.io.Serializable;

public class Properties implements Serializable {
    private String text;
    private String description;
    private double x;
    private double y;
    private String fontColor;
    private String leftColor;
    private String rightColor;
    private double sliderValue;
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

    public String getLeftColor() {
        return leftColor;
    }

    public void setLeftColor(String leftColor) {
        this.leftColor = leftColor;
    }

    public String getRightColor() {
        return rightColor;
    }

    public void setRightColor(String rightColor) {
        this.rightColor = rightColor;
    }

    public double getSliderValue() {
        return sliderValue;
    }

    public void setSliderValue(double sliderValue) {
        this.sliderValue = sliderValue;
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
