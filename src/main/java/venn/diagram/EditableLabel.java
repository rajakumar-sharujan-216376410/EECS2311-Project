
package venn.diagram;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class EditableLabel extends Label implements SelectableNode{
   
    TextField textField = new TextField();
    String backup = "enter text";
    private double x;
    private double y;
    mainFXMLController controller;
    
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
            else if(e.getClickCount() == 1){
               this.textFillProperty().bind(mainFXMLController.getInstance().getFontColor().valueProperty()); 
               this.textFillProperty().unbind();
            }
            this.textFillProperty().unbind();
        });
        this.setOnMouseExited((event) -> {
           
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
            "Comfortaa",
            "Impact",
            "Verdana",
            "Aerial",
            "Comic Sans MS",
            "Courier New",
            "Georgia",
            "Times New Roman",
            "Trebuchet MS",
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
        
//        if(this.isFocused()){
//            System.out.println("Focused");
//           this.setOnKeyReleased((event) -> {
//               if(event.getCode().equals(KeyCode.DELETE)){
//                   ap.getChildren().remove(this);
//               }
//           });
//        }
    }
    
    public void makeDragAndDrop(){
        
        this.setOnMousePressed((event) -> {
            x = this.getLayoutX() - event.getSceneX();
            y = this.getLayoutY() - event.getSceneY();
        });
        
        this.setOnMouseDragged((event) -> {
            this.setLayoutX(event.getSceneX() + x);
            this.setLayoutY(event.getSceneY() + y);
        });
    }

    @Override
    public boolean requestSelection(boolean select) {
        return true;
    }

    @Override
    public void notifySelection(boolean select) {
        if(select){
            mainFXMLController.getInstance().getFontSize().textProperty().addListener((observable) -> {
                this.setFont(Font.font(Integer.parseInt(mainFXMLController.getInstance().getFontSize().getText())));
            });
            this.setTextFill(Color.GREEN);
        }
        else{
            this.setFont(Font.font(Integer.parseInt(controller.getFontSizeField().getText())));
            this.setTextFill(Color.BLUE);
        }
    }
   
}









