package venn.diagram;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class VennDiagramTester {

	private mainFXMLController controller;
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
		assertEquals(Color.RED, controller.getColor(c));
	}
	 
	@Test
	public void getEmptyTextTest() {
		assertEquals(null, text);
	}
	
	@Test
	public void getTextTest() {
		controller.addText(1, text);
		assertEquals(text,controller.getText(1));
	}
	@Test
	public void getSizeTest() {
		c.setRadius(100);
		assertEquals(100, c.getRadius()); 
	}
}
