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
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class mainFXMLController implements Initializable {
    
	public MenuItem undo;
    private Label label;
    @FXML
    private JFXSlider circleRadiusSlider;
    @FXML
    private Circle firstCircle;
    @FXML
    private Circle secondCircle;
    @FXML
    private Pane writeToContainer;
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

    private EditableLabel left,middle,right;

    private static final String[] ACTIONS = {"COLOR","FONT","SIZE"};
    private String lastAction;
    private EditableLabel lastSelection;
    private Font lastFont;
    private Paint lastColor;
    private double lastSize;
    private boolean canRedo = false;

    private static mainFXMLController instance;
    @FXML
    private HBox rootPane;
    @FXML
    private MenuItem donwloadAsPdf;
    @FXML
    private StackPane stackPane;
    private boolean guessMod;
    private double initX,initY;

    public mainFXMLController(){
        instance = this;
    }
    
    public static mainFXMLController getInstance(){
        return instance;
    }
    
    public Pane getWriteToContainer(){
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
    public EditableLabel selectedLabel;
    
    private EditableLabel editableLabel;
    
    //hold the number of labels
    private int labelCount = 0;
    private double scaleFactor = 0.2;
    private double locationXFactor = 1.3;
    
    //holds the increment for the x cordinate
    private double yvalueIncrement = 0.0;
    private double xValueMargin = 5;
    private UtilityActions utils = new UtilityActions();
    public ObservableList<EditableLabel> observableList = FXCollections.observableArrayList();
    public ObservableList<EditableLabel> selectionList = FXCollections.observableArrayList();
    private File saveLocation;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //setting the fonts specs in the font family selector combobox
        utils.setFonts(fontdropdown);
        initX = firstCircle.getLayoutX();
        initY = secondCircle.getLayoutX();
        writeToContainer.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue!=null & !guessMod){
                    firstCircle.setLayoutX(newValue.doubleValue() / 2 );
                    secondCircle.setLayoutX((newValue.doubleValue() / 2 ) - 50);
                }
            }
        });
        writeToContainer.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue!=null & !guessMod){
                    firstCircle.setLayoutY((newValue.doubleValue() / 2 ) );
                    secondCircle.setLayoutY((newValue.doubleValue() / 2 ));
                }
            }
        });
        // min was 150 and max was 250
       circleRadiusSlider.setMin(250);
       circleRadiusSlider.setMax(400);
       firstCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
       secondCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
       secondCircle.translateXProperty().bind(circleRadiusSlider.valueProperty().subtract(100));
       
       //binding to the color properties of the circles
       leftContainerPicker.setValue(Color.DARKORANGE);
       rightContainerPicker.setValue(Color.LAWNGREEN);
       firstCircle.fillProperty().bind(leftContainerPicker.valueProperty());
       secondCircle.fillProperty().bind(rightContainerPicker.valueProperty());

       circleRadiusSlider.valueProperty().addListener(new ChangeListener<Number>() {
           @Override
           public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
               if (newValue!=null){
                   for(EditableLabel label:observableList){
                       Bounds first = firstCircle.getBoundsInParent();
                       Bounds sec = secondCircle.getBoundsInParent();
                       Bounds labelBounds = label.getBounds().getBoundsInParent();
                       boolean firstCheck = first.intersects(labelBounds);
                       boolean secCheck = sec.intersects(labelBounds);

                       if (firstCheck){
                           Font font = label.getFont();
                           Font newFont;
                           if (newValue.doubleValue() > oldValue.doubleValue()) {
                               newFont = new Font(font.getName(), font.getSize() + scaleFactor);
                               label.setLayoutX(label.getLayoutX() - locationXFactor);
                           }else{
                               newFont = new Font(font.getName(), font.getSize() - scaleFactor);
                               label.setLayoutX(label.getLayoutX() + locationXFactor);
                           }
                           label.setFont(newFont);
                           System.out.println("first was moved");
                       }
                       else if (secCheck){
                           Font font = label.getFont();
                           Font newFont;
                           if (newValue.doubleValue() > oldValue.doubleValue()) {
                               newFont = new Font(font.getName(), font.getSize() + scaleFactor);
                               label.setLayoutX(label.getLayoutX() + locationXFactor);
                           }else{
                               newFont = new Font(font.getName(), font.getSize() - scaleFactor);
                               label.setLayoutX(label.getLayoutX() - locationXFactor);
                           }
                           label.setFont(newFont);
                           System.out.println("Sec was moved");
                       }
                   }
               }
           }
       });
       
       
