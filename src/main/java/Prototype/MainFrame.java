package Prototype;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.*;

//Creates the frame for the application
public class MainFrame extends JFrame implements ActionListener {
	
	private JPanel panel;
	private JButton addInputs;
	private JButton hide;
	private JButton createDiagram;
	private Draw object;
	protected int counter = 0;
	
	public MainFrame() {
		//Creates empty JFrame window with the name 'Venn App'
		super("Venn App");

		//Sets the Layout to Border Layout style
		setLayout(new BorderLayout());
		
		//Creates panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		//Creates show & hide buttons
		addInputs = new JButton("Add inputs");
		hide = new JButton("Hide");
		createDiagram = new JButton("Create the diagram");
		
		//Adds the button click Listener
		addInputs.addActionListener(this);
		hide.addActionListener(this);
		createDiagram.addActionListener(this); 

		//Sets the settings for the window 
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//creates the new drawing
		object = new Draw();
		
		//adds the Components 
		panel.add(addInputs);
		panel.add(hide);
		panel.add(createDiagram);
		add(panel, BorderLayout.WEST);
		add(object, BorderLayout.CENTER);
		
		//Draws the two circles and hides the Venn diagram
		object.drawing();
		object.setVisible(false);
		
		JTextField text = new JTextField(6);
		text.setText("Similarities");
		text.moveCaretPosition(10); 
		object.add(text);
		
		object.addMenu(this); 
	}
	
	//This method is run once a button is clicked
	public void actionPerformed (ActionEvent e) {
		//Action for Show Button
		if (e.getSource() == addInputs){
			
			//Shows the drawing
			counter++;
			object.setVisible(true);
			object.addInput();		
			
		}
		//Action for Hide button
		else if (e.getSource() == hide) {
			
			//Hides the drawing 
			object.setVisible(false);
		}
		else if (e.getSource() == createDiagram) {
			
			object.drawing();
		}
	}
}
