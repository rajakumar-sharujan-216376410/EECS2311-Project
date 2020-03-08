package venn.diagram;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.scene.control.skin.LabeledText;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


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
    @FXML
    private StackPane stackPane;
    
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
    
    public JFXComboBox< String> getFontDropdown(){
        return fontdropdown;
    }
    
    Persistent persistent;
    
    //holding a list of all editable fields
    Map<Integer, EditableLabel> list = new HashMap<>();
    
    //holds the selected label
    LabeledText selectedLabel;
    
    EditableLabel editableLabel;
    
    //hold the number of labels
    int labelCount = 0;
    
    //holds the increment for the x cordinate
    double yvalueIncrement = 0.0;
    double xValueMargin = 5;
    UtilityActions utils = new UtilityActions();
    ObservableList<EditableLabel> observableList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //setting the fonts specs in the font family selector combobox
        utils.setFonts(fontdropdown); 
        
        
       circleRadiusSlider.setMin(150);
       circleRadiusSlider.setMax(250);
       firstCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
       secondCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
       secondCircle.translateXProperty().bind(circleRadiusSlider.valueProperty().subtract(100));
       
       //binding to the color properties of the circles
       leftContainerPicker.setValue(Color.DARKORANGE);
       rightContainerPicker.setValue(Color.LAWNGREEN);
       firstCircle.fillProperty().bind(leftContainerPicker.valueProperty());
       secondCircle.fillProperty().bind(rightContainerPicker.valueProperty());
       
       
//       SelectionHandler selectionHandler = new SelectionHandler(writeToContainer);
//       writeToContainer.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMousePressedEventHandler());
//               
       //setting the color of the fontcolor chooser to black by defalt
       fontColor.setValue(Color.BLACK);
       
           writeToContainer.setOnMouseClicked((evt) -> {
            
            if(evt.getButton() == MouseButton.SECONDARY){
                
                
                LabeledText labeledText = null;
            if(evt.getPickResult().getIntersectedNode() instanceof LabeledText){

                utils.unBindAll(list);
                selectedLabel = (LabeledText) evt.getPickResult().getIntersectedNode();
                System.out.println(selectedLabel.getText());
                
            }
            else{
                
                System.out.println("no node selected");
            }
        }
        });
           
           if(selectedLabel != null){
               if (!list.isEmpty()) {
                   utils.unBindAll(list);
               }
               utils.bindTo(selectedLabel);
           }
           

    }    

    @FXML
    private void addTextField(ActionEvent event) {
    
         editableLabel = new EditableLabel("enter text here");
         
         //setting a new position for the editableLabel
         editableLabel.setTranslateY(yvalueIncrement);
         editableLabel.setTranslateX(xValueMargin);
         yvalueIncrement +=30;
         //create a list of editable label to keep track and count
        list.put(labelCount+1, editableLabel);
       
        observableList.add(editableLabel);
    
       
        
        labelCount+=1;
        writeToContainer.getChildren().add(editableLabel);
        editableLabel.setFont(Font.font(fontdropdown.getValue(), Integer.parseInt(fontSize.getText())));
        editableLabel.textFillProperty().bind(fontColor.valueProperty());
        editableLabel.setFont(Font.font(fontdropdown.getValue(), Integer.parseInt(fontSize.getText())));
       
        
        
        
        fontdropdown.valueProperty().addListener((observable) -> {
            editableLabel.setFont(new Font(fontdropdown.getValue(), Integer.parseInt(fontSize.getText())));
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
        FileChooser fc = new FileChooser();
        
        fc.setTitle("Save as pdf");
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("save document as", "*.pdf");
        fc.getExtensionFilters().add(extension);
        //file chooser to save the pdf document
        File file = fc.showSaveDialog(null);
        File imgFile = new File("img.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imgFile);
        } catch (IOException e) {
            System.err.println("An Error occured "+ e.getMessage());
        }
        
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        PDImageXObject pdimage;
        PDPageContentStream content;
        
        try {
            pdimage = PDImageXObject.createFromFile("img.png", doc);
            content = new PDPageContentStream(doc, page);
            content.drawImage(pdimage, 100, 100);
            content.close();
            doc.addPage(page);
            doc.save(file);
            doc.close();
            imgFile.delete();
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

    @FXML
    private void saveForEdit(ActionEvent event) {
  
        Persistent persist = new Persistent();
        
        persist.saveForEdit(list);
    }

    @FXML
    private void newProject(ActionEvent event) {
        
        Persistent persist = new Persistent();
        persist.createProject();
    }

    @FXML
    private void clearAllLabel(ActionEvent event) {
        JFXDialogLayout content = new JFXDialogLayout();
        
        //setting the heading for the dialog box
        content.setHeading(new Text("Confirm Action"));
        content.setBody(new Text("Are you sure you want to clear all text?"));
        
        //instantiating the dialog 
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, false);
        JFXButton confirmBtn = new JFXButton("delete");
        confirmBtn.setOnAction((evt) -> {
            dialog.close();
            writeToContainer.getChildren().removeIf(node -> node instanceof EditableLabel);
            
        });
        JFXButton cancelBtn = new JFXButton("cancel");
        cancelBtn.setOnAction((evt) -> {
            dialog.close();
        });
        
        //adding the button to dialog
        content.setActions(confirmBtn, cancelBtn);
        dialog.show();
        
        
    }

    @FXML
    private void openExistingProject(ActionEvent event) {
    }

    @FXML
    private void exit(ActionEvent event) {
        
        Platform.exit();
    }
    
        
}