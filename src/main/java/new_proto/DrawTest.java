package new_proto;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DrawTest extends JPanel {
	
	//private int xPos, yPos, distance;
	private String user, user2, user3;
	
	public void drawing() {
		//Draws the two circles
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Sets the settings for the Venn Diagram
		
		g.setColor(new Color(0, 150, 255, 150));
		g.fillOval(110, 100, 220, 220);
		g.setColor(new Color(255, 0, 0, 150));
		g.fillOval(5, 100, 220, 220);	
	
	}
}
