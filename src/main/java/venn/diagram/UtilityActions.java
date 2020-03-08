package venn.diagram;

import com.jfoenix.controls.JFXComboBox;
import com.sun.javafx.scene.control.skin.LabeledText;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class UtilityActions {
    
    public UtilityActions(){};
    
    //delete selected label on button click
    public void deleteSelectedLabel(LabeledText label, AnchorPane anchorPane){
        
        anchorPane.getChildren().remove(label);
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
     
     public void unBindAll(Map<Integer, EditableLabel> map){
         Set< Integer> keys = map.keySet();
         
         keys.forEach((key) -> {
             map.get(key).textFillProperty().unbind();
             map.get(key).textProperty().removeListener((observable) -> {
             map.get(key).setFont(new Font(mainFXMLController.getInstance().getFontDropdown()
                     .getValue(), Integer.parseInt(mainFXMLController.getInstance().getFontSize().getText())));

             });
         });
     }
     
     public void bindTo( LabeledText label){
         label.fillProperty().bind(mainFXMLController.getInstance().getFontColor().valueProperty());
         label.textProperty().addListener((observable) -> {
             label.setFont(Font.font(
                     mainFXMLController.getInstance().getFontDropdown().getValue(), 
                     Integer.parseInt(mainFXMLController.getInstance().getFontSize().getText())));
         });
     }
     
}