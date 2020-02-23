package venn.diagram;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jfoenix.controls.JFXColorPicker;
import com.sun.istack.internal.NotNull;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



class VennDiagramTester {

	private mainFXMLController controller;
	@NotNull
	private EditableLabel text;
	private Circle c;
	
	@BeforeEach
    public void setUp() throws Exception {
        controller = new mainFXMLController();
        c = new Circle();
        c.setFill(Color.RED);
        
		 
	}
	@Test
	public void getColorTest() {
		
		assertEquals(Color.RED,controller.getColor(c));
 
	}
	
	@Test
	public void setColorTest() {
		controller.setColor(c, Color.BLUE);
		assertEquals(Color.BLUE,controller.getColor(c));
		
		
	}
	
	@Test
	public void addTextTest() {
		controller.addText(1, text);
		assertEquals(text,controller.getText(1));
	}
	

}
