package venn.diagram;

import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class VennDiagram extends Application {
    
    //initiate the persistent class
    Persistent persisten = new Persistent();
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainFXML.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.getIcons().add(new Image(VennDiagram.class.getResourceAsStream("icon.png")));
        stage.setTitle("VennGram");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
