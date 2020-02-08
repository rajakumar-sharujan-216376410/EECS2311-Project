package Prototype;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Label;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
	private JTextField circle1;
	private JTextField circle2;
	private Draw object;
	protected int counter = 0;
	
	
	
	public MainFrame() {
		//Creates empty JFrame window with the name 'Venn App'
		super("Venn App");

		//Sets the Layout to Border Layout style
		setLayout(new BorderLayout());
		
		//Creates panel
		panel = new JPanel();
		Border blackline = BorderFactory.createLineBorder(Color.BLACK, 2);
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBorder(blackline);
		
		//Creates show & hide buttons
		addInputs = new JButton("Add inputs");
		hide = new JButton("Hide");
		createDiagram = new JButton("Create the diagram");
		
		//Adds the button click Listener
		addInputs.addActionListener(this);
		hide.addActionListener(this);
		createDiagram.addActionListener(this); 

		//Sets the settings for the window 
		
		setSize(1000, 900);
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
		object.setVisible(true);
		
		JTextField circle1 = new JTextField(6);
		circle1.setText("Circle1");
		circle1.setAlignmentX(0);
		circle1.setAlignmentY(0);
		circle1.setBackground(null);
		circle1.setHorizontalAlignment(SwingConstants.CENTER);
		circle1.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
				
				circle1.setBackground(Color.WHITE);
				circle1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				
			}
			
			public void focusLost(FocusEvent arg0) {
				if(circle1.getText().equals("")) {
					circle1.setText("Circle1");
				}
				
				circle1.setBackground(null);
				circle1.setBackground(null);
				
			}
		});
		
		JTextField circle2 = new JTextField(6);
		circle2.setText("Circle2");
		circle1.setLocation(90, 90); 
		circle2.setBackground(null);
		circle2.setHorizontalAlignment(SwingConstants.CENTER);
		circle2.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
		
					circle2.setBackground(Color.WHITE);
					circle2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
					
				
			}
			
			public void focusLost(FocusEvent arg0) {
				if(circle2.getText().equals("")) {
					circle2.setText("Circle2");
				}
				circle2.setBackground(null);
				circle2.setBackground(null);
			
			
		}});
		
		
		object.add(circle1);
		object.add(circle2);
		object.addMenu(this); 
	}
	
	//This method is run once a button is clicked
	public void actionPerformed (ActionEvent e) {
		//Action for Show Button
		if (e.getSource() == addInputs){
			
			//Shows the drawing
			counter++;
			object.setVisible(true);
			//object.addInput();		
			
		}
		//Action for Hide button
		else if (e.getSource() == hide) {
			
			//Hides the drawing 
			object.setVisible(false);
		}
		else if (e.getSource() == createDiagram) {
			
			object.drawing();
		}
		
		else if (e.getSource() == circle1)
			circle1.setText("Hello guys");
		
	}
	
	
	
}
