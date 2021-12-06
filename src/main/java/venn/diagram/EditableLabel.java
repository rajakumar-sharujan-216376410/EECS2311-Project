
package venn.diagram;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.Pair;


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
    private double greenX = 0, greenY = 0;
    public Color last;


    public EditableLabel(String str, String desc) {
        super(str);
        this.desc = desc;
        bounds = new Rectangle();
        bounds.widthProperty().bind(widthProperty());
        bounds.heightProperty().bind(heightProperty());
        tooltip = new Tooltip(desc);
        tooltip.setStyle("-fx-base: #AE3522; "
                + "-fx-text-fill: orange;");
        setupCustomTooltipBehavior(0, Integer.MAX_VALUE, 20);
        this.setTooltip(tooltip);
        this.setOnMouseClicked((e) -> {
            if (!mainFXMLController.getInstance().isGuessMod()) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    mainFXMLController.getInstance().rightClickOnLabel = true;
                    final ContextMenu menu = new ContextMenu();
                    MenuItem edit = new MenuItem("Edit");
                    menu.getItems().add(edit);
                    edit.setOnAction(action -> {
                        // Create the custom dialog.
                        Dialog<Pair<String, String>> dialog = new Dialog<>();
                        dialog.setTitle("Edit Text");
                        dialog.setHeaderText("Edit Title And description.");
                        // Set the button types.
                        ButtonType create = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(create, ButtonType.CANCEL);
                        // Create the textInput and descriptionsArea labels and fields.
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        TextField textInput = new TextField();
                        textInput.setText(this.getText());
                        TextArea descriptionsArea = new TextArea();
                        descriptionsArea.setText(this.desc);

                        grid.add(new Label("Title: "), 0, 0);
                        grid.add(textInput, 1, 0);
                        grid.add(new Label("Description: "), 0, 1);
                        grid.add(descriptionsArea, 1, 1);

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
                            this.setText(textDesc.getKey());
                            this.desc = textDesc.getValue();
                            tooltip.setText(textDesc.getValue());
                        });
                        mainFXMLController.getInstance().rightClickOnLabel = false;
                    });
                    this.setContextMenu(menu);
                }
                if (e.getClickCount() == 2) {
                    this.setStyle("-fx-border: 0; -fx-border-color: black");
                    textField.setText(backup = this.getText());
                    this.setGraphic(textField);
                    this.setText("");
                    textField.requestFocus();
                    e.consume();
                } else if (e.getClickCount() == 1) {
                    if (e.isShortcutDown()) {
                        if (mainFXMLController.getInstance().selectedLabel != null) {
                            mainFXMLController.getInstance().selectionList.add(
                                    mainFXMLController.getInstance().selectedLabel
                            );
                        }
                        mainFXMLController.getInstance().selectedLabel = null;
                        mainFXMLController.getInstance().selectionList.add(this);
                        this.setStyle("-fx-border: 5; -fx-border-color: black");
                        mainFXMLController.getInstance().multiSelection = true;
                    } else {
                        if (mainFXMLController.getInstance().selectedLabel != null) {
                            mainFXMLController.getInstance().selectedLabel.setStyle("-fx-border: 0");
                        }
                        mainFXMLController.getInstance().selectedLabel = this;
                        this.setStyle("-fx-border: 5; -fx-border-color: black");
                    }
                }
            }
        });

        textField.focusedProperty().addListener((prop, o, n) -> {
            if (!n) {
                toLabel();
            }
        });

        textField.setOnKeyReleased((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                toLabel();
            } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                textField.setText(backup);
                toLabel();
            }
        });
    }

    public void toLabel() {
        this.setGraphic(null);
        this.setText(textField.getText());
    }

    public void editColor(JFXColorPicker cp) {
        this.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 1) {

                this.textFillProperty().bind(cp.valueProperty());
            } else
                this.textFillProperty().unbind();
        });
        this.setOnMouseExited((event) -> {
            this.textFillProperty().unbind();
        });
    }

    public void setFonts(JFXComboBox<String> fontCombo) {
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
        ObservableList<String> fontItems = FXCollections.observableArrayList(fontList);
        fontCombo.getItems().addAll(fontItems);
    }


    //deleting the label on button pressed
    public void delete(Button btn, AnchorPane ap) {
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

    public void deleteSelectedLabel(Button btn, AnchorPane ap) {
        btn.setOnAction((event) -> {
            ap.getChildren().remove(getSelectedLabel());
            System.out.println("selectable node " + getSelectedLabel().getText());
        });
    }

    private EditableLabel getSelectedLabel() {
        EditableLabel defaultLabel = new EditableLabel("", "");
//        if(selectedLabel == null)
//            return defaultLabel;
//        else
//        return selectedLabel;
        return defaultLabel;
    }

    public void makeDragAndDrop() {

        this.setOnMousePressed((event) -> {
            x = this.getLayoutX() - event.getSceneX();
            y = this.getLayoutY() - event.getSceneY();
            greenX = event.getSceneX() + x;
            greenY = event.getSceneY() + y;
            mainFXMLController.getInstance().setLastPosX(getLayoutX());
            mainFXMLController.getInstance().setLastPosY(getLayoutY());
            mainFXMLController.getInstance().setLastAction(
                    mainFXMLController.getInstance().getActions()[3]
            );
            mainFXMLController.getInstance().setLastSelection(this);
        });

        this.setOnMouseDragged((event) -> {
            dragging = true;
            this.setLayoutX(event.getSceneX() + x);
            this.setLayoutY(event.getSceneY() + y);
            bounds.setLayoutX(event.getSceneX() + x);
            bounds.setLayoutY(event.getSceneY() + y);
            Point point = new Point();
            point.setLocation(this.getLayoutX() + this.getWidth() / 2
                    , this.getLayoutY() + this.getHeight() / 2);
            Map<Double, EditableLabel> all = new HashMap<>();
            ObservableList<EditableLabel> textElements = mainFXMLController.getInstance().observableList;
            // prevent container overlap
            Pane c = mainFXMLController.getInstance().getWriteToContainer();
            if (textElements.size() == 1) {
                if (this.getLayoutX() <= c.getLayoutX()
                        | (this.getLayoutX() + this.getWidth() >= c.getLayoutX() + c.getWidth())
                        | (this.getLayoutY() <= c.getLayoutY())
                        | (this.getLayoutY() + this.getHeight() >= c.getLayoutY() + c.getHeight() - 60)) {
                    this.setTextFill(Color.RED);
                    red = true;
                } else {
                    this.setTextFill(Color.GREEN);
                    greenX = event.getSceneX() + x;
                    greenY = event.getSceneY() + y;
                    red = false;
                }
            }
            for (int i = 0; i < textElements.size(); i++) {
                if (textElements.get(i) != this) {
                    Point p = new Point();
                    p.setLocation(textElements.get(i).getLayoutX() + textElements.get(i).getWidth() / 2
                            , textElements.get(i).getLayoutY() + textElements.get(i).getHeight() / 2);
                    double d = point.distance(p);
                    all.put(d, textElements.get(i));
                }
            }
            if (textElements.size()>1) {
                double min = Collections.min(all.keySet());
                System.out.println(all.get(min).getText());
                EditableLabel e = all.get(min);

                // prevent text overlap
                if (
                        ((this.getLayoutX() >= e.getLayoutX() & this.getLayoutX() <= e.getLayoutX() + e.getWidth())
                                | (this.getLayoutX() < e.getLayoutX() & this.getLayoutX() + this.getWidth() > e.getLayoutX()))
                                &
                                ((this.getLayoutY() >= e.getLayoutY() & this.getLayoutY() <= e.getLayoutY() + e.getHeight())
                                        | (this.getLayoutY() < e.getLayoutY() & this.getLayoutY() + this.getHeight() > e.getLayoutY()))

                ) {
                    this.setTextFill(Color.RED);
                    red = true;
                } else if (this.getLayoutX() <= c.getLayoutX()
                        | (this.getLayoutX() + this.getWidth() >= c.getLayoutX() + c.getWidth())
                        | (this.getLayoutY() <= c.getLayoutY() - 50)
                        | (this.getLayoutY() + this.getHeight() >= c.getLayoutY() + c.getHeight() - 60)) {
                    this.setTextFill(Color.RED);
                    red = true;
                } else {
                    red = false;
                    this.setTextFill(Color.GREEN);
                    greenX = event.getSceneX() + x;
                    greenY = event.getSceneY() + y;
                }
            }

        });
        this.setOnMouseReleased(e -> {
            if (dragging) {
                dragging = false;
                if (red) {
                    if (greenX == this.getLayoutX())
                        System.out.println("NOPE");
                    this.setLayoutX(greenX);
                    this.setLayoutY(greenY);
                    bounds.setLayoutX(greenX);
                    bounds.setLayoutY(greenY);
                    this.setTextFill(last);
                    red = false;
                }
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setupCustomTooltipBehavior(int openDelayInMillis, int visibleDurationInMillis, int closeDelayInMillis) {
        try {
            Class TTBehaviourClass = null;
            Class[] declaredClasses = Tooltip.class.getDeclaredClasses();
            for (Class c : declaredClasses) {
                if (c.getCanonicalName().equals("javafx.scene.control.Tooltip.TooltipBehavior")) {
                    TTBehaviourClass = c;
                    break;
                }
            }
            Objects.requireNonNull(TTBehaviourClass);
            Constructor constructor = TTBehaviourClass.getDeclaredConstructor(Duration.class, Duration.class, Duration.class, boolean.class);
            assert (constructor != null);
            constructor.setAccessible(true);
            Object newTTBehaviour = constructor.newInstance(new Duration(openDelayInMillis), new Duration(visibleDurationInMillis), new Duration(closeDelayInMillis), false);
            assert (newTTBehaviour != null);
            Field ttbehaviourField = Tooltip.class.getDeclaredField("BEHAVIOR");
            assert (ttbehaviourField != null);
            ttbehaviourField.setAccessible(true);
            // Cache the default behavior if needed.
            // Object defaultTTBehavior = ttbehaviourField.get(Tooltip.class);
            ttbehaviourField.set(Tooltip.class, newTTBehaviour);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}

