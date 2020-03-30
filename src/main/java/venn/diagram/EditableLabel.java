
package venn.diagram;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


public class EditableLabel extends Label {//implements SelectableNode {
   
    private TextField textField = new TextField();
    public String desc;
    private Tooltip tooltip;
    private Rectangle bounds;
    private String backup = "enter text";
    private double x;
    private double y;
    private boolean dragging = false;
    private boolean red = false;
    private double greenX = 0,greenY = 0;
    public Color last;

    
    public EditableLabel(String str,String desc){
        super(str);
        this.desc = desc;
        bounds = new Rectangle();
        bounds.widthProperty().bind(widthProperty());
        bounds.heightProperty().bind(heightProperty());
        tooltip = new Tooltip(desc);
        this.setTooltip(tooltip);
        this.setOnMouseClicked((e) -> {
            if(e.getClickCount() == 2){
                this.setStyle("-fx-border: 0; -fx-border-color: black");
                textField.setText(backup = this.getText());
                this.setGraphic(textField);
                this.setText("");
                textField.requestFocus();
                e.consume();
            }
            else if(e.getClickCount() == 1){
                if (e.isShortcutDown()){
                    if (mainFXMLController.getInstance().selectedLabel!=null) {
                        mainFXMLController.getInstance().selectionList.add(
                                mainFXMLController.getInstance().selectedLabel
                        );
                    }
                    mainFXMLController.getInstance().selectedLabel = null;
                    mainFXMLController.getInstance().selectionList.add(this);
                    this.setStyle("-fx-border: 5; -fx-border-color: black");
                }else {
                    if (mainFXMLController.getInstance().selectedLabel != null) {
                        mainFXMLController.getInstance().selectedLabel.setStyle("-fx-border: 0");
                    }
                    mainFXMLController.getInstance().selectedLabel = this;
                    this.setStyle("-fx-border: 5; -fx-border-color: black");
                }
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
    
    public void deleteSelectedLabel(Button btn, AnchorPane ap){
        btn.setOnAction((event) -> {
            ap.getChildren().remove(getSelectedLabel());
            System.out.println("selectable node "+ getSelectedLabel().getText());
        });
    }
    
    private EditableLabel getSelectedLabel(){
        EditableLabel defaultLabel = new EditableLabel("","");
//        if(selectedLabel == null)
//            return defaultLabel;
//        else
//        return selectedLabel;
        return defaultLabel;
    }
    public void makeDragAndDrop(){
        
        this.setOnMousePressed((event) -> {
            x = this.getLayoutX() - event.getSceneX();
            y = this.getLayoutY() - event.getSceneY();
        });
        
        this.setOnMouseDragged((event) -> {
            dragging = true;
            this.setLayoutX(event.getSceneX() + x);
            this.setLayoutY(event.getSceneY() + y);
            bounds.setLayoutX(event.getSceneX() +x);
            bounds.setLayoutY(event.getSceneY() +y);
            ObservableList<EditableLabel> textElements = mainFXMLController.getInstance().observableList;
            for (EditableLabel e:textElements){
                if (e != this) {
                    if (
                            ((this.getLayoutX() >= e.getLayoutX() & this.getLayoutX() <= e.getLayoutX() + e.getWidth())
                                    | (this.getLayoutX() < e.getLayoutX() & this.getLayoutX() + this.getWidth() > e.getLayoutX()))
                                    &
                                    ((this.getLayoutY() >= e.getLayoutY() & this.getLayoutY() <= e.getLayoutY() + e.getHeight())
                                            | (this.getLayoutY() < e.getLayoutY() & this.getLayoutY() + this.getHeight() > e.getLayoutY()))

                    ) {
                        this.setTextFill(Color.RED);
                        red = true;
                    } else {
                        this.setTextFill(Color.GREEN);
                        greenX = event.getSceneX() + x;
                        greenY = event.getSceneY() + y;
                        red = false;
                    }
                }
            }
        });
        this.setOnMouseReleased(e->{
            if (dragging){
                dragging = false;
                if (red){
                    this.setLayoutX(greenX);
                    this.setLayoutY(greenY);
                    bounds.setLayoutX(greenX);
                    bounds.setLayoutY(greenY);
                    System.out.println("this.getTextFill().toString() = " + this.getTextFill().toString());
                    System.out.println("last = " + last.toString());
                    this.setTextFill(last);
                }
                System.out.println("this.getTextFill().toString() = " + this.getTextFill().toString());
                System.out.println("last = " + last.toString());
                this.setTextFill(last);
            }
        });
    }

//    @Override
//    public boolean requestSelection(boolean select) {
//        return true;
//    }
//
//    @Override
//    public void notifySelection(boolean select) {
//        if(select){
//            mainFXMLController.getInstance().getFontSize().textProperty().addListener((observable) -> {
//                this.setFont(Font.font(Integer.parseInt(mainFXMLController.getInstance().getFontSize().getText())));
//            });
//            this.setTextFill(Color.GREEN);
//        }
//        else{
//            this.setFont(Font.font(Integer.parseInt(mainFXMLController.getInstance().getFontSizeField().getText())));
//            this.setTextFill(Color.BLUE);
//        }
//    }

    public Rectangle getBounds() {
        return bounds;
    }
}

