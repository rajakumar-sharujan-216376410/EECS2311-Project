
package venn.diagram;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
    
    
    private static mainFXMLController instance;
    @FXML
    private HBox rootPane;
    @FXML
    private MenuItem donwloadAsPdf;
    
    public mainFXMLController(){
        instance = this;
    }
    
    public static mainFXMLController getInstance(){
        return instance;
    }
    
    public AnchorPane getWriteToContainer(){
        return writeToContainer;
    }
    
    public JFXTextField getFontSize(){
        return fontSize;
    }
    
    public JFXColorPicker getFontColor(){
        return fontColor;
    }
    
    //holding a list of all editable fields
    Map<Integer, EditableLabel> list = new HashMap<>();
    
    //holds the selected label
    EditableLabel selectedLabel;
    
    EditableLabel editableLabel;
    
    //hold the number of labels
    int labelCount = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       circleRadiusSlider.setMin(100);
       circleRadiusSlider.setMax(200);
       firstCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
       secondCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
//       secondCircle.centerXProperty().bind(secondCircle.radiusProperty().add(5));
//       firstCircle.centerXProperty().bind(circleRadiusSlider.valueProperty().subtract(5));
       secondCircle.translateXProperty().bind(circleRadiusSlider.valueProperty().subtract(100));
//       firstCircle.translateXProperty().bind(circleRadiusSlider.valueProperty().subtract(1.5));
       
       //binding to the color properties of the circles
       leftContainerPicker.setValue(Color.BLUE);
       rightContainerPicker.setValue(Color.RED);
       firstCircle.fillProperty().bind(leftContainerPicker.valueProperty());
       secondCircle.fillProperty().bind(rightContainerPicker.valueProperty());
       
       
       SelectionHandler selectionHandler = new SelectionHandler(writeToContainer);
       writeToContainer.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMousePressedEventHandler());
               
       
       //binding the selected label to label properties
       if(selectedLabel != null){
           editableLabel.textFillProperty().unbind();
           selectedLabel.textFillProperty().bind(fontColor.valueProperty());
       }
       
       
    }    

    @FXML
    private void addTextField(ActionEvent event) {
    
         editableLabel = new EditableLabel("enter text here");
        
        //add the created editableLabel to list
        list.put(labelCount+1, editableLabel);
        
        labelCount+=1;
        writeToContainer.getChildren().add(editableLabel);
        editableLabel.setFont(Font.font(fontdropdown.getValue(), Integer.parseInt(fontSize.getText())));
        fontColor.setValue(Color.BLACK);
        editableLabel.textFillProperty().bind(fontColor.valueProperty());
        editableLabel.setFonts(fontdropdown);
        editableLabel.setFont(Font.font(fontdropdown.getValue()));
       
        
        
        
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
        
        editableLabel.makeDragAndDrop();
        
    }
    
    public JFXTextField getFontSizeField(){
        return fontSize;
    }

    @FXML
    private void saveImage(ActionEvent event) {
        
        // here we make image from anchorpane and add it to scene, can be repeated :)
                WritableImage snapshot = writeToContainer.snapshot(new SnapshotParameters(), null);

        //creating a filechooser to save the iamge file
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, "save image as");
        
        //set extension filter 
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("png images", "*.png");
        
        fileChooser.getExtensionFilters().add(extensionFilter);
        
        //show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            SaveFile(snapshot, file);
        }
         }

    
    //save image file in a png format
    private void SaveFile(WritableImage snapshot, File file) {
       try {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
        ImageIO.write(bufferedImage, "png", file);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }

    @FXML
    private void savePDF(ActionEvent event) {
        
        //creating a writable image
        WritableImage image = writeToContainer.snapshot(new SnapshotParameters(), null);
        
        //file chooser to save the pdf document
        File file = new File("pdfImage.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {

        }
        
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        PDImageXObject pdimage;
        PDPageContentStream content;
        
        try {
            pdimage = PDImageXObject.createFromFile("pdfImage.png", doc);
            content = new PDPageContentStream(doc, page);
            content.drawImage(pdimage, 100, 100);
            content.close();
            doc.addPage(page);
            doc.save("pdf_file.pdf");
            file.delete();
        } catch (IOException e) {
            Logger.getLogger(mainFXMLController.class.getName()).log(Level.SEVERE, null, e);
        }
        
        
        
    }
    
    private static void configureFileChooser(final FileChooser fileChooser, String title){                           
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        ); 
    }
}

//test






