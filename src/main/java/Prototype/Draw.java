package Prototype;

import javax.swing.*;

import Prototype.Menu;

import java.awt.*;
import java.util.Scanner;

public class Draw extends JPanel {
	
	private int xPos, yPos, distance;
	private String user, user2, user3;
	
	public void drawing() {
		//Draws the two circles
		repaint();
	}

	public AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return AlphaComposite.getInstance(type, alpha);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Sets the settings for the Venn Diagram
		
		g.setColor(Color.RED);
		g.drawOval(50, 100, xPos, yPos);
		g.setColor(Color.GREEN);
		g.drawOval(distance, 100, xPos, yPos);
		//g.setComposite(makeComposite((float) 0.75)); 
	}
	
	public void addMenu(MainFrame f) {
		Menu m = new Menu();
		JMenuBar mb; // MenuBar
		JMenu x; // JMenu
		JMenuItem m1; // MenuItems
		
		mb = new JMenuBar();
		x = new JMenu("Menu"); 
		
		m1 = new JMenuItem("Insert");
		m1.addActionListener(m);
		x.add(m1);
		mb.add(x);
		f.setJMenuBar(mb);
		f.setSize(500, 500);
		f.setVisible(true);
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
