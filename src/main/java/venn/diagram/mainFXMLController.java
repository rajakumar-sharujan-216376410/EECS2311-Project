package venn.diagram;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;


public class mainFXMLController implements Initializable {
    
    private Label label;
    @FXML
    private JFXSlider circleRadiusSlider;
    @FXML
    private Circle firstCircle;
    @FXML
    private Circle secondCircle;
    @FXML
    private AnchorPane writeToContainer;
    @FXML
    private Button addButton;
    @FXML
    private JFXColorPicker leftContainerPicker;
    @FXML
    private JFXColorPicker rightContainerPicker;
    @FXML
    private JFXColorPicker fontColor;
    @FXML
    private JFXComboBox<String> fontdropdown;
    @FXML
    private JFXTextField fontSize;
    @FXML
    private Button deleteBtn;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       circleRadiusSlider.setMin(100);
       circleRadiusSlider.setMax(250);
       firstCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
       secondCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
       secondCircle.centerXProperty().bind(secondCircle.radiusProperty().add(5));
       firstCircle.centerXProperty().bind(circleRadiusSlider.valueProperty().subtract(5));
       firstCircle.setFill(Color.GREEN);
       
       //binding to the color properties of the circles
       leftContainerPicker.setValue(Color.BLUE);
       rightContainerPicker.setValue(Color.RED);
       firstCircle.fillProperty().bind(leftContainerPicker.valueProperty());
       secondCircle.fillProperty().bind(rightContainerPicker.valueProperty());
       
    } 
    
    @FXML
    private void addTextField(ActionEvent event) {
    
        EditableLabel editableLabel = new EditableLabel("enter text here");
        writeToContainer.getChildren().add(editableLabel);
        fontColor.setValue(Color.BLACK);
        editableLabel.textFillProperty().bind(fontColor.valueProperty());
        editableLabel.setFonts(fontdropdown);
        fontdropdown.valueProperty().addListener((observable) -> {
            editableLabel.setFont(new Font(fontdropdown.getValue(), 13));
        });
        fontSize.textProperty().addListener((observable) -> {
            
            int value =Integer.parseInt(fontSize.getText());
            editableLabel.setFont(Font.font(value));
            DecimalFormat format =new DecimalFormat("#.0");
            fontSize.setTextFormatter( new TextFormatter<>(c ->
            {
                if ( c.getControlNewText().isEmpty() )
                {
                    return c;
                }

                ParsePosition parsePosition = new ParsePosition( 0 );
                Object object = format.parse( c.getControlNewText(), parsePosition );

                if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
                {
                    return null;
                }
                else
                {
                    return c;
                }
            }));
         });
        
        editableLabel.delete(deleteBtn, writeToContainer);
    }
    
}