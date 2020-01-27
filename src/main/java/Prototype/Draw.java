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
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Sets the settings for the Venn Diagram
		
		g.setColor(new Color(0, 150, 255, 125));
		g.fillOval(50, 100, xPos, yPos);
		g.setColor(new Color(0, 150, 255, 125));
		g.fillOval(distance, 100, xPos, yPos);	
	
	}
	
	public void addMenu(MainFrame f) {
		Menu m = new Menu();
		JMenuBar mb; // MenuBar
		JMenu x; // JMenu
		JMenuItem m1, m2, m3, m4; // MenuItems
		
		mb = new JMenuBar();
		x = new JMenu("Menu"); 
		
		m1 = new JMenuItem("New");
		m2 = new JMenuItem("Insert");
		m3 = new JMenuItem("Save");
		m4 = new JMenuItem("Save as");
		m1.addActionListener(m);
		m2.addActionListener(m);
		m3.addActionListener(m);
		m4.addActionListener(m);
		x.add(m1);
		x.add(m2);
		x.add(m3);
		x.add(m4);
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