//       SelectionHandler selectionHandler = new SelectionHandler(writeToContainer);
//       writeToContainer.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMousePressedEventHandler());
//               
       //setting the color of the fontcolor chooser to black by defalt
       fontColor.setValue(Color.BLACK);
       fontColor.valueProperty().addListener(new ChangeListener<Color>() {
           @Override
           public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
               if (newValue != null) {
                   if (selectedLabel != null) {
                       selectedLabel.setTextFill(newValue);
                       lastSelection = selectedLabel;
                       lastAction = ACTIONS[0];
                       lastColor = oldValue;
                       canRedo = false;
                       undo.setText("Undo");
                       selectedLabel.last = newValue;
                   } else {
                       for (EditableLabel label : selectionList) {
                           label.setTextFill(newValue);
                           label.last = newValue;
                           lastSelection = label;
                           lastAction = ACTIONS[0];
                           lastColor = oldValue;
                           canRedo = false;
                           undo.setText("Undo");
                       }
                   }
               }
           }
       });
       fontSize.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               if (newValue!=null){
                   if (selectedLabel!=null){
                       try {
                           Font font = selectedLabel.getFont();
                           Font newFont = new Font(font.getName(), Integer.parseInt(newValue));
                           selectedLabel.setFont(newFont);
                           lastSelection = selectedLabel;
                           lastAction = ACTIONS[2];
                           lastSize = font.getSize();
                           canRedo = false;
                           undo.setText("Undo");
                       }catch (NumberFormatException nfe){
                       }
                   }else{
                       for (EditableLabel label:selectionList){
                           try {
                               Font font = label.getFont();
                               Font newFont = new Font(font.getName(), Integer.parseInt(newValue));
                               label.setFont(newFont);
                               lastSelection = label;
                               lastAction = ACTIONS[2];
                               lastSize = font.getSize();
                               canRedo = false;
                               undo.setText("Undo");
                           }catch (NumberFormatException nfe){
                           }
                       }
                   }
               }
           }
       });
        fontdropdown.valueProperty().addListener((observable) -> {
            if (selectedLabel!=null) {
                double size = selectedLabel.getFont().getSize();
                lastSelection = selectedLabel;
                lastAction = ACTIONS[1];
                lastFont = selectedLabel.getFont();
                selectedLabel.setFont(new Font(fontdropdown.getValue(), size));
                canRedo = false;
                undo.setText("Undo");
            }
            else{
                for (EditableLabel label:selectionList){
                    double size = label.getFont().getSize();
                    lastSelection = label;
                    lastAction = ACTIONS[1];
                    lastFont = label.getFont();
                    label.setFont(new Font(fontdropdown.getValue(), size));
                    canRedo = false;
                    undo.setText("Undo");
                }
            }
        });
       
           writeToContainer.setOnMouseClicked((evt) -> {
            
            if(evt.getButton() == MouseButton.SECONDARY){
//
//
//                EditableLabel labeledText = null;
//            if(evt.getPickResult().getIntersectedNode() instanceof EditableLabel){
//
//                utils.unBindAll(list);
//                selectedLabel = (EditableLabel) evt.getPickResult().getIntersectedNode();
//                System.out.println(selectedLabel.getText());
//
//            }
//            else{
                if (selectedLabel!=null) {
                    selectedLabel.setStyle("-fx-border: 0");
                    selectedLabel = null;
                }
                for (EditableLabel s:selectionList) {
                    s.setStyle("-fx-border: 0");
                }
                selectionList.removeAll(selectionList);
//            }
        }
        });
           
//           if(selectedLabel != null){
//               if (!list.isEmpty()) {
//                   utils.unBindAll(list);
//               }
               //utils.bindTo(selectedLabel);
