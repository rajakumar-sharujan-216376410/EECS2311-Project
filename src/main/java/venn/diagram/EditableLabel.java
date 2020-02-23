package venn.diagram;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class EditableLabel extends Label{
    
    TextField textField = new TextField();
    String backup = "";
    
    public EditableLabel(){
        this("");
    };
    
    public EditableLabel(String str){
        super(str);
        
        this.setOnMouseClicked((e) -> {
            if(e.getClickCount() == 2){
                textField.setText(backup = this.getText());
                this.setGraphic(textField);
                this.setText("");
                textField.requestFocus();
            }
        });
        
        textField.focusedProperty().addListener((prop, o, n) -> {
            if(!n){
                toLabel();
            }
        });
        
        textField.setOnKeyReleased((event) -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                toLabel();
            }
            else if(event.getCode().equals(KeyCode.ESCAPE)){
                textField.setText(backup);
                toLabel();
            }
        });
    }
    
    public void toLabel(){
        this.setGraphic(null);
        this.setText(textField.getText());
    }
    
    public void editColor(JFXColorPicker cp){
        this.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 1) {
                
                this.textFillProperty().bind(cp.valueProperty());
            }
            else
                this.textFillProperty().unbind();
        });
        this.setOnMouseExited((event) -> {
            this.textFillProperty().unbind();
        });
    }
    
    public void setFonts(JFXComboBox<String> fontCombo){
        String[] fonts = {
            "Roboto",
            "Sans-serif",
            "Serif",
            "Century Gothic"
        };
        List<String> fontList = Arrays.asList(fonts);
        
         //adding thea above array to list
        ObservableList<String> fontItems=FXCollections.observableArrayList(fontList);
        fontCombo.getItems().addAll(fontItems);
    }
    
    //deleting the label on button pressed
    public void delete(Button btn, AnchorPane ap){
        btn.setOnAction((event) -> {
            ap.getChildren().remove(this);
        });
