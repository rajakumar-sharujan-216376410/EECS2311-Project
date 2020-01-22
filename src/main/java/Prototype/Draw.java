package Prototype;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Draw extends JPanel {
	
	private int xPos, yPos, distance;
	private String user, user2, user3;
	
	public void drawing() {
		//Draws the two circles
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Sets the settings for the Venn Diagram
		
		g.setColor(Color.RED);
		g.fillOval(50, 100, xPos, yPos);
		g.setColor(Color.GREEN);
		g.fillOval(distance, 100, xPos, yPos);
	}
	
	public void addInput() {
		user = JOptionPane.showInputDialog(this, "Enter the x position of the circle: ");
		user2 = JOptionPane.showInputDialog(this, "Enter the y position of the circle: ");
		user3 = JOptionPane.showInputDialog(this, "Enter the separation distance of the circles: ");
		
		xPos = Integer.parseInt(user);
		yPos = Integer.parseInt(user2);
		distance = Integer.parseInt(user3);
		
	}
}
