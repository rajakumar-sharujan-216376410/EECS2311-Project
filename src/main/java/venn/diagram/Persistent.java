
package venn.diagram;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public class Persistent {
    
    public Persistent(){
        
    }
    
    public void saveConfig(Properties prop, String path, String projectName){
        try {
            OutputStream out = new FileOutputStream("config.properties");
            prop.setProperty("project", path);
            prop.setProperty("project", projectName);
            prop.store(out, "this is a config file for initial run and project editing\n DO NOT EDIT\n");
        } catch (IOException e) {
            
            venndiagramLogger("errored occured "+ e.getMessage());
        }
    }
    
    public  void createProject(){
        
        mainFXMLController.getInstance().getWriteToContainer().getChildren().removeIf(node -> node instanceof EditableLabel);
        
        //creating a file chooser to specify the json file to save project
        FileChooser fc = new FileChooser();
        
        //filter desired extension for the project type to json
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("json", "*.json");
        
        //add extension to the file chooser
        fc.getExtensionFilters().add(extension);
        
        //setting the intial paramters for the project
        fc.setInitialFileName("untitled.json");
       
        File newFile = fc.showSaveDialog(null);
        
        saveConfig(new Properties(), newFile.getAbsolutePath(), newFile.getName());
        updateProjectConfigInPropFile(newFile.toString());
        
        try {
            //instantiating a file writer
            FileWriter writer = new FileWriter(newFile);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", "pair");
            
            JSONArray array = new JSONArray();
            array.add(jsonObject);
            writer.write(array.toJSONString());
            
        } catch (IOException ex) {
            Logger.getLogger(Persistent.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
        
        
        
        
    }
    
    public void saveForEdit( Map<Integer, EditableLabel> list){
             FileChooser fileChooser = new FileChooser();
       
       //setting configuration for filechooser
       fileChooser.setTitle("Save");
       fileChooser.setInitialFileName("untitled.json");
       
       //setting the extension filter of the file
       FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("save project", "*.json");
       fileChooser.getExtensionFilters().add(extension);
        fileChooser.setTitle("save project");
        File file = fileChooser.showSaveDialog(null);
        if(file!=null){
            JSONObject labelObject = new JSONObject();
            JSONArray array = new JSONArray();
            //creating a set of keys for iteration 
            Set < Integer > keys = list.keySet();
            LabelProperty labelProperty = new LabelProperty();
                 for (Iterator<Integer> it = keys.iterator(); it.hasNext();) {
                     Integer key = it.next();
                     System.out.println("adding elements to json file");
                     labelProperty.setId(key);
                     labelProperty.setFont(list.get(key));
                     labelProperty.setText(list.get(key));
                     labelProperty.setxCordinate(list.get(key));
                     labelProperty.setyCordinate(list.get(key));
                     JSONArray properties = new JSONArray();
                     properties.add(labelProperty.getId());
                     properties.add(labelProperty.getText());
                     properties.add(labelProperty.getFont());
                     properties.add(labelProperty.getxCordinate());
                     properties.add(labelProperty.getyCordinate());
                     labelObject.put("label "+ key, properties);
                     array.add(labelObject);
                 }
            try {
                FileWriter writer = new FileWriter(file);
                
                writer.write(array.toJSONString());
                writer.flush();
            } catch (IOException e) {
                System.err.println("an error occured "+ e.getMessage());
            }
        }
    }

    private void venndiagramLogger(String value) {
        Logger.getLogger(value);
    }
    
    public void updateProjectConfigInPropFile(String name){
        try {
            Properties prop;
            try (FileInputStream in = new FileInputStream("config.properties")) {
                prop = new Properties();
                prop.load(in);
                in.close();
            }
            
            try ( //opening the output stream
                    FileOutputStream out = new FileOutputStream("config.properties")) {
                prop.setProperty("project", name);
                prop.store(out, "modification date: "+new Date().toString());
                out.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Persistent.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Persistent.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
    }

}





class Values{
    @Expose
    private String text;
    private Font font;
    private String style;
    
    public void setText( EditableLabel label){
        text = label.getText();
    }
    public String getText(){
        return text;
    }
    
    public void setFont(EditableLabel label){
        font = label.getFont();
    }
    
    public Font getFont(){
        return font;
    }
    
    public void setStyle(EditableLabel label){
        style =  label.getStyle();
    }
    public String getStyle(){
        return style;
    }
    
}
















