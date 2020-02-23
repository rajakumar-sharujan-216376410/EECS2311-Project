package venn.diagram;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jfoenix.controls.JFXColorPicker;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



class VennDiagramTester {

	private mainFXMLController controller;
	private EditableLabel text;
	
	@BeforeEach
    public void setUp() throws Exception {
        controller = new mainFXMLController();
        //text = new EditableLabel("enter text here");
		 
	}
	@Test
	public void getColorTest() {
		Circle c = new Circle();
		c.setFill(Color.RED);
		assertEquals(Color.RED,controller.getColorFirstCirle(c));
 
	}
	
	

}
