
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

    private static final String TITLE = "VENN Diagram";
    private static final String MODE_TITLE = "Game Mode";

    public MenuItem undo;
    public Label title;
    public Button removeAll;
    private Label label;
    public MenuItem newProjectV;
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
    private Guess guess;

    private EditableLabel left, middle, right;

    private static final String[] ACTIONS = {"COLOR", "FONT", "SIZE","POS","CREATE","DELETE"};
    private String lastAction;
    private EditableLabel lastSelection;
    private Font lastFont;
    private Paint lastColor;
    private double lastSize;
    private boolean canRedo = false;
    private double lastPosX;
    private double lastPosY;
    private int scale = 225000;

    private static mainFXMLController instance;
    @FXML
    private HBox rootPane;
    @FXML
    private MenuItem donwloadAsPdf;
    @FXML
    private StackPane stackPane;
    private boolean guessMod;
    private double initX, initY;
    public boolean rightClickOnLabel = false;

    public mainFXMLController() {
        instance = this;
    }

    public static mainFXMLController getInstance() {
        return instance;
    }

    public Pane getWriteToContainer() {
        return writeToContainer;
    }

    public JFXTextField getFontSize() {
        return fontSize;
    }

    public JFXColorPicker getFontColor() {
        return fontColor;
    }

    public JFXComboBox<String> getFontDropdown() {
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
    private double locationXFactor = 0.8;

    //holds the increment for the x cordinate
    private double yvalueIncrement = 0.0;
    private double xValueMargin = 5;
    private UtilityActions utils = new UtilityActions();
    public ObservableList<EditableLabel> observableList = FXCollections.observableArrayList();
    public ObservableList<EditableLabel> selectionList = FXCollections.observableArrayList();
    private File saveLocation;
    public boolean multiSelection = false;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //setting the fonts specs in the font family selector combobox
        utils.setFonts(fontdropdown);
        initX = firstCircle.getLayoutX();
        initY = secondCircle.getLayoutX();
        writeToContainer.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null & !guessMod) {
                    firstCircle.setLayoutX(newValue.doubleValue() / 3);
                    secondCircle.setLayoutX((newValue.doubleValue() / 3) - 50);
                }
            }
        });
        writeToContainer.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null & !guessMod) {
                    firstCircle.setLayoutY((newValue.doubleValue() / 2));
                    secondCircle.setLayoutY((newValue.doubleValue() / 2));
                }
            }
        });
        // min was 150 and max was 250
        circleRadiusSlider.setMin(250);
        circleRadiusSlider.setMax(400);
        firstCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
        secondCircle.radiusProperty().bind(circleRadiusSlider.valueProperty());
        secondCircle.translateXProperty().bind(circleRadiusSlider.valueProperty().subtract(10));

        //binding to the color properties of the circles
        leftContainerPicker.setValue(Color.DARKORANGE);
        rightContainerPicker.setValue(Color.LAWNGREEN);
        firstCircle.fillProperty().bind(leftContainerPicker.valueProperty());
        secondCircle.fillProperty().bind(rightContainerPicker.valueProperty());

        circleRadiusSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null) {
                    double inc = (newValue.doubleValue() - oldValue.doubleValue());
                    inc = Math.abs(inc);
                    for (EditableLabel label : observableList) {
                        Bounds first = firstCircle.getBoundsInParent();
                        Bounds sec = secondCircle.getBoundsInParent();
                        Bounds labelBounds = label.getBounds().getBoundsInParent();
                        boolean firstCheck = first.intersects(labelBounds);
                        boolean secCheck = sec.intersects(labelBounds);

                        if (firstCheck) {
                            Font font = label.getFont();
                            Font newFont;
                            if (newValue.doubleValue() > oldValue.doubleValue()) {
                                newFont = new Font(font.getName(), font.getSize() + scaleFactor);
                                label.setLayoutX(label.getLayoutX() - locationXFactor * inc);
                            } else {
                                newFont = new Font(font.getName(), font.getSize() - scaleFactor);
                                label.setLayoutX(label.getLayoutX() + locationXFactor * inc);
                            }
                            label.setFont(newFont);
                            System.out.println("first was moved");
                        } else if (secCheck) {
                            Font font = label.getFont();
                            Font newFont;
                            if (newValue.doubleValue() > oldValue.doubleValue()) {
                                newFont = new Font(font.getName(), font.getSize() + scaleFactor);
                                label.setLayoutX(label.getLayoutX() + locationXFactor * inc);
                            } else {
                                newFont = new Font(font.getName(), font.getSize() - scaleFactor);
                                label.setLayoutX(label.getLayoutX() - locationXFactor * inc);
                            }
                            label.setFont(newFont);
                            System.out.println("Sec was moved");
                        }
                    }
                }
            }
        });
        title.setOnMouseClicked(e->{
            if (e.getButton() == MouseButton.SECONDARY){
                if (!guessMod){
                    final ContextMenu menu = new ContextMenu();
                    MenuItem edit = new MenuItem("Edit");
                    menu.getItems().add(edit);
                    edit.setOnAction(x->{
                        JFXDialogLayout layout = new JFXDialogLayout();
                        JFXButton change = new JFXButton("Change");
                        JFXTextField newTitle = new JFXTextField(title.getText());
                        layout.setHeading(new Label("Enter New Title"));
                        layout.setBody(newTitle);
                        layout.setActions(change);
                        JFXDialog changeTitle = new JFXDialog(stackPane,layout,JFXDialog.DialogTransition.TOP);
                        changeTitle.show();
                        change.setOnAction(y->{
                            if (newTitle.getText().isEmpty()) return;
                            title.setText(newTitle.getText());
                            changeTitle.close();
                        });
                    });
                    title.setContextMenu(menu);
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
                if (newValue != null) {
                    if (selectedLabel != null) {
                        try {
                            Font font = selectedLabel.getFont();
                            Font newFont = new Font(font.getName(), Integer.parseInt(newValue));
                            selectedLabel.setFont(newFont);
                            lastSelection = selectedLabel;
                            lastAction = ACTIONS[2];
                            lastSize = font.getSize();
                            canRedo = false;
                            undo.setText("Undo");
                        } catch (NumberFormatException nfe) {
                        }
                    } else {
                        for (EditableLabel label : selectionList) {
                            try {
                                Font font = label.getFont();
                                Font newFont = new Font(font.getName(), Integer.parseInt(newValue));
                                label.setFont(newFont);
                                lastSelection = label;
                                lastAction = ACTIONS[2];
                                lastSize = font.getSize();
                                canRedo = false;
                                undo.setText("Undo");
                            } catch (NumberFormatException nfe) {
                            }
                        }
                    }
                }
            }
        });
        fontdropdown.valueProperty().addListener((observable) -> {
            if (selectedLabel != null) {
                double size = selectedLabel.getFont().getSize();
                lastSelection = selectedLabel;
                lastAction = ACTIONS[1];
                lastFont = selectedLabel.getFont();
                selectedLabel.setFont(new Font(fontdropdown.getValue(), size));
                canRedo = false;
                undo.setText("Undo");
            } else {
                for (EditableLabel label : selectionList) {
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

            if (evt.getButton() == MouseButton.SECONDARY) {
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
                if (!rightClickOnLabel) {
                    if (!multiSelection & selectedLabel!=null) {
                        selectedLabel.setStyle("-fx-border: 0");
                        selectedLabel = null;
                    }
                    if (multiSelection) {
                        for (EditableLabel s : selectionList) {
                            s.setStyle("-fx-border: 0");
                        }
                        selectionList.removeAll(selectionList);
                        multiSelection = false;
                    }
                }
//            }
            }else rightClickOnLabel = false;
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
        if (guessMod) {
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
            if (middleCheck & leftCheck & rightCheck) {
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
            } else {
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
        lastSelection = editableLabel;
        lastAction = ACTIONS[4];
    }

    public JFXTextField getFontSizeField() {
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
        if (file != null) {
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
            ImageIO.write(SwingFXUtils.fromFXImage(scale(image,scale/(int)circleRadiusSlider.getValue(),scale/(int)circleRadiusSlider.getValue() ,true), null), "png", imgFile);
        } catch (IOException e) {
            System.err.println("An Error occured " + e.getMessage());
        }

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        PDImageXObject pdimage;
        PDPageContentStream content;

        try {
            pdimage = PDImageXObject.createFromFile("img.png", doc);
            content = new PDPageContentStream(doc, page);
            content.drawImage(pdimage, 0  , 200);
            content.close();
            doc.addPage(page);
            doc.save(file);
            doc.close();
            imgFile.delete();
        } catch (IOException e) {
            Logger.getLogger(mainFXMLController.class.getName()).log(Level.SEVERE, null, e);
        }


    }
    private Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }

    private static void configureFileChooser(final FileChooser fileChooser, String title) {
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    @FXML
    private void saveForEdit(ActionEvent event) {

//        Persistent persist = new Persistent();
//        persist.saveForEdit(list);
        save();
    }

    @FXML
    private void newProject(ActionEvent event) {
        JFXDialogLayout content = new JFXDialogLayout();
        //setting the heading for the dialog box
        content.setHeading(new Text("Confirm Action"));
        content.setBody(new Text("Are you sure you want to Close this and create new project?"));

        //instantiating the dialog
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.BOTTOM, false);
        JFXButton confirmBtn = new JFXButton("Yes");
        confirmBtn.setOnAction((evt) -> {
            dialog.close();
            writeToContainer.getChildren().removeIf(node -> node instanceof EditableLabel);
            observableList.removeAll(observableList);
            circleRadiusSlider.setValue(250);
            leftContainerPicker.setValue(Color.DARKORANGE);
            rightContainerPicker.setValue(Color.LAWNGREEN);
            fontColor.setDisable(false);
            fontSize.setDisable(false);
            fontColor.setValue(Color.BLACK);
            fontSize.setText("12");
            addButton.setText("Add Text");
            if (!guessMod) {
                save();
            }
            if (guessMod) {
                resetModeConfig();
            }

        });
        JFXButton cancelBtn = new JFXButton("No");
        cancelBtn.setOnAction((evt) -> {
            dialog.close();
        });

        //adding the button to dialog
        content.setActions(confirmBtn, cancelBtn);
        dialog.show();
//        Persistent persist = new Persistent();
//        persist.createProject();
    }

    @FXML
    private void clearAllLabel(ActionEvent event) {
        JFXDialogLayout content = new JFXDialogLayout();

        //setting the heading for the dialog box
        content.setHeading(new Text("Confirm Action"));
        content.setBody(new Text("Are you sure you want to clear all text?"));

        //instantiating the dialog 
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP, false);
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

        FileChooser fileChooser = new FileChooser();

        //setting configuration for filechooser
        fileChooser.setTitle("Load");
        fileChooser.setInitialFileName("untitled.venn");

        //setting the extension filter of the file
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("Load Project", "*.venn");
        fileChooser.getExtensionFilters().add(extension);
        fileChooser.setTitle("Load project");
        File file = fileChooser.showOpenDialog(VennDiagram.stage);
        if (file != null) {
            writeToContainer.getChildren().removeIf(node -> node instanceof EditableLabel);
            observableList.removeAll(observableList);
            selectionList.removeAll(selectionList);
            if (guessMod) {
                resetModeConfig();
            }
            try {
                FileInputStream fos = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fos);
                List<Properties> properties = (List<Properties>) ois.readObject();
                if (properties != null) {
                    for (Properties p : properties) {
                        editableLabel = new EditableLabel(p.getText(), p.getDescription());
                        //setting a new position for the editableLabel translate was here
                        editableLabel.setLayoutY(p.getY());
                        editableLabel.setLayoutX(p.getX());
                        //create a list of editable label to keep track and count
                        editableLabel.setFont(Font.font(p.getFontName(), p.getFontSize()));
                        editableLabel.setTextFill(Color.web(p.getFontColor()));
                        leftContainerPicker.setValue(Color.web(p.getLeftColor()));
                        rightContainerPicker.setValue(Color.web(p.getRightColor()));
                        circleRadiusSlider.setValue(p.getSliderValue());
                        editableLabel.last = Color.web(p.getFontColor());
                        fontdropdown.valueProperty().addListener((observable) -> {
                            editableLabel.setFont(new Font(fontdropdown.getValue(), Integer.parseInt(fontSize.getText())));
                        });
                        fontSize.textProperty().addListener((observable) -> {
                            int value = Integer.parseInt(fontSize.getText());
                            editableLabel.setFont(Font.font(value));
                            DecimalFormat format = new DecimalFormat("#.0");
                            fontSize.setTextFormatter(new TextFormatter<>(c ->
                            {
                                if (c.getControlNewText().isEmpty()) {
                                    return c;
                                }

                                ParsePosition parsePosition = new ParsePosition(0);
                                Object object = format.parse(c.getControlNewText(), parsePosition);

                                if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                                    return null;
                                } else {
                                    return c;
                                }
                            }));
                        });

                        observableList.add(editableLabel);
                        writeToContainer.getChildren().add(editableLabel);
                        editableLabel.makeDragAndDrop();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetModeConfig() {
        newProjectV.setText("New Project");
        guessMod = false;
        firstCircle.setLayoutX(initX);
        secondCircle.setLayoutX(initY);
        addButton.setText("Add Text");
        addButton.setMinWidth(67);
        deleteBtn.setText("Remove");
        deleteBtn.setMinWidth(65);
        removeAll.setVisible(true);
        fontdropdown.setDisable(false);
        circleRadiusSlider.setDisable(false);
        leftContainerPicker.setDisable(false);
        rightContainerPicker.setDisable(false);
        title.setText(TITLE);
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }

    private void save() {
        if (!observableList.isEmpty()) {
            List<Properties> properties = new ArrayList<>();
            for (EditableLabel label : observableList) {
                Color c = (Color) label.getTextFill();
                String hex = String.format("#%02X%02X%02X",
                        (int) (c.getRed() * 255),
                        (int) (c.getGreen() * 255),
                        (int) (c.getBlue() * 255));
                String left = String.format("#%02X%02X%02X",
                        (int) (((Color)firstCircle.getFill()).getRed() * 255),
                        (int) (((Color)firstCircle.getFill()).getGreen() * 255),
                        (int) (((Color)firstCircle.getFill()).getBlue() * 255));
                String right = String.format("#%02X%02X%02X",
                        (int) (((Color)secondCircle.getFill()).getRed() * 255),
                        (int) (((Color)secondCircle.getFill()).getGreen() * 255),
                        (int) (((Color)secondCircle.getFill()).getBlue() * 255));
                Properties properties1 = new Properties(
                        label.getText(),
                        label.getLayoutX(),
                        label.getLayoutY(),
                        hex,
                        label.getFont().getName(),
                        label.getFont().getSize()
                );
                properties1.setDescription(label.desc);
                properties1.setLeftColor(left);
                properties1.setRightColor(right);
                properties1.setSliderValue(circleRadiusSlider.getValue());
                properties.add(properties1);
            }
            if (saveLocation == null) {
                FileChooser fileChooser = new FileChooser();

                //setting configuration for filechooser
                fileChooser.setTitle("Save");
                fileChooser.setInitialFileName("untitled.venn");

                //setting the extension filter of the file
                FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("save project", "*.venn");
                fileChooser.getExtensionFilters().add(extension);
                fileChooser.setTitle("save project");
                saveLocation = fileChooser.showSaveDialog(VennDiagram.stage);
            }
            if (saveLocation != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(saveLocation);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(properties);
                    os.close();
                    fos.close();
                    System.out.println("Success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void undo(ActionEvent actionEvent) {
        if (lastAction.equals(ACTIONS[0])) {
            if (lastSelection != null) {
                Paint temp = lastSelection.getTextFill();
                lastSelection.setTextFill(lastColor);
                lastColor = temp;
                if (!canRedo) {
                    canRedo = true;
                    undo.setDisable(true);
                }
            }
        }
        if (lastAction.equals(ACTIONS[1])) {
            if (lastSelection != null) {
                Font temp = lastSelection.getFont();
                lastSelection.setFont(lastFont);
                lastFont = temp;
                if (!canRedo) {
                    canRedo = true;
                    undo.setDisable(true);
                }
            }
        }
        if (lastAction.equals(ACTIONS[2])) {
            if (lastSelection != null) {
                Font temp = lastSelection.getFont();
                lastSelection.setFont(new Font(temp.getName(), lastSize));
                lastSize = temp.getSize();
                if (!canRedo) {
                    canRedo = true;
                    undo.setDisable(true);
                }
            }
        }
        if (lastAction.equals(ACTIONS[3])) {
            if (lastSelection != null) {
                double temp = lastSelection.getLayoutX();
                lastSelection.setLayoutX(lastPosX);
                lastPosX = temp;
                temp = lastSelection.getLayoutY();
                lastSelection.setLayoutY(lastPosY);
                lastPosY = temp;
                if (!canRedo) {
                    canRedo = true;
                    undo.setDisable(true);
                }
            }
        }
        if (lastAction.equals(ACTIONS[4])) {
            if (lastSelection != null) {
                writeToContainer.getChildren().remove(lastSelection);
                observableList.remove(lastSelection);
                if (!canRedo) {
                    canRedo = true;
                    undo.setDisable(true);
                }
            }
        }
        if (lastAction.equals(ACTIONS[5])) {
            if (lastSelection != null) {
                observableList.add(lastSelection);
                writeToContainer.getChildren().add(lastSelection);
                if (!canRedo) {
                    canRedo = true;
                    undo.setDisable(true);
                }
            }
        }
    }

    private void createGuess() {
        guess = new Guess(50, "Gas Powered", "Hybrid", "Electric");
        Pair<String, String> leftP = new Pair<>(guess.getLeft(), "");
        createLabel(leftP);
        left = editableLabel;
        Pair<String, String> middleP = new Pair<>(guess.getMiddle(), "");
        createLabel(middleP);
        middle = editableLabel;
        Pair<String, String> rightP = new Pair<>(guess.getRight(), "");
        createLabel(rightP);
        right = editableLabel;
        double radius = firstCircle.getRadius();
        double diameter = radius * 2;
        double diff = ((guess.overLapPercent / 100) * diameter);
        double offset = 300;
        firstCircle.setLayoutX(initX - offset);
        if (diff == 0) {
            secondCircle.setLayoutX((initX - radius - 200) + (diameter - diff));
        } else secondCircle.setLayoutX((initX - radius - offset) + (diameter - diff));

        JFXDialogLayout layout = new JFXDialogLayout();
        JFXButton change = new JFXButton("Close");
        Label title = new Label("Your Task.");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        layout.setHeading(title);
        layout.setBody(new Label("The Venn diagram is setup to represent Vehicle Engine type. You have to guess which is which.\nGood luck."));
        layout.setActions(change);
        JFXDialog changeTitle = new JFXDialog(stackPane,layout,JFXDialog.DialogTransition.TOP);
        changeTitle.show();
        change.setOnAction(y->{
            changeTitle.close();
        });
    }

    public void newGuessMod(ActionEvent actionEvent) {
        JFXDialogLayout content = new JFXDialogLayout();
        //setting the heading for the dialog box
        content.setHeading(new Text("Confirm Action"));
        content.setBody(new Text("Are you sure you want to Close this and create new Mode?"));

        //instantiating the dialog
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.BOTTOM, false);
        JFXButton confirmBtn = new JFXButton("Yes");
        confirmBtn.setOnAction((evt) -> {
            dialog.close();
            save();
            writeToContainer.getChildren().removeIf(node -> node instanceof EditableLabel);
            observableList.removeAll(observableList);
            selectionList.removeAll(selectionList);
            circleRadiusSlider.setValue(250);
            leftContainerPicker.setValue(Color.DARKORANGE);
            rightContainerPicker.setValue(Color.LAWNGREEN);
            fontColor.setDisable(true);
            fontSize.setText("20");
            fontSize.setDisable(true);
            addButton.setText("Check My Work");
            addButton.setMinWidth(120);
            deleteBtn.setText("Check Answers");
            deleteBtn.setMinWidth(100);
            removeAll.setVisible(false);
            title.setText(MODE_TITLE);
            newProjectV.setText("Exit Guess Mode");
            fontdropdown.setDisable(true);
            circleRadiusSlider.setDisable(true);
            leftContainerPicker.setDisable(true);
            rightContainerPicker.setDisable(true);
            guessMod = true;
            createGuess();
        });
        JFXButton cancelBtn = new JFXButton("No");
        cancelBtn.setOnAction((evt) -> {
            dialog.close();
        });

        //adding the button to dialog
        content.setActions(confirmBtn, cancelBtn);
        dialog.show();
    }

    public void remove(ActionEvent actionEvent) {
        if (guessMod) {
            JFXDialogLayout content = new JFXDialogLayout();
            //setting the heading for the dialog box
            content.setHeading(new Text("Confirm Action"));
            content.setBody(new Text("Are you sure you want to check right answers?"));

            //instantiating the dialog
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.BOTTOM, false);
            JFXButton confirmBtn = new JFXButton("Yes");
            confirmBtn.setOnAction((evt) -> {
                dialog.close();
                JFXDialogLayout content2 = new JFXDialogLayout();
                //setting the heading for the dialog box
                content2.setHeading(new Text("Results"));
                content2.setBody(new Text("Correct answer is :\n Left container: " + guess.left + ", Middle container" +
                        ": " + guess.middle + " and right container: " + guess.right));
                //instantiating the dialog
                JFXDialog dialog2 = new JFXDialog(stackPane, content2, JFXDialog.DialogTransition.TOP, false);
                JFXButton ok = new JFXButton("OK");
                ok.setOnAction((evt1) -> {
                    dialog2.close();
                });
                //adding the button to dialog
                content2.setActions(ok);
                dialog2.show();
            });
            JFXButton cancelBtn = new JFXButton("No");
            cancelBtn.setOnAction((evt) -> {
                dialog.close();
            });

            //adding the button to dialog
            content.setActions(confirmBtn, cancelBtn);
            dialog.show();
        } else {
            if (selectedLabel != null) {
                lastSelection = selectedLabel;
                lastAction = ACTIONS[5];
                observableList.remove(selectedLabel);
                writeToContainer.getChildren().remove(selectedLabel);
                selectedLabel = null;
            } else {
                observableList.removeAll(selectionList);
                writeToContainer.getChildren().removeAll(selectionList);
                selectionList.removeAll(selectionList);
            }
        }
    }

    public boolean isGuessMod() {
        return guessMod;
    }

    public String[] getActions() {
        return ACTIONS;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public void setLastPosX(double lastPosX) {
        this.lastPosX = lastPosX;
    }

    public void setLastPosY(double lastPosY) {
        this.lastPosY = lastPosY;
    }

    public void setLastSelection(EditableLabel newLabel){
        lastSelection = newLabel;
    }

    public void redo(ActionEvent actionEvent) {
        if (lastAction.equals(ACTIONS[0])) {
            if (lastSelection != null) {
                Paint temp = lastSelection.getTextFill();
                lastSelection.setTextFill(lastColor);
                lastColor = temp;
                if (canRedo) {
                    canRedo = false;
                    undo.setDisable(false);
                }

            }
        }
        if (lastAction.equals(ACTIONS[1])) {
            if (lastSelection != null) {
                Font temp = lastSelection.getFont();
                lastSelection.setFont(lastFont);
                lastFont = temp;
                if (canRedo) {
                    canRedo = false;
                    undo.setDisable(false);
                }
            }
        }
        if (lastAction.equals(ACTIONS[2])) {
            if (lastSelection != null) {
                Font temp = lastSelection.getFont();
                lastSelection.setFont(new Font(temp.getName(), lastSize));
                lastSize = temp.getSize();
                if (canRedo) {
                    canRedo = false;
                    undo.setDisable(false);
                }
            }
        }
        if (lastAction.equals(ACTIONS[3])) {
            if (lastSelection != null) {
                double temp = lastSelection.getLayoutX();
                lastSelection.setLayoutX(lastPosX);
                lastPosX = temp;
                temp = lastSelection.getLayoutY();
                lastSelection.setLayoutY(lastPosY);
                lastPosY = temp;
                if (canRedo) {
                    canRedo = false;
                    undo.setDisable(false);
                }
            }
        }
        if (lastAction.equals(ACTIONS[4])) {
            if (lastSelection != null) {
                observableList.add(lastSelection);
                writeToContainer.getChildren().add(lastSelection);
                if (canRedo) {
                    canRedo = false;
                    undo.setDisable(false);
                }
            }
        }
        if (lastAction.equals(ACTIONS[5])) {
            if (lastSelection != null) {
                writeToContainer.getChildren().remove(lastSelection);
                observableList.remove(lastSelection);
                if (canRedo) {
                    canRedo = false;
                    undo.setDisable(false);
                }
            }
        }
    }

    public void about(ActionEvent actionEvent) {
        if (guessMod){
            JFXDialogLayout layout = new JFXDialogLayout();
            JFXButton change = new JFXButton("Close");
            Label title = new Label("Your Task.");
            title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            layout.setHeading(title);
            layout.setBody(new Label("The Venn diagram is setup to represent Vehicle Engine type. You have to guess which is which.\nGood luck."));
            layout.setActions(change);
            JFXDialog changeTitle = new JFXDialog(stackPane,layout,JFXDialog.DialogTransition.TOP);
            changeTitle.show();
            change.setOnAction(y->{
                changeTitle.close();
            });
        }else{
            JFXDialogLayout layout = new JFXDialogLayout();
            JFXButton change = new JFXButton("Close");
            Label about = new Label("About");
            about.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            layout.setHeading(about);
            layout.setBody(new Label("This is an application to create venn diagrams."));
            layout.setActions(change);
            JFXDialog changeTitle = new JFXDialog(stackPane,layout,JFXDialog.DialogTransition.TOP);
            changeTitle.show();
            change.setOnAction(y->{
                changeTitle.close();
            });
        }
    }

    private class Guess {
        private double overLapPercent;
        private String left;
        private String middle;
        private String right;

        public Guess(double overLapPercent, String left, String middle, String right) {
            this.overLapPercent = overLapPercent;
            this.left = left;
            this.middle = middle;
            this.right = right;
        }

        public double getOverLapPercent() {
            return overLapPercent;
        }

        public String getLeft() {
            return left;
        }

        public String getMiddle() {
            return middle;
        }

        public String getRight() {
            return right;
        }
    }
}






