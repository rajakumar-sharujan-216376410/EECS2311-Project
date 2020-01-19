package Prototype;

import javax.swing.*;
import java.awt.*;

public class Draw extends JPanel{
	
	public void drawing(){
		//Draws the two circles
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Sets the settings for the Venn Diagram
		g.setColor(Color.RED);
		g.drawOval(50, 100, 250, 250);
		g.setColor(Color.GREEN);
		g.drawOval(175, 100, 250, 250);
	}

}