//           }
           

    }    

    @FXML
    private void addTextField(ActionEvent event) {
        if (guessMod){
            Bounds first = firstCircle.getBoundsInParent();
            Bounds sec = secondCircle.getBoundsInParent();
            Bounds leftBounds = left.getBounds().getBoundsInParent();
            Bounds middleBounds = middle.getBounds().getBoundsInParent();
            Bounds rightBounds = right.getBounds().getBoundsInParent();
            boolean leftCheck = first.intersects(leftBounds);
            boolean rightCheck = sec.intersects(rightBounds);
            boolean middleCheck = false;

            boolean middleFirst = first.intersects(middleBounds);
            boolean middleSec = sec.intersects(middleBounds);

            if (middleFirst & middleSec) middleCheck = true;
            if (middleCheck & leftCheck & rightCheck){
                JFXDialogLayout content = new JFXDialogLayout();
                //setting the heading for the dialog box
                content.setHeading(new Text("Results"));
                content.setBody(new Text("Your answer is correct."));
                //instantiating the dialog
                JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP, false);
                JFXButton ok = new JFXButton("OK");
                ok.setOnAction((evt) -> {
                    dialog.close();
                });
                //adding the button to dialog
                content.setActions(ok);
                dialog.show();
            }else {
                JFXDialogLayout content = new JFXDialogLayout();
                //setting the heading for the dialog box
                content.setHeading(new Text("Results"));
                content.setBody(new Text("Your answer is incorrect. Try Again."));
                //instantiating the dialog
                JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP, false);
                JFXButton ok = new JFXButton("OK");
                ok.setOnAction((evt) -> {
                    dialog.close();
                });
                //adding the button to dialog
                content.setActions(ok);
                dialog.show();
            }
            return;
        }

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Text");
        dialog.setHeaderText("Add Title And description.");
        // Set the button types.
        ButtonType create = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(create, ButtonType.CANCEL);
        // Create the textInput and descriptionsArea labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField textInput = new TextField();
        textInput.setPromptText("Title");
        TextArea descriptionsArea = new TextArea();
        descriptionsArea.setPromptText("Description");

        grid.add(new Label("Title: "), 0, 0);
        grid.add(textInput, 1, 0);
        grid.add(new Label("Description: "), 0, 1);
        grid.add(descriptionsArea, 1, 1);
        // Enable/Disable login button depending on whether a textInput was entered.
        Node createButton = dialog.getDialogPane().lookupButton(create);
        createButton.setDisable(true);
        // Do some validation (using the Java 8 lambda syntax).
        textInput.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        // Request focus on the textInput field by default.
        Platform.runLater(() -> textInput.requestFocus());
        // Convert the result to a textInput-descriptionsArea-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == create) {
                return new Pair<>(textInput.getText(), descriptionsArea.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(textDesc -> {
            createLabel(textDesc);
        });
        
    }

    private void createLabel(Pair<String, String> textDesc) {
        editableLabel = new EditableLabel(textDesc.getKey(), textDesc.getValue());

        //setting a new position for the editableLabel translate was here
        editableLabel.setLayoutY(yvalueIncrement);
        editableLabel.setLayoutX(xValueMargin);
        yvalueIncrement += 30;
        //create a list of editable label to keep track and count
        list.put(labelCount + 1, editableLabel);

        observableList.add(editableLabel);


        labelCount += 1;
        writeToContainer.getChildren().add(editableLabel);
        editableLabel.setFont(Font.font(fontdropdown.getValue(), Integer.parseInt(fontSize.getText())));
        //editableLabel.textFillProperty().bind(fontColor.valueProperty());
        editableLabel.setTextFill(fontColor.getValue());
        editableLabel.last = fontColor.getValue();
        //editableLabel.setFont(Font.font(fontdropdown.getValue(), Integer.parseInt(fontSize.getText())));

//        fontSize.textProperty().addListener((observable) -> {
//            if (!fontSize.getText().isEmpty()) {
//                int value = Integer.parseInt(fontSize.getText());
//                editableLabel.setFont(Font.font(value));
//                DecimalFormat format = new DecimalFormat("#.0");
//                fontSize.setTextFormatter(new TextFormatter<>(c ->
//                {
//                    if (c.getControlNewText().isEmpty()) {
//                        return c;
//                    }
//
//                    ParsePosition parsePosition = new ParsePosition(0);
//                    Object object = format.parse(c.getControlNewText(), parsePosition);
//
//                    if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
//                        return null;
//                    } else {
//                        return c;
//                    }
//                }));
//            }
//         });


        editableLabel.makeDragAndDrop();
    }
        
}