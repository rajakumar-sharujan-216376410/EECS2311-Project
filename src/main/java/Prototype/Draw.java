package Prototype;

import javax.swing.*;

import Prototype.Menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class Draw extends JPanel {
	
	private int xPos, yPos, distance;
	private String user, user2, user3;
	Menu m = new Menu();
	JMenuBar mb; // MenuBar
	JMenu x; // JMenu
	JMenuItem m1, m2, m3, m4; // MenuItems
	
	static private final String newline = "\n";
	JButton openButton, saveButton;
	JTextArea log;
	JFileChooser fc;
	
	public void drawing() {
		//Draws the two circles
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Sets the settings for the Venn Diagram
		
		g.setColor(new Color(0, 150, 255, 150));
		g.fillOval(50, 100, xPos, yPos);
		g.setColor(new Color(255, 0, 0, 150));
		g.fillOval(distance, 100, xPos, yPos);	
	
	}
	
	public void addTextBox() {
		JTextField text = new JTextField(20);
		text.setText("hello"); 
	}
	
	public void addMenu(MainFrame f) {
		
		mb = new JMenuBar();
		x = new JMenu("Menu"); 
		
		m1 = new JMenuItem("New");
		m2 = new JMenuItem("Open");
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
	
	public void openFiles() {
		
				log = new JTextArea(5,20);
		        log.setMargin(new Insets(5,5,5,5));
		        log.setEditable(false);
		        JScrollPane logScrollPane = new JScrollPane(log);
		        fc = new JFileChooser();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m1) { //new
			System.out.println("NEW. CHECK");
		}
		if (e.getSource() == m2) { //Open
			int returnVal = fc.showOpenDialog(Draw.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
		}
		if (e.getSource() == m3) { //save
			System.out.println("SAVE. CHECK");
		}
		if (e.getSource() == m4) { //save as
			System.out.println("SAVE AS. CHECK");
		}

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
